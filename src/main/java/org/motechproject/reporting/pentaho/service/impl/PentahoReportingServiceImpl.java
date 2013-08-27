package org.motechproject.reporting.pentaho.service.impl;

import java.util.List;
import org.motechproject.event.MotechEvent;
import org.motechproject.reporting.pentaho.exception.PentahoJobException;
import org.motechproject.reporting.pentaho.exception.StatusParserException;
import org.motechproject.reporting.pentaho.parser.StatusParser;
import org.motechproject.reporting.pentaho.request.PentahoStartTransRequest;
import org.motechproject.reporting.pentaho.request.PentahoStatusRequest;
import org.motechproject.reporting.pentaho.request.PentahoStopTransRequest;
import org.motechproject.reporting.pentaho.request.PentahoExecuteTransRequest;
import org.motechproject.reporting.pentaho.service.PentahoReportingService;
import org.motechproject.reporting.pentaho.status.ServerStatus;
import org.motechproject.reporting.pentaho.util.JobSchedulerUtil;
import org.motechproject.reporting.pentaho.util.PentahoCarteHttpClient;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.motechproject.scheduler.domain.JobBasicInfo;
import org.motechproject.scheduler.domain.JobDetailedInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PentahoReportingServiceImpl implements PentahoReportingService {

    @Autowired
    private MotechSchedulerService schedulerService;

    @Autowired
    private PentahoCarteHttpClient httpClient;

    @Override
    public void stopTrans(PentahoStopTransRequest request) {
        // TODO Auto-generated method stub

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

    public void endReportJob(String executionInstanceId) {
//        String dailyJobId = "";
//        String weeklyJobId = "";
//        String monthlyJobId = "";
        
        //TODO
        schedulerService.safeUnscheduleAllJobs(executionInstanceId);
    }

    public boolean jobIsRunning(String executionInstanceId, String typeOfJob) {
        String jobId = typeOfJob + "-" + executionInstanceId;

        JobBasicInfo jobBasicInfo = new JobBasicInfo();
        jobBasicInfo.setName(jobId);

        JobDetailedInfo info = schedulerService.getScheduledJobDetailedInfo(jobBasicInfo);

        if (info.getEventInfoList().size() == 0) {
            return false;
        }

        return true;
    }

    @Override
    public void scheduleDailyExecTrans(String executionInstanceId, int hour) throws PentahoJobException {
        String hourString = validateAndParseValue(hour, 0, 23);
        MotechEvent dailyEvent = new MotechEvent("dailyPentahoReport");
        dailyEvent.getParameters().put(MotechSchedulerService.JOB_ID_KEY, executionInstanceId);

        CronSchedulableJob job = new CronSchedulableJob(dailyEvent, JobSchedulerUtil.getDailyCronExpression(hourString));

        schedulerService.safeScheduleJob(job);
    }
    
    @Override
    public void scheduleWeeklyExexTrans(String executionInstanceId, int hour, int day, PentahoExecuteTransRequest request) throws PentahoJobException {
        String hourString = validateAndParseValue(hour, 0, 23);
        String dayString = validateAndParseValue(day, 0, 6);
        
        MotechEvent weeklyEvent = new MotechEvent("weeklyPentahoReport");
        weeklyEvent.getParameters().put(MotechSchedulerService.JOB_ID_KEY, executionInstanceId);
        
        CronSchedulableJob job = new CronSchedulableJob(weeklyEvent, JobSchedulerUtil.getWeeklyCronExpression(hourString,  dayString));
        
        schedulerService.safeScheduleJob(job);
    }

    private String validateAndParseValue(Integer value, int lowerBound, int upperBound) throws PentahoJobException {
        if (value < lowerBound || value > upperBound) {
            throw new PentahoJobException("Value constraint violated");
        }

        return value.toString();
    }

    @Override
    public void schedule(String cronExpression, PentahoExecuteTransRequest request) {
        List<JobBasicInfo> jobList = schedulerService.getScheduledJobsBasicInfo();

        //        "org.motechproject.reporting.pentaho.daily-job.reportName";
        //        "org.motechproject.reporting.pentaho.weekly-job.reportName";
        //        
        for (JobBasicInfo job : jobList) {
            job.getName();
            job.getNextFireDate();
            job.getStartDate();
            job.getInfo();
        }
    }
}
