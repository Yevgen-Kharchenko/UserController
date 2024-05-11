package com.testTasks.testAssignment;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication(scanBasePackages = "com.testTasks.testAssignment")
//@EnableConfigurationProperties(Config.class)
public class UserControllerApplication {

	public static void main(String[] args) {
		SpringApplication.run(UserControllerApplication.class, args);
	}

}
