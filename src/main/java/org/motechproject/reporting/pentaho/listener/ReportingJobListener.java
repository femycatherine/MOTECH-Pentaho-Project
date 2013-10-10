package org.motechproject.reporting.pentaho.listener;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.joda.time.DateTime;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.motechproject.event.MotechEvent;
import org.motechproject.event.listener.annotations.MotechListener;
import org.motechproject.reporting.pentaho.domain.ParamConfig;
import org.motechproject.reporting.pentaho.domain.PentahoExecuteTransInstance;
import org.motechproject.reporting.pentaho.repository.AllPentahoTransformations;
import org.motechproject.reporting.pentaho.request.PentahoExecuteTransRequest;
import org.motechproject.reporting.pentaho.service.PentahoReportingService;
import org.motechproject.reporting.pentaho.web.SettingsController;
import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import static org.motechproject.commons.date.util.DateUtil.setTimeZoneUTC;

@Component
public class ReportingJobListener {

    private static final String UTF_8_ENCODING = "UTF-8";

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SettingsFacade settings;

    @Autowired
    private PentahoReportingService reportingService;

    @Autowired
    private AllPentahoTransformations allTransformations;

    @MotechListener(subjects = { "dailyPentahoReport", "weeklyPentahoReport" })
    public void handleReportJob(MotechEvent event) {
        String transId = (String) event.getParameters().get("transId");
        PentahoExecuteTransInstance trans = allTransformations.get(transId);
        PentahoExecuteTransRequest transRequest = new PentahoExecuteTransRequest();
        transRequest.setParams(convertParamMap(convertList(trans.getParams()), trans.getHourOfDay(),
                trans.getDayOfWeek(), trans.getDayOfMonth()));
        transRequest.setTransName(trans.getTransName());
        reportingService.executeTrans(transRequest);
    }

    private Map<String, ParamConfig> convertList(List<ParamConfig> params) {
        Map<String, ParamConfig> paramMap = new LinkedHashMap<String, ParamConfig>();
        if (params != null) {
            for (ParamConfig param : params) {
                paramMap.put(param.getParamName(), param);
            }
        }

        return paramMap;
    }

    private Map<String, String> convertParamMap(Map<String, ParamConfig> params, Integer hourOfDay, Integer dayOfWeek,
            Integer dayOfMonth) {

        Map<String, String> convertedParams = new HashMap<String, String>();

        for (Map.Entry<String, ParamConfig> entry : params.entrySet()) {
            convertedParams.put(entry.getKey(), convertParamConfig(entry.getValue(), hourOfDay, dayOfWeek, dayOfMonth));
        }

        appendParams(convertedParams, params);

        encodeDates(convertedParams, params);

        return convertedParams;
    }

    private void encodeDates(Map<String, String> convertedParams, Map<String, ParamConfig> params) {
        for (Map.Entry<String, ParamConfig> entry : params.entrySet()) {
            ParamConfig config = entry.getValue();
            if ("DATETIME".equals(config.getType().toString())) {
                String dateString = convertedParams.get(entry.getKey());
                try {
                    dateString = URLEncoder.encode(dateString, UTF_8_ENCODING);
                    convertedParams.put(entry.getKey(), dateString);
                } catch (UnsupportedEncodingException e) {
                    logger.warn("UnsupportedEncodingException encoding date [" + dateString + "]: " + e);
                }
            }
        }
    }

    private void appendParams(Map<String, String> convertedParams, Map<String, ParamConfig> params) {
        for (Map.Entry<String, ParamConfig> entry : params.entrySet()) {
            StringBuilder value = new StringBuilder(convertedParams.get(entry.getKey()));
            ParamConfig config = entry.getValue();
            String appendValue = config.getAppendValue();

            if (config.isAppendFieldValue()) {
                ParamConfig appendConfig = params.get(config.getAppendValue());
                if ("DATETIME".equals(appendConfig.getType().toString())) {
                    DateTime appendTime = DateTime.parse(convertedParams.get(appendValue));
                    DateTimeFormatter format = DateTimeFormat.forPattern("MM-dd-YYYY");
                    value.append(format.print(appendTime));
                } else {
                    value.append(convertedParams.get(appendValue));

                }
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

            DateTime now = DateTime.now().withSecondOfMinute(0).withMillisOfSecond(0);

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
                DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS");
                return formatter.print(now.getMillis());
            }

            return setTimeZoneUTC(now).toString();
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

        if (timeOffset.toLowerCase().contains("hours") || timeOffset.toLowerCase().contains("hour")) {
            return Period.hours(value);
        } else if (timeOffset.toLowerCase().contains("days") || timeOffset.toLowerCase().contains("day")) {
            return Period.days(value);
        } else if (timeOffset.toLowerCase().contains("weeks") || timeOffset.toLowerCase().contains("week")) {
            return Period.days(value);
        } else if (timeOffset.toLowerCase().contains("months") || timeOffset.toLowerCase().contains("month")) {
            return Period.days(value);
        }
        return Period.ZERO;
    }
}
