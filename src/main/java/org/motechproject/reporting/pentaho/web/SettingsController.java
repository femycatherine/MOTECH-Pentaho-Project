package org.motechproject.reporting.pentaho.web;

import org.motechproject.reporting.pentaho.domain.SettingsDto;
import org.motechproject.server.config.SettingsFacade;
import org.osgi.framework.BundleException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import static org.apache.commons.lang.StringUtils.isNotBlank;

@Controller
public class SettingsController {

    public static final String CARTE_URL_KEY = "carteUrl";
    public static final String CARTE_PORT_KEY = "port";
    public static final String USERNAME_KEY = "username";
    public static final String PASSWORD_KEY = "password";
    public static final String LUCENE_KEY = "lucene";

    private SettingsFacade settingsFacade;

    @Autowired
    public SettingsController(@Qualifier("pentahoReportingSettings") final SettingsFacade settingsFacade) {
        this.settingsFacade = settingsFacade;
    }

    @RequestMapping(value = "/settings", method = RequestMethod.GET)
    @ResponseBody
    public SettingsDto getSettings() {
        SettingsDto dto = new SettingsDto();
        dto.setCarteUrl(getPropertyValue(CARTE_URL_KEY));
        dto.setCartePort(getPropertyValue(CARTE_PORT_KEY));
        dto.setUsername(getPropertyValue(USERNAME_KEY));
        dto.setPassword(getPropertyValue(PASSWORD_KEY));
        dto.setLuceneDates(getPropertyValue(LUCENE_KEY));
        return dto;
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/settings", method = RequestMethod.POST)
    public void saveSettings(@RequestBody SettingsDto settings) throws BundleException {
        settingsFacade.setProperty(CARTE_URL_KEY, settings.getCarteUrl());
        settingsFacade.setProperty(CARTE_PORT_KEY, settings.getCartePort());
        settingsFacade.setProperty(USERNAME_KEY, settings.getUsername());
        settingsFacade.setProperty(PASSWORD_KEY, settings.getPassword());
        settingsFacade.setProperty(LUCENE_KEY, settings.getLuceneDates());
    }

    private String getPropertyValue(final String propertyKey) {
        String propertyValue = settingsFacade.getProperty(propertyKey);
        return isNotBlank(propertyValue) ? propertyValue : null;
    }

}
