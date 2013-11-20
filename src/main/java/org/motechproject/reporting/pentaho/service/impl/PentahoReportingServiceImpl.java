package org.motechproject.reporting.pentaho.service.impl;

import org.ektorp.DocumentNotFoundException;
import org.joda.time.DateTime;
import org.motechproject.event.MotechEvent;
import org.motechproject.reporting.pentaho.domain.PentahoExecuteTransInstance;
import org.motechproject.reporting.pentaho.exception.PentahoJobException;
import org.motechproject.reporting.pentaho.exception.StatusParserException;
import org.motechproject.reporting.pentaho.parser.StatusParser;
import org.motechproject.reporting.pentaho.repository.AllPentahoTransformations;
import org.motechproject.reporting.pentaho.request.PentahoStartTransRequest;
import org.motechproject.reporting.pentaho.request.PentahoStatusRequest;
import org.motechproject.reporting.pentaho.request.PentahoExecuteTransRequest;
import org.motechproject.reporting.pentaho.service.PentahoReportingService;
import org.motechproject.reporting.pentaho.status.ServerStatus;
import org.motechproject.reporting.pentaho.util.JobSchedulerUtil;
import org.motechproject.reporting.pentaho.util.PentahoCarteHttpClient;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.motechproject.scheduler.domain.RunOnceSchedulableJob;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PentahoReportingServiceImpl implements PentahoReportingService {
    
    private final static String TRANS_ID = "transId";

    @Autowired
    private AllPentahoTransformations allTransformations;

    @Autowired
    private MotechSchedulerService schedulerService;

    @Autowired
    private PentahoCarteHttpClient httpClient;

    @Override
    public void deleteTrans(String transId) {
        schedulerService.safeUnscheduleJob("dailyPentahoReport", transId);
        schedulerService.safeUnscheduleJob("weeklyPentahoReport", transId);
        schedulerService.safeUnscheduleJob("monthlyPentahoReport", transId);

        PentahoExecuteTransInstance instance = null;

        try {
            instance = allTransformations.get(transId);
        } catch (DocumentNotFoundException e) {
            return;
        }

        if (instance != null) {
            allTransformations.remove(instance);
        }
    }

    @Override
    public ServerStatus getServerStatus(PentahoStatusRequest request) throws StatusParserException {
        String responseString = httpClient.statusRequest(request);

        StatusParser parser = new StatusParser(responseString);

        return parser.parse();
    }

    @Override
    public void executeTrans(PentahoExecuteTransRequest request) {
        httpClient.transRequest(request);
    }

    @Override
    public void startTrans(PentahoStartTransRequest request) {
        // TODO Auto-generated method stub
    }

    @Override
    public void scheduleImmediateExecTrans(String executionInstanceId) throws PentahoJobException {
        MotechEvent immediateEvent = new MotechEvent("immediatePentahoReport");
        immediateEvent.getParameters().put(MotechSchedulerService.JOB_ID_KEY, executionInstanceId);
        immediateEvent.getParameters().put(TRANS_ID, executionInstanceId);

        RunOnceSchedulableJob job = new RunOnceSchedulableJob(immediateEvent, DateTime.now().toDate());
        
        schedulerService.safeScheduleRunOnceJob(job);
    }

    
    @Override
    public void scheduleDailyExecTrans(String executionInstanceId, int minute, int hour) throws PentahoJobException {
        String hourString = validateAndParseValue(hour, 0, 23);
        String minuteString = validateAndParseValue(minute, 0, 59);
        MotechEvent dailyEvent = new MotechEvent("dailyPentahoReport");
        dailyEvent.getParameters().put(MotechSchedulerService.JOB_ID_KEY, executionInstanceId);
        dailyEvent.getParameters().put(TRANS_ID, executionInstanceId);

        CronSchedulableJob job = new CronSchedulableJob(dailyEvent, JobSchedulerUtil.getDailyCronExpression(minuteString, hourString));

        schedulerService.safeScheduleJob(job);
    }

    @Override
    public void scheduleWeeklyExecTrans(String executionInstanceId, int minute, int hour, int day) throws PentahoJobException {
        String hourString = validateAndParseValue(hour, 0, 23);
        String dayString = validateAndParseValue(day, 1, 7);
        String minuteString = validateAndParseValue(minute, 0, 59);

        MotechEvent weeklyEvent = new MotechEvent("weeklyPentahoReport");
        weeklyEvent.getParameters().put(MotechSchedulerService.JOB_ID_KEY, executionInstanceId);
        weeklyEvent.getParameters().put(TRANS_ID, executionInstanceId);

        CronSchedulableJob job = new CronSchedulableJob(weeklyEvent, JobSchedulerUtil.getWeeklyCronExpression(minuteString, hourString,  dayString));

        schedulerService.safeScheduleJob(job);
    }


    @Override
    public void scheduleMonthlyExecTrans(String executionInstanceId, int minute, int hour, int dayOfMonth)
            throws PentahoJobException {
        String hourString = validateAndParseValue(hour, 0, 23);
        String dayString = validateAndParseValue(dayOfMonth, 1, 31);
        String minuteString = validateAndParseValue(minute, 0, 59);

        MotechEvent monthlyEvent = new MotechEvent("monthlyPentahoReport");
        monthlyEvent.getParameters().put(MotechSchedulerService.JOB_ID_KEY, executionInstanceId);
        monthlyEvent.getParameters().put(TRANS_ID, executionInstanceId);

        CronSchedulableJob job = new CronSchedulableJob(monthlyEvent, JobSchedulerUtil.getMonthlyCronExpression(minuteString, hourString,  dayString));

        schedulerService.safeScheduleJob(job);
    }

    private String validateAndParseValue(Integer value, int lowerBound, int upperBound) throws PentahoJobException {
        if (value < lowerBound || value > upperBound) {
            throw new PentahoJobException("Value constraint violated");
        }

        return value.toString();
    }
}
