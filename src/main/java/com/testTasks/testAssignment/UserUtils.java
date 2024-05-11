package com.testTasks.testAssignment;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class UserUtils {
    public static <T> ResponseEntity<T> getResponseEntity(T response) {
        HttpStatus status = response == null ? HttpStatus.NO_CONTENT : HttpStatus.OK;
        return new ResponseEntity<>(response, status);
    }
}
