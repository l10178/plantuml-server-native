package com.nxest.plantuml.config;


import com.nxest.plantuml.exception.PlantumlException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerAdviceHandler {

    private static final Logger log = LoggerFactory.getLogger(ControllerAdviceHandler.class);

    @ExceptionHandler(PlantumlException.class)
    public ResponseEntity<String> plantumlException(PlantumlException exception) {
        log.error("Validation exception.", exception);
        return ResponseEntity.badRequest().build();
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handlerError(Exception exception) {
        log.error("Server error.", exception);
        return ResponseEntity.internalServerError().build();
    }
}
