package com.nava.service.cooperfilme.common;

import com.nava.service.cooperfilme.common.exceptions.BadRequestException;
import com.nava.service.cooperfilme.common.exceptions.FlowException;
import com.nava.service.cooperfilme.common.exceptions.NotFoundException;
import com.nava.service.cooperfilme.common.exceptions.UnauthorizedException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request
    ) {

        Map<String, Object> body = new HashMap<>();
        body.put("message", "Bad request");
        body.put("status", status.value());

        Map<String, String> errors = ex.getBindingResult().getFieldErrors().stream().collect(Collectors.toMap(
                FieldError::getField,
                FieldError::getDefaultMessage,
                (msg1, msg2) -> msg1
        ));
        body.put("errors", errors);
        return ResponseEntity
                .status(status.value())
                .body(body);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<Object> handleNotFoundException(NotFoundException ex, WebRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatusCode.valueOf(HttpStatus.NOT_FOUND.value()))
                .body(map);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<Object> handleUnauthorizedException(UnauthorizedException ex, WebRequest request) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatusCode.valueOf(HttpStatus.UNAUTHORIZED.value()))
                .body(map);
    }

    @ExceptionHandler(FlowException.class)
    public ResponseEntity<Object> handleUnauthorizedException(
            FlowException ex,
            WebRequest request
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatusCode.valueOf(HttpStatus.CONFLICT.value()))
                .body(map);
    }

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<Object> handleUnauthorizedException(
            BadRequestException ex,
            WebRequest request
    ) {
        Map<String, Object> map = new HashMap<>();
        map.put("message", ex.getMessage());
        return ResponseEntity
                .status(HttpStatusCode.valueOf(HttpStatus.BAD_REQUEST.value()))
                .body(map);
    }
}
