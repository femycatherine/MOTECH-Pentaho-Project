package org.motechproject.reporting.pentaho.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;
import static org.mockito.MockitoAnnotations.initMocks;
import org.apache.commons.httpclient.HttpClient;
import org.hamcrest.core.IsEqual;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.motechproject.server.config.SettingsFacade;
import static org.junit.Assert.assertThat;

public class PentahoCarteHttpClientTest {

    @Mock
    private HttpClient httpClient;

    @Mock
    private SettingsFacade settingsFacade;

    private PentahoCarteHttpClient pentahoClient;

    private static final String BASE_URL = "http://localhost";
    private static final String PORT = "10000";
    private static final String KETTLE = "kettle";
    private static final String EXECUTE_TRANS = "executeTrans";
    private static final String TRANS_STATUS = "status";

    @Before
    public void setUp() {
        initMocks(this);
        when(settingsFacade.getProperty("carteUrl")).thenReturn(BASE_URL);
        when(settingsFacade.getProperty("port")).thenReturn(PORT);

        pentahoClient = new PentahoCarteHttpClient(httpClient, settingsFacade);
    }
    
    @Test
    public void shouldConstructExecuteTransUrl() {
        String url = pentahoClient.getExecuteTransUrl();
        assertThat(url, IsEqual.equalTo(String.format("%s:%s/%s/%s/", BASE_URL, PORT, KETTLE, EXECUTE_TRANS)));
    }
    
    @Test
    public void shouldConstructStatusUrl() {
        String url = pentahoClient.getStatusUrl();
        assertThat(url, IsEqual.equalTo(String.format("%s:%s/%s/%s/", BASE_URL, PORT, KETTLE, TRANS_STATUS)));
    }
}
