package org.motechproject.reporting.pentaho.status;

import java.util.List;

import org.codehaus.jackson.annotate.JsonProperty;

public class ServerStatus {

    @JsonProperty
    private String statusDescription;
    @JsonProperty
    private List<TransStatus> transStatusList;

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public List<TransStatus> getTransStatusList() {
        return transStatusList;
    }

    public void setTransStatusList(List<TransStatus> transStatusList) {
        this.transStatusList = transStatusList;
    }
}
