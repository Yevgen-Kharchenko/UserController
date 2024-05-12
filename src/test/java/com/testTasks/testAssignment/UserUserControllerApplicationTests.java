package com.testTasks.testAssignment;

import com.testTasks.testAssignment.config.ApplicationProperties;
import com.testTasks.testAssignment.exception.ApplicationExceptionHandler;
import com.testTasks.testAssignment.mapper.UserMapperImpl;
import com.testTasks.testAssignment.repo.UserRepository;
import com.testTasks.testAssignment.rest.UserController;
import com.testTasks.testAssignment.service.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserUserControllerApplicationTests {

	@Autowired
	private ApplicationContext context;
	@Test
	void contextLoads() {
		assertThat(context).isNotNull();
		assertThat(context.getBean(UserController.class)).isNotNull();
		assertThat(context.getBean(UserService.class)).isNotNull();
		assertThat(context.getBean(UserRepository.class)).isNotNull();
		assertThat(context.getBean(ApplicationProperties.class)).isNotNull();
		assertThat(context.getBean(ApplicationExceptionHandler.class)).isNotNull();
		assertThat(context.getBean(UserMapperImpl.class)).isNotNull();
	}

}
