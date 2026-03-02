package com.bankapp.messagerouter.config;

import com.bankapp.messagerouter.error.PartnerNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.io.EOFException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(EOFException.class)
    public ResponseEntity<String> handleEOFException(EOFException e) {
        log.warn("Client disconnected prematurely: {}", e.getMessage());
        return new ResponseEntity<>("Client disconnected prematurely", HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PartnerNotFoundException.class)
    public ResponseEntity<String> handlePartnerNotFound(PartnerNotFoundException ex) {
        log.warn("Partner not found: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleGeneralException(Exception ex) {
        log.error("An error occurred: {}", ex.getMessage(), ex);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}