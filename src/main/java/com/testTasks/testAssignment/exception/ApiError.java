package com.testTasks.testAssignment.exception;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ApiError {
    private List<ErrorItem> errors = new ArrayList<>();

    public void addError(ErrorItem error) {
        this.errors.add(error);
    }

    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class ErrorItem {
        private int status;
        private String detail;
        private int code;
        private Links links;

        @Data
        @NoArgsConstructor
        @AllArgsConstructor
        public static class Links {
            private String about;
        }
    }
}
