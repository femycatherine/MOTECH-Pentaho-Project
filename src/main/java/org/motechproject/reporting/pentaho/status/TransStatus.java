package org.motechproject.reporting.pentaho.status;

import org.codehaus.jackson.annotate.JsonProperty;

public class TransStatus {

    @JsonProperty
    private String transName;
    @JsonProperty
    private String id;
    @JsonProperty
    private String statusDescription;
    @JsonProperty
    private String errorDescription;
    @JsonProperty
    private Boolean paused;

    public String getTransName() {
        return transName;
    }

    public void setTransName(String transName) {
        this.transName = transName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatusDescription() {
        return statusDescription;
    }

    public void setStatusDescription(String statusDescription) {
        this.statusDescription = statusDescription;
    }

    public String getErrorDescription() {
        return errorDescription;
    }

    public void setErrorDescription(String errorDescription) {
        this.errorDescription = errorDescription;
    }

    public boolean isPaused() {
        return paused;
    }

    public void setPaused(boolean paused) {
        this.paused = paused;
    }
}
