package org.motechproject.reporting.pentaho.web;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
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

    @RequestMapping(value = "transformations", method = RequestMethod.GET)
    @ResponseBody
    public List<PentahoExecuteTransInstance> getTransformations() throws BundleException {
        return pentahoTransformations.getAll();
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/transformations", method = RequestMethod.POST)
    public void saveTransformation(@RequestBody PentahoExecuteTransInstance transformation) throws BundleException {

        pentahoTransformations.update(transformation);
        //pentahoTransformations.add(transformation);
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @RequestMapping(value = "/transformations", method = RequestMethod.DELETE)
    public void deleteTransformation(@RequestBody PentahoExecuteTransInstance transformation) throws BundleException {
        pentahoTransformations.remove(transformation);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/startTransformation", method = RequestMethod.GET)
    public void startTransformation() throws BundleException, PentahoJobException {
//        String transId = transformation.getId();
//        Integer hourOfDay = transformation.getHourOfDay();

        reportingService.scheduleDailyExecTrans("570395ea0356e0e1efb1a3ef0101a739", 5);
        //        transformation.getDayOfWeek();
        //        transformation.getDayOfMonth();
        //make assumptions about type of job
    }
}
