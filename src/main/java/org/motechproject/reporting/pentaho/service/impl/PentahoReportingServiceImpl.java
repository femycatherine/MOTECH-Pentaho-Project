package org.motechproject.reporting.pentaho.service.impl;

import java.util.List;

import org.motechproject.event.MotechEvent;
import org.motechproject.reporting.pentaho.domain.PentahoExecuteTransInstance;
import org.motechproject.reporting.pentaho.exception.StatusParserException;
import org.motechproject.reporting.pentaho.parser.StatusParser;
import org.motechproject.reporting.pentaho.request.PentahoStartTransRequest;
import org.motechproject.reporting.pentaho.request.PentahoStatusRequest;
import org.motechproject.reporting.pentaho.request.PentahoStopTransRequest;
import org.motechproject.reporting.pentaho.request.PentahoExecuteTransRequest;
import org.motechproject.reporting.pentaho.service.PentahoReportingService;
import org.motechproject.reporting.pentaho.status.ServerStatus;
import org.motechproject.reporting.pentaho.util.PentahoCarteHttpClient;
import org.motechproject.scheduler.MotechSchedulerService;
import org.motechproject.scheduler.domain.CronSchedulableJob;
import org.motechproject.scheduler.domain.JobBasicInfo;
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

    public void scheduleDailyExecTrans(String executionInstanceId, String cronExpression, PentahoExecuteTransRequest request) {
        MotechEvent dailyEvent = new MotechEvent("dailyEvent");
        dailyEvent.getParameters().put(MotechSchedulerService.JOB_ID_KEY, executionInstanceId);
        
        PentahoExecuteTransInstance executionInstance = new PentahoExecuteTransInstance();

        CronSchedulableJob job = new CronSchedulableJob(dailyEvent, cronExpression);
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
