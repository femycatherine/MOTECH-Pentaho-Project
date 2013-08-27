package org.motechproject.reporting.pentaho.listener;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.reporting.pentaho.domain.ParamConfig;
import org.motechproject.reporting.pentaho.domain.PentahoExecuteTransInstance;
import org.motechproject.reporting.pentaho.repository.AllPentahoTransformations;
import org.motechproject.reporting.pentaho.request.PentahoExecuteTransRequest;
import org.motechproject.reporting.pentaho.service.PentahoReportingService;
import org.motechproject.reporting.pentaho.web.SettingsController;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.server.config.SettingsFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ReportingJobListener {

    @Autowired
    private SettingsFacade settings;

    @Autowired
    private PentahoReportingService reportingService;

    @Autowired
    private AllPentahoTransformations allTransformations;

    @MotechListener(subjects = {"dailyReportJob", "weeklyReportJob"})
    public void handleReportJob(MotechEvent event) {
        String transId = (String) event.getParameters().get(MotechSchedulerService.JOB_ID_KEY);
        PentahoExecuteTransInstance trans = allTransformations.get(transId);
        PentahoExecuteTransRequest transRequest = new PentahoExecuteTransRequest();
        transRequest.setParams(convertParamMap(trans.getParams(), trans.getHourOfDay(), trans.getDayOfWeek(), trans.getDayOfMonth()));
        transRequest.setTransName(trans.getTransName());
        reportingService.executeTrans(transRequest);
    }

    private Map<String, String> convertParamMap(Map<String, ParamConfig> params, Integer hourOfDay, Integer dayOfWeek, Integer dayOfMonth) {
        if (params == null || params.size() == 0) {
            return Collections.emptyMap();
        }

        Map<String, String> convertedParams = new HashMap<String, String>();

        for (Map.Entry<String, ParamConfig> entry : params.entrySet()) {
            convertedParams.put(entry.getKey(), convertParamConfig(entry.getValue(), hourOfDay, dayOfWeek, dayOfMonth));
        }

        appendParams(convertedParams, params);

        return convertedParams;
    }

    private void appendParams(Map<String, String> convertedParams, Map<String, ParamConfig> params) {
        for (Map.Entry<String, ParamConfig> entry : params.entrySet()) {
            StringBuilder value = new StringBuilder(convertedParams.get(entry.getKey()));
            ParamConfig config = entry.getValue();
            String appendValue = config.getAppendValue();
            if (config.isAppendFieldValue()) {
                value.append(convertedParams.get(appendValue));
            } else {
                if (appendValue != null) {
                    value.append(appendValue);
                }
            }
            convertedParams.put(entry.getKey(), value.toString());
        }
    }

    private String convertParamConfig(ParamConfig paramConfig, Integer hourOfDay, Integer dayOfWeek, Integer dayOfMonth) {
        if ("DATETIME".equals(paramConfig.getType().toString())) {

            DateTime now = DateTime.now();

            if (dayOfMonth != null) {
                now = now.withDayOfMonth(dayOfMonth);
            }
            if (dayOfWeek != null) {
                now = now.withDayOfWeek(dayOfWeek);
            }
            if (hourOfDay != null) {
                now = now.withHourOfDay(hourOfDay);
            }


            now = offsetTime(now, paramConfig.getTimeOffset());

            boolean luceneDateFormat = Boolean.parseBoolean(settings.getProperty(SettingsController.LUCENE_KEY));


            if (luceneDateFormat) {
                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSSZ");

                return formatter.print(now.getMillis());
            }

            return now.toString();
        } 

        return paramConfig.getValue() == null ? "" : paramConfig.getValue();
    }

    private DateTime offsetTime(DateTime now, String timeOffset) {
        boolean isPlus = parseOffsetDirection(timeOffset);

        Period period = parseTimeOffset(timeOffset);

        if (isPlus) {
            return now.plus(period);
        }
        return now.minus(period);
    }

    private boolean parseOffsetDirection(String timeOffset) {
        if (timeOffset.contains("+")) {
            return true;
        }
        return false;
    }

    private Period parseTimeOffset(String timeOffset) {
        String[] split = timeOffset.split(" ");

        Integer value = Integer.parseInt(split[1]);

        if (timeOffset.toLowerCase().contains("hours")) {
            return Period.hours(value);
        } else if (timeOffset.toLowerCase().contains("days")) {
            return Period.days(value);
        } else if (timeOffset.toLowerCase().contains("weeks")) {
            return Period.days(value);
        } else if (timeOffset.toLowerCase().contains("months")) {
            return Period.days(value);
        }
        return Period.ZERO;
    }
}
