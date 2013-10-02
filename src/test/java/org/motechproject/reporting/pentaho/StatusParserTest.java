package org.motechproject.reporting.pentaho;

import java.util.List;

import org.junit.Test;
import org.motechproject.reporting.pentaho.exception.StatusParserException;
import org.motechproject.reporting.pentaho.parser.StatusParser;
import org.motechproject.reporting.pentaho.status.ServerStatus;
import org.motechproject.reporting.pentaho.status.TransStatus;

import junit.framework.TestCase;

public class StatusParserTest extends TestCase {

    @Test
    public void testShouldParseStatusCorrectly() throws StatusParserException {
        StatusParser parser = new StatusParser(statusXml());

        ServerStatus status = parser.parse();

        assertEquals("Online", status.getStatusDescription());

        assertEquals(2, status.getTransStatusList().size());

        List<TransStatus> transStatusList = status.getTransStatusList();

        TransStatus firstStatus = transStatusList.get(0);

        assertEquals("Row generator test", firstStatus.getTransName());
        assertEquals("c90edf4b-4d21-4797-afd7-d0207fe46f81", firstStatus.getId());
        assertEquals(false, firstStatus.isPaused());
        assertEquals("", firstStatus.getErrorDescription());
        assertEquals("Waiting", firstStatus.getStatusDescription());

        TransStatus secondStatus = transStatusList.get(1);
        
        assertEquals("dailyReport", secondStatus.getTransName());
        assertEquals("dd116ea8-e3c0-4dd4-812e-a6717b34543f", secondStatus.getId());
        assertEquals(true, secondStatus.isPaused());
        assertEquals("", secondStatus.getErrorDescription());
        assertEquals("Paused", secondStatus.getStatusDescription());
    }

    private String statusXml() {
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n<serverstatus>\n<statusdesc>Online</statusdesc>\n  <transstatuslist>\n    <transstatus>\n<transname>Row generator test</transname>\n<id>c90edf4b-4d21-4797-afd7-d0207fe46f81</id>\n<status_desc>Waiting</status_desc>\n<error_desc/>\n<paused>N</paused>\n  <stepstatuslist>\n  </stepstatuslist>\n<first_log_line_nr>0</first_log_line_nr>\n<last_log_line_nr>0</last_log_line_nr>\n<logging_string><![CDATA[]]></logging_string>\n</transstatus>\n    <transstatus>\n<transname>dailyReport</transname>\n<id>dd116ea8-e3c0-4dd4-812e-a6717b34543f</id>\n<status_desc>Paused</status_desc>\n<error_desc/>\n<paused>Y</paused>\n  <stepstatuslist>\n  </stepstatuslist>\n<first_log_line_nr>0</first_log_line_nr>\n<last_log_line_nr>0</last_log_line_nr>\n<logging_string><![CDATA[]]></logging_string>\n</transstatus>\n  </transstatuslist>\n  <jobstatuslist>\n  </jobstatuslist>\n</serverstatus>\n";
    }
}
