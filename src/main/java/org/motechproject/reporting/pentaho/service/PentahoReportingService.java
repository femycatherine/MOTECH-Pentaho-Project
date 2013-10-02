package org.motechproject.reporting.pentaho.service;

import org.motechproject.reporting.pentaho.exception.PentahoJobException;
import org.motechproject.reporting.pentaho.exception.StatusParserException;
import org.motechproject.reporting.pentaho.request.PentahoStartTransRequest;
import org.motechproject.reporting.pentaho.request.PentahoStatusRequest;
import org.motechproject.reporting.pentaho.request.PentahoExecuteTransRequest;
import org.motechproject.reporting.pentaho.status.ServerStatus;

public interface PentahoReportingService {

    void startTrans(PentahoStartTransRequest request);

    void executeTrans(PentahoExecuteTransRequest request);

    void deleteTrans(String transId);

    ServerStatus getServerStatus(PentahoStatusRequest request) throws StatusParserException;

    void scheduleMonthlyExecTrans(String executionInstanceId, int minute, int hour, int dayOfMonth) throws PentahoJobException;

    void scheduleWeeklyExecTrans(String executionInstanceId, int minute, int hour, int day)
            throws PentahoJobException;

    void scheduleDailyExecTrans(String executionInstanceId, int hour, int minute) throws PentahoJobException;
}
