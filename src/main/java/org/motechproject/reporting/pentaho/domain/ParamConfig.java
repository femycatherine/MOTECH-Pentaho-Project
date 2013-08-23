package org.motechproject.reporting.pentaho.domain;

import java.io.Serializable;

/**
 * A class to configure optional parameters.
 *
 * @param <T> The toString method of this class will be invoked when deciding what 
 * value to pass to Pentaho's Carte server
 */
public class ParamConfig {

    public enum PentahoParamType {
        DATETIME, STRING;
    }

    private String paramName;
    private PentahoParamType type;
    private String value;

    private boolean appendFieldValue;
    private String appendValue; //append a value or even name of another param
    private String timeOffset; //date offset, +, -, hours, days, weeks, years, performed before appends
    public String getParamName() {
        return paramName;
    }
    public void setParamName(String paramName) {
        this.paramName = paramName;
    }
    public PentahoParamType getType() {
        return type;
    }
    public void setType(PentahoParamType type) {
        this.type = type;
    }
    public String getValue() {
        return value;
    }
    public void setValue(String value) {
        this.value = value;
    }
    public boolean isAppendFieldValue() {
        return appendFieldValue;
    }
    public void setAppendFieldValue(boolean appendFieldValue) {
        this.appendFieldValue = appendFieldValue;
    }
    public String getAppendValue() {
        return appendValue;
    }
    public void setAppendValue(String appendValue) {
        this.appendValue = appendValue;
    }
    public String getTimeOffset() {
        return timeOffset;
    }
    public void setTimeOffset(String timeOffset) {
        this.timeOffset = timeOffset;
    }

    

}
