package com.assignment.fuelorder.web;

import com.assignment.fuelorder.exception.CustomGlobalException;
import com.assignment.fuelorder.exception.ErrorCode;
import java.util.HashMap;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(CustomGlobalException.class)
    public ResponseEntity<?> handleCustomGlobalException(CustomGlobalException ex) {
        HttpStatus status = mapStatus(ex.getCode());
        return ResponseEntity.status(status).body(Map.of(
                "code", ex.getCode(),
                "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> handleValidation(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        for (FieldError fe : ex.getBindingResult().getFieldErrors()) {
            errors.put(fe.getField(), fe.getDefaultMessage());
        }
        return ResponseEntity.badRequest().body(Map.of("message", "Validation failed", "errors", errors));
    }

    private HttpStatus mapStatus(String code) {
        if (ErrorCode.ORDER_NOT_FOUND.getCode().equals(code)) {
            return HttpStatus.NOT_FOUND;
        }
        if (ErrorCode.ILLEGAL_STATUS_TRANSITION.getCode().equals(code) ||
            ErrorCode.DELIVERY_WINDOW_OVERLAP.getCode().equals(code)) {
            return HttpStatus.CONFLICT;
        }
        // Default to BAD_REQUEST for input-related errors
        return HttpStatus.BAD_REQUEST;
    }
}
