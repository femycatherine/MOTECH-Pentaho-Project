package org.motechproject.reporting.pentaho.request;

public abstract class PentahoRequest {

    private String xml = "y";

    public abstract String toQueryString();

    protected String concat(String key, Object value) {
        return String.format("%s=%s", key, value.toString());
    }

    public String getXml() {
        return xml;
    }

    public void setXml(String xml) {
        this.xml = "y";
    }
}
