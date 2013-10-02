package org.motechproject.reporting.pentaho.exception;

public class StatusParserException extends Exception {

    public StatusParserException(String message) {
        super(message);
    }

    public StatusParserException(Exception ex, String message) {
        super(message, ex);
    }
}
