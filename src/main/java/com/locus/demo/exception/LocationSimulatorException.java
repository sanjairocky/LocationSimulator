package com.locus.demo.exception;

import com.locus.demo.codes.LocationSimulatorErrorCodes;

/**
 * @author sanjaikumar.arumugam
 */
public class LocationSimulatorException extends java.lang.Exception {
    private static final long serialVersionUID = -7436056881934111681L;
    private final LocationSimulatorErrorCodes code;

    public LocationSimulatorException(String message, Throwable cause, LocationSimulatorErrorCodes errorCode) {
        super(message, cause);
        this.code = errorCode;
    }

    public LocationSimulatorException(String message, LocationSimulatorErrorCodes errorCode) {
        super(message);
        this.code = errorCode;
    }
    public LocationSimulatorErrorCodes getCode() {
        return this.code;
    }
}
