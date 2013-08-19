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


}
