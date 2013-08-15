package org.motechproject.reporting.pentaho.domain;

import java.util.Map;

import org.joda.time.DateTime;

public class PentahoExecuteTransInstance {

    private DateTime startDate; //purely for display purposes
    private String cronExpression; //cron expression for the firing of the job event
    private Map<String, ParamConfig> params; //parameters and what values they will be passed as
    private String transName;  //name of the transformation
    
}
