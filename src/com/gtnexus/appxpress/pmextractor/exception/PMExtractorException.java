package com.gtnexus.appxpress.pmextractor.exception;

/**
 * Created by jjdonov on 9/29/14.
 */
public class PMExtractorException extends Exception {

    private static final long serialVersionUID = 5468857470158908903L;

    public PMExtractorException(String message) {
        super(message);
    }

    public PMExtractorException(Exception e) {
        super(e);
    }

    public PMExtractorException(String message, Exception e) {
        super(message,e);
    }

}
