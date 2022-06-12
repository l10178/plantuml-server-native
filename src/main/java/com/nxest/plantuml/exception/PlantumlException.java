package com.nxest.plantuml.exception;

public class PlantumlException extends RuntimeException {

    public PlantumlException() {
        super();
    }

    public PlantumlException(String message) {
        super(message);
    }

    public PlantumlException(String message, Throwable cause) {
        super(message, cause);
    }

    public PlantumlException(Throwable cause) {
        super(cause);
    }

}
