package org.motechproject.reporting.pentaho.exception;

public class PentahoJobException extends Exception {

    public PentahoJobException(String message) {
        super(message);
    }
    
    public PentahoJobException(Exception ex, String message) {
        super(message, ex);
    }
}
