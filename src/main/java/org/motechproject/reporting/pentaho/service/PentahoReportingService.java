package org.motechproject.reporting.pentaho.service;

import org.motechproject.reporting.pentaho.exception.PentahoJobException;
import org.motechproject.reporting.pentaho.exception.StatusParserException;
import org.motechproject.reporting.pentaho.request.PentahoStartTransRequest;
import org.motechproject.reporting.pentaho.request.PentahoStatusRequest;
import org.motechproject.reporting.pentaho.request.PentahoStopTransRequest;
import org.motechproject.reporting.pentaho.request.PentahoExecuteTransRequest;
import org.motechproject.reporting.pentaho.status.ServerStatus;

public interface PentahoReportingService {

    void startTrans(PentahoStartTransRequest request);
    
    void executeTrans(PentahoExecuteTransRequest request);

    void stopTrans(PentahoStopTransRequest request);

    ServerStatus getServerStatus(PentahoStatusRequest request) throws StatusParserException;
    
    void schedule(String cronExpression, PentahoExecuteTransRequest request);

    void scheduleWeeklyExexTrans(String executionInstanceId, int hour, int day, PentahoExecuteTransRequest request)
            throws PentahoJobException;

    void scheduleDailyExecTrans(String executionInstanceId, int hour) throws PentahoJobException;
}
