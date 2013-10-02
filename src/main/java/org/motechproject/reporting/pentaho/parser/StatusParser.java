package org.motechproject.reporting.pentaho.parser;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;
import org.apache.xerces.parsers.DOMParser;
import org.motechproject.reporting.pentaho.exception.StatusParserException;
import org.motechproject.reporting.pentaho.status.ServerStatus;
import org.motechproject.reporting.pentaho.status.TransStatus;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class StatusParser {

    public static final String SERVER_STATUS_ELEMENT = "serverstatus";
    public static final String SERVER_DESCRIPTION_ELEMENT = "statusdesc";
    public static final String STATUS_LIST_ELEMENT = "transstatuslist";
    public static final String TRANS_STATUS_ELEMENT = "transstatus";
    public static final String TRANSNAME_ELEMENT = "transname";
    public static final String ID_ELEMENT = "id";
    public static final String ERROR_DESC_ELEMENT = "error_desc";
    public static final String STATUS_DESC_ELEMENT = "status_desc";
    public static final String PAUSED_ELEMENT = "paused";

    private String xmlDoc;

    public StatusParser(String xmlDoc) {
        this.xmlDoc = xmlDoc;
    }

    public ServerStatus parse() throws StatusParserException {
        DOMParser parser = new DOMParser();

        InputSource inputSource = new InputSource();
        inputSource.setCharacterStream(new StringReader(xmlDoc));

        ServerStatus status = new ServerStatus();

        try {
            parser.parse(inputSource);

            Document document = parser.getDocument();

            Node statusDescription = document.getElementsByTagName(SERVER_DESCRIPTION_ELEMENT).item(0);

            List<TransStatus> transStatusList = parseStatusList(document.getElementsByTagName(STATUS_LIST_ELEMENT).item(0));

            status.setStatusDescription(statusDescription.getTextContent());
            status.setTransStatusList(transStatusList);

        } catch (SAXException | IOException | NullPointerException ex) {
            throw new StatusParserException(ex, "Exception while trying to parse statusXml: " + xmlDoc);
        }

        return status;
    }

    private List<TransStatus> parseStatusList(Node item) {
        List<TransStatus> transStatusList = new ArrayList<TransStatus>();

        NodeList children = item.getChildNodes();

        for (int i = 0; i < children.getLength(); i++) {
            if (children.item(i).getNodeName().equals(TRANS_STATUS_ELEMENT)) {
                TransStatus status = buildTransStatus(children.item(i).getChildNodes());
                transStatusList.add(status);
            }
        }

        return transStatusList;
    }

    private TransStatus buildTransStatus(NodeList childNodes) {
        TransStatus status = new TransStatus();

        for (int i = 0; i < childNodes.getLength(); i++) {
            String nodeName = childNodes.item(i).getNodeName();
            String nodeValue = childNodes.item(i).getTextContent();

            switch (nodeName) {
                case TRANSNAME_ELEMENT: status.setTransName(nodeValue); break;
                case ID_ELEMENT: status.setId(nodeValue); break;
                case ERROR_DESC_ELEMENT: status.setErrorDescription(nodeValue); break;
                case STATUS_DESC_ELEMENT: status.setStatusDescription(nodeValue); break;
                case PAUSED_ELEMENT: status.setPaused(parseBoolean(nodeValue)); break;
                default: break;
            }
        }

        return status;
    }

    private boolean parseBoolean(String nodeValue) {
        if ("Y".equals(nodeValue)) {
            return true;
        } 

        return false;
    }
}
