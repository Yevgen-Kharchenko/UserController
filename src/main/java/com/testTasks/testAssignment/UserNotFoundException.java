package com.testTasks.testAssignment;


public class UserNotFoundException extends RuntimeException  {
    private Long id;

    public UserNotFoundException(Long id) {
        super("Could not find User " + id);
    }
}
