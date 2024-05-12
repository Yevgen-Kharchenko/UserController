package com.testTasks.testAssignment.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.format.DateTimeParseException;

@ControllerAdvice
public class ApplicationExceptionHandler {

    @ExceptionHandler(UserValidationException.class)
    ResponseEntity<ApiError> validateFields(UserValidationException ex, WebRequest request) {
        String requestURL = getRequestURL(request);
        ApiError.ErrorItem.Links links = new ApiError.ErrorItem.Links(requestURL);
        ApiError.ErrorItem errorItem = new ApiError.ErrorItem(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                235,
                links
        );

        ApiError apiError = new ApiError();
        apiError.addError(errorItem);

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({IllegalArgumentException.class, RuntimeException.class, DateTimeParseException.class})
    public ResponseEntity<ApiError> handleIllegalArgument(Exception ex, WebRequest request) {
        String requestURL = getRequestURL(request);
        ApiError.ErrorItem.Links links = new ApiError.ErrorItem.Links(requestURL);
        ApiError.ErrorItem errorItem = new ApiError.ErrorItem(
                HttpStatus.BAD_REQUEST.value(),
                ex.getMessage(),
                475,
                links
        );

        ApiError apiError = new ApiError();
        apiError.addError(errorItem);

        return new ResponseEntity<>(apiError, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(DataNotFoundException.class)
    ResponseEntity<ApiError> dataNotFoundExceptionHandler(DataNotFoundException ex, WebRequest request) {
        String requestURL = getRequestURL(request);
        ApiError.ErrorItem.Links links = new ApiError.ErrorItem.Links(requestURL);
        ApiError.ErrorItem errorItem = new ApiError.ErrorItem(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                356,
                links
        );

        ApiError apiError = new ApiError();
        apiError.addError(errorItem);

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ConstraintViolationException.class)
    ResponseEntity<ApiError> constraintViolationExceptionHandler(ConstraintViolationException ex, WebRequest request) {
        String requestURL = getRequestURL(request);
        ApiError.ErrorItem.Links links = new ApiError.ErrorItem.Links(requestURL);
        ApiError.ErrorItem errorItem = new ApiError.ErrorItem(
                HttpStatus.NOT_FOUND.value(),
                ex.getMessage(),
                345,
                links
        );

        ApiError apiError = new ApiError();
        apiError.addError(errorItem);

        return new ResponseEntity<>(apiError, HttpStatus.NOT_FOUND);
    }

    private String getRequestURL(WebRequest request) {
        try {
            return new URI(request.getDescription(false)).toString();
        } catch (URISyntaxException e) {
            return "Invalid URL";
        }
    }

}
