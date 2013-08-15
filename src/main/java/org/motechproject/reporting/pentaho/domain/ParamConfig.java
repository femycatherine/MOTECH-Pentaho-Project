package org.motechproject.reporting.pentaho.domain;

import java.io.Serializable;

public class ParamConfig<T extends Serializable> {

    private String paramName;
    private String type;
    private T value;
    private boolean appendFieldValue;
    private String appendValue; //append a value or even name of another param
    private String operation; //date offset, +, -, hours, days, weeks, years, performed before appends
}
