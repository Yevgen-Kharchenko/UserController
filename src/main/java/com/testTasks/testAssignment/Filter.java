package com.testTasks.testAssignment;

import lombok.Data;

import java.time.LocalDate;

@Data
public class Filter {

    private final Config config;

    private LocalDate from;
    private LocalDate to;
    private Integer limit;
    private Integer offset;



}
