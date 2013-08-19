package org.motechproject.reporting.pentaho.web;

import java.util.HashMap;
import java.util.Map;

import org.joda.time.DateTime;
import org.motechproject.reporting.pentaho.exception.StatusParserException;
import org.motechproject.reporting.pentaho.request.PentahoExecuteTransRequest;
import org.motechproject.reporting.pentaho.request.PentahoStatusRequest;
import org.motechproject.reporting.pentaho.service.PentahoReportingService;
import org.motechproject.reporting.pentaho.status.ServerStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class PentahoCarteController {

    @Autowired
    private PentahoReportingService reportingService;

    @RequestMapping(value = "/status", method = RequestMethod.GET)
    @ResponseBody
    public ServerStatus settings() throws StatusParserException {

        PentahoExecuteTransRequest request = new PentahoExecuteTransRequest();

        request.setTransName("dailyReport.ktr");

        Map<String, String> params = new HashMap<String, String>();

        DateTime startDate = new DateTime(2013, 8, 1, 0, 0);
        DateTime endDate = new DateTime(2013, 9, 1, 0, 0);

        params.put("fileName", "testingWebApiFile");
        params.put("startDate", startDate.toString());
        params.put("endDate", endDate.toString());
        request.setParams(params);

        reportingService.executeTrans(request);

        PentahoStatusRequest statusRequest = new PentahoStatusRequest();

        ServerStatus status = reportingService.getServerStatus(statusRequest);

        return status;
    }
}
