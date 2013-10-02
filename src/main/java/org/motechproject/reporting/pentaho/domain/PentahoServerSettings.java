package org.motechproject.reporting.pentaho.domain;

import org.apache.commons.lang.StringUtils;
import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(JsonMethod.NONE)
public class PentahoServerSettings {


    @JsonProperty
    private String username;
    @JsonProperty
    private String password;
    @JsonProperty
    private String carteUrl;
    @JsonProperty
    private String cartePort;
    @JsonProperty
    private String luceneDates;

    public String getUsername() {
        return username != null ? username : StringUtils.EMPTY;
    }

    public void setUsername(final String username) {
        this.username = username;
    }

    public String getPassword() {
        return password != null ? password : StringUtils.EMPTY;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public String getCarteUrl() {
        return carteUrl != null ? carteUrl : StringUtils.EMPTY;
    }

    public void setCarteUrl(final String carteUrl) {
        this.carteUrl = carteUrl;
    }

    public String getCartePort() {
        return cartePort != null ? cartePort : StringUtils.EMPTY;
    }

    public void setCartePort(final String cartePort) {
        this.cartePort = cartePort;
    }

    public String getLuceneDates() {
        return luceneDates;
    }

    public void setLuceneDates(String luceneDates) {
        this.luceneDates = luceneDates;
    }
}
