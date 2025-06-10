package com.sh32bit.exception;

import com.sh32bit.dto.response.ApiResponse;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleValidationErrors(MethodArgumentNotValidException ex) {
        List<String> errors = ex.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.toList());
        log.warn("Validation error: {}", errors);
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Validation failure",
                errors, LocalDateTime.now()));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleConstraintViolations(ConstraintViolationException ex) {
        List<String> errors = ex.getConstraintViolations().stream()
                .map(violation -> violation.getPropertyPath() + ": " + violation.getMessage())
                .toList();
        log.warn("Validation error: {}", errors);
        return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, "Validation failure", errors, LocalDateTime.now())
        );
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleHandlerMethodValidation(HandlerMethodValidationException ex) {
        List<String> errors = ex.getAllErrors().stream()
                .map(error -> {
                    if (error instanceof org.springframework.validation.FieldError fieldError) {
                        return fieldError.getField() + ": " + fieldError.getDefaultMessage();
                    }
                    return error.getDefaultMessage();
                })
                .toList();
        log.warn("Validation error: {}", errors);
        return ResponseEntity.badRequest().body(new ApiResponse<>(false, "Validation failure",
                errors, LocalDateTime.now()));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Void>> handleNotFound(NotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ApiResponse<>(false, ex.getMessage(), null, LocalDateTime.now()));
    }

    @ExceptionHandler(value = {ConflictException.class})
    public ResponseEntity<ApiResponse<Void>> handleConflict(ConflictException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(false, ex.getMessage(), null, LocalDateTime.now()));
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ApiResponse<Void>> handleEmailExists(EmailAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ApiResponse<>(false, ex.getMessage(), null, LocalDateTime.now()));
    }

    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<Void>> handleForbidden(ForbiddenException ex) {
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ApiResponse<>(false, ex.getMessage(), null, LocalDateTime.now()));
    }

    @ExceptionHandler(value = {AccessDeniedException.class})
    public ResponseEntity<ApiResponse<Void>> handleAccessDeniedException(Exception ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false,
                        "Unauthorized: Access is denied", null, LocalDateTime.now()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiResponse<Void>> handleBadCredentials(BadCredentialsException ex) {
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .body(new ApiResponse<>(false,
                        "Invalid email or password", null, LocalDateTime.now()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Void>> handleGeneralErrors(Exception ex) {
        log.error("Unexpected error:", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ApiResponse<>(false, "Internal Server Error: " + ex.getMessage(),
                        null, LocalDateTime.now()));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        List<String> errors = new ArrayList<>();
        if (ex.getMessage() != null && ex.getMessage().contains("com.sh32bit.enums.Role")) {
            errors.add("Role must be STUDENT or TEACHER");
        } else {
            errors.add("Invalid request body");
        }
        return ResponseEntity.badRequest().body(
                new ApiResponse<>(false, "Validation failure", errors, LocalDateTime.now())
        );
    }

    private String extractEnumErrorMessage(String message) {
        if (message != null && message.contains("from String")) {
            int idx = message.indexOf("from String");
            return message.substring(idx);
        }
        return "Invalid request body";
    }
}
