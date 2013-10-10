package org.motechproject.reporting.pentaho.util;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpException;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.URI;
import org.apache.commons.httpclient.URIException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import org.apache.commons.io.IOUtils;
import org.motechproject.reporting.pentaho.request.PentahoExecuteTransRequest;
import org.motechproject.reporting.pentaho.request.PentahoRequest;
import org.motechproject.reporting.pentaho.request.PentahoStatusRequest;
import org.motechproject.server.config.SettingsFacade;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
public class PentahoCarteHttpClient {

    private Logger logger = LoggerFactory.getLogger(this.getClass());
    private HttpClient commonsHttpClient;
    private SettingsFacade settingsFacade;
    private static final String KETTLE_PATH = "kettle";
    private static final String UTF_8_ENCODING = "UTF-8";

    @Autowired
    public PentahoCarteHttpClient(final HttpClient commonsHttpClient,
            @Qualifier("pentahoReportingSettings") final SettingsFacade settingsFacade) {
        this.commonsHttpClient = commonsHttpClient;
        this.settingsFacade = settingsFacade;
    }

    public String statusRequest(PentahoStatusRequest statusRequest) {
        return this.getRequest(getStatusUrl(), statusRequest);
    }

    private String getRequest(String requestUrl, PentahoRequest pentahoRequest) {

        HttpMethod getMethod = buildRequest(requestUrl, pentahoRequest);

        try {
            URI uri = getMethod.getURI();
            logger.info("Sending request to Pentaho: " + uri);
            commonsHttpClient.executeMethod(getMethod);
            InputStream responseBodyAsStream = getMethod.getResponseBodyAsStream();

            String getResponse = IOUtils.toString(responseBodyAsStream);
            logger.debug("Response from Pentaho: " + getResponse);
            return getResponse;

        } catch (URIException e) {
            logger.warn("URIException building request for Pentaho: " + e.getMessage());
        } catch (HttpException e) {
            logger.warn("HttpException while sending request to Pentaho: " + e.getMessage());
        } catch (IOException e) {
            logger.warn("IOException while sending request to Pentaho: " + e.getMessage());
        } finally {
            getMethod.releaseConnection();
        }

        return null;
    }

    private HttpMethod buildRequest(String requestUrl, PentahoRequest pentahoRequest) {
        HttpMethod requestMethod = new GetMethod(requestUrl);

        authenticate();

        if (pentahoRequest != null) {
            try {
                String queryString = URLEncoder.encode(pentahoRequest.toQueryString(), UTF_8_ENCODING);
                requestMethod.setQueryString(queryString);
            } catch (UnsupportedEncodingException e) {
                logger.warn("Error encoding request query string: " + e);
            }
        }

        return requestMethod;
    }

    private void authenticate() {
        commonsHttpClient.getParams().setAuthenticationPreemptive(true);

        commonsHttpClient.getState().setCredentials(new AuthScope(null, -1, null, null),
                new UsernamePasswordCredentials(getUsername(), getPassword()));
    }

    private String getPassword() {
        return settingsFacade.getProperty("username");
    }

    private String getUsername() {
        return settingsFacade.getProperty("password");
    }

    String getStatusUrl() {
        return String.format("%s/%s/%s/", getCarteServerBaseUrl(), KETTLE_PATH, "status");
    }

    String getExecuteTransUrl() {
        return String.format("%s/%s/%s/", getCarteServerBaseUrl(), KETTLE_PATH, "executeTrans");
    }

    String getCarteServerBaseUrl() {
        return settingsFacade.getProperty("carteUrl") + ":" + settingsFacade.getProperty("port");
    }

    public void transRequest(PentahoExecuteTransRequest request) {
        ExecuteTransThread transThread = new ExecuteTransThread(request);
        transThread.start();
    }

    private class ExecuteTransThread extends Thread {
        private PentahoExecuteTransRequest request;

        ExecuteTransThread(PentahoExecuteTransRequest request) {
            this.request = request;
        }

        public void run() {
            getRequest(getExecuteTransUrl(), request);
        }
    }
}
