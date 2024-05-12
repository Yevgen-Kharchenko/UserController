package com.testTasks.testAssignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "com.testTasks.testAssignment")
//@EnableConfigurationProperties(Config.class)
public class UserControllerApplication {

    public static void main(String[] args) {
        SpringApplication.run(UserControllerApplication.class, args);
    }

}
