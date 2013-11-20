package org.motechproject.reporting.pentaho.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.ektorp.UpdateConflictException;
import org.joda.time.DateTime;
import org.motechproject.reporting.pentaho.domain.PentahoExecuteTransInstance;
import org.motechproject.reporting.pentaho.exception.PentahoJobException;
import org.motechproject.reporting.pentaho.exception.StatusParserException;
import org.motechproject.reporting.pentaho.repository.AllPentahoTransformations;
import org.motechproject.reporting.pentaho.request.PentahoExecuteTransRequest;
import org.motechproject.reporting.pentaho.request.PentahoStatusRequest;
import org.motechproject.reporting.pentaho.service.PentahoReportingService;
import org.motechproject.reporting.pentaho.status.ServerStatus;
import org.osgi.framework.BundleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
@RequestMapping("/transformations")
public class TransformationsController {

    @Autowired
    private PentahoReportingService reportingService;

    @Autowired
    private AllPentahoTransformations pentahoTransformations;

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

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public List<PentahoExecuteTransInstance> getTransformations() throws BundleException {
        return pentahoTransformations.getAll();
    }

    @RequestMapping(value = "", method = RequestMethod.PUT)
    @ResponseBody
    public void startTransformation(@RequestBody PentahoExecuteTransInstance transformation) throws BundleException,
            PentahoJobException {

        updateTransformation(transformation);

        if (transformation.getDayOfMonth() != null) {
            // schedule monthly
            reportingService.scheduleMonthlyExecTrans(transformation.getId(), transformation.getMinuteOfHour(),
                    transformation.getHourOfDay(), transformation.getDayOfMonth());
        } else if (transformation.getDayOfWeek() != null) {
            // schedule weekly
            reportingService.scheduleWeeklyExecTrans(transformation.getId(), transformation.getMinuteOfHour(),
                    transformation.getHourOfDay(), transformation.getDayOfWeek());
        } else {
            // schedule daily
            reportingService.scheduleDailyExecTrans(transformation.getId(), transformation.getMinuteOfHour(),
                    transformation.getHourOfDay());
        }
    }

    @RequestMapping(value = "/immediate", method = RequestMethod.PUT)
    @ResponseBody
    public void runTransformationImmediately(@RequestBody PentahoExecuteTransInstance transformation)
            throws BundleException, PentahoJobException {

        updateTransformation(transformation);

        reportingService.scheduleImmediateExecTrans(transformation.getId());
    }

    @ResponseStatus(HttpStatus.CREATED)
    @RequestMapping(value = "", method = RequestMethod.POST)
    public void saveTransformation(@RequestBody PentahoExecuteTransInstance transformation) throws BundleException {
        pentahoTransformations.add(transformation);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "", method = RequestMethod.DELETE)
    public void deleteTransformation(@RequestParam("transId") String transId) throws BundleException {
        reportingService.deleteTrans(transId);
    }

    private void updateTransformation(PentahoExecuteTransInstance transformation) {
        try {
            pentahoTransformations.update(transformation);
        } catch (UpdateConflictException e) {
            PentahoExecuteTransInstance oldTrans = pentahoTransformations.get(transformation.getId());
            transformation.setRevision(oldTrans.getRevision());
            pentahoTransformations.update(transformation);
        }
    }
}
