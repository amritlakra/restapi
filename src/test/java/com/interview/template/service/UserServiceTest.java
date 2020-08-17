package com.interview.template.service;

import com.interview.template.configuration.ApplicationConfiguration;
import com.interview.template.dao.UserDao;
import com.interview.template.exceptions.DuplicateUserException;
import com.interview.template.exceptions.ReservedUsernameException;
import com.interview.template.exceptions.UserNotFoundException;
import com.interview.template.model.UserEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doReturn;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(initializers = ConfigFileApplicationContextInitializer.class)
@EnableConfigurationProperties(value = ApplicationConfiguration.class)
@ActiveProfiles("test")
class UserServiceTest {

	@Mock
	private UserDao userDao;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ApplicationConfiguration applicationConfiguration;

	private UserService userService;

	@BeforeEach
	void beforeEach() {
		userService = new UserService(userDao,applicationConfiguration,passwordEncoder);
	}

	@Test
	void shouldFindUser() throws UserNotFoundException {
		UserEntity user = UserEntity.builder()
				.id(1L)
				.username("john")
				.password("pass")
				.build();
		doReturn(user).when(userDao).findOrDie(1L);

		assertEquals(user, userService.getUser(1L));
	}

	@Test
	void shouldCreateUser() throws DuplicateUserException, ReservedUsernameException {
		UserEntity user = UserEntity.builder()
				.username("john")
				.password("pass")
				.build();

		doAnswer((Answer<UserEntity>) invocation -> {
			UserEntity userEntity = invocation.getArgument(0);
			userEntity.setId(123L);
			return userEntity;
		}).when(userDao).create(user);

		UserEntity createdUserEntity = userService.createUser(user);
		assertNotNull(createdUserEntity);
		assertEquals(createdUserEntity.getId(),123L);
	}

	@Test
	void shouldNotCreateUserWhenReservedUsernameIsUsed() {
		UserEntity user = UserEntity.builder()
				.username("admin")
				.password("pass")
				.build();
		Assertions.assertThrows(ReservedUsernameException.class,()-> userService.createUser(user));
	}
}
