package org.motechproject.reporting.pentaho.domain;

import java.util.Map;

import org.codehaus.jackson.annotate.JsonProperty;
import org.ektorp.support.TypeDiscriminator;
import org.joda.time.DateTime;
import org.motechproject.commons.couchdb.model.MotechBaseDataObject;

/**
 * Class that represents a pentaho execution configuration. When a reporting job
 * is scheduled, a configuration instance is created and persisted in the database.
 * Upon event triggers, the configuration will be retrieved and inspected in order
 * to query Carte with the transformation name and any potential parameters. Parameter
 * values can be static or determined at run time. See ParamConfig for more information
 * on how to configure the optional parameters sent to Pentaho.
 *
 */
@TypeDiscriminator("doc.type === 'TransformationConfiguration'")
public class PentahoExecuteTransInstance extends MotechBaseDataObject {

    @JsonProperty
    private DateTime startDate; //purely for display purposes
    @JsonProperty
    private String cronExpression; //cron expression for the firing of the job event
    @JsonProperty
    private Map<String, ParamConfig> params; //parameters and what values they will be passed as
    @JsonProperty
    private String transName;  //name of the transformation

    public DateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(DateTime startDate) {
        this.startDate = startDate;
    }

    public String getCronExpression() {
        return cronExpression;
    }

    public void setCronExpression(String cronExpression) {
        this.cronExpression = cronExpression;
    }

    public Map<String, ParamConfig> getParams() {
        return params;
    }

    public void setParams(Map<String, ParamConfig> params) {
        this.params = params;
    }

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }
}
