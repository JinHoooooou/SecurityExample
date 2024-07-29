package com.example.securityexample.global.handler;

import com.example.securityexample.global.exception.DuplicateResourceException;
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

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e) {
    Map<String, String> details = new HashMap<>();
    for (FieldError fieldError : e.getBindingResult().getFieldErrors()) {
      details.putIfAbsent(fieldError.getField(), fieldError.getDefaultMessage());
    }

    return ResponseEntity
        .badRequest()
        .body(details);
  }

  @ExceptionHandler(DuplicateResourceException.class)
  public ResponseEntity<String> handleDuplicateResource(DuplicateResourceException e) {
    return ResponseEntity
        .status(HttpStatus.CONFLICT)
        .body(e.getLocalizedMessage());
  }
}
