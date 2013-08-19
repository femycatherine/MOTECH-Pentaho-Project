package org.motechproject.reporting.pentaho.domain;

import org.codehaus.jackson.annotate.JsonAutoDetect;
import org.codehaus.jackson.annotate.JsonMethod;
import org.codehaus.jackson.annotate.JsonProperty;

@JsonAutoDetect(JsonMethod.NONE)
public class SettingsDto {

    @JsonProperty
    private PentahoServerSettings serverSettings;

    public SettingsDto() {
        this.serverSettings = new PentahoServerSettings();
    }

    public PentahoServerSettings getServerSettings() {
        return serverSettings;
    }

    public void setServerSettings(PentahoServerSettings serverSettings) {
        this.serverSettings = serverSettings;
    }

    public String getPassword() {
        return serverSettings.getPassword();
    }

    public void setPassword(final String password) {
        serverSettings.setPassword(password);
    }

    public String getCarteUrl() {
        return serverSettings.getCarteUrl();
    }

    public void setCarteUrl(final String carteUrl) {
        serverSettings.setCarteUrl(carteUrl);
    }

    public String getCartePort() {
        return serverSettings.getCartePort();
    }

    public void setCartePort(final String cartePort) {
        serverSettings.setCartePort(cartePort);
    }

    public String getUsername() {
        return serverSettings.getUsername();
    }

    public void setUsername(final String username) {
        serverSettings.setUsername(username);
    }
}
