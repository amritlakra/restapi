package com.interview.template.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.interview.template.dao.UserDao;
import com.interview.template.exceptions.DuplicateUserException;
import com.interview.template.exceptions.ReservedUsernameException;
import com.interview.template.exceptions.UserNotFoundException;
import com.interview.template.model.UserEntity;
import com.interview.template.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.stubbing.Answer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@WebMvcTest(controllers = UserController.class)
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserDao userDao;

    @MockBean
    private UserService userService;

    @Test
    void shouldCreateUser() throws Exception {

        UserEntity user = UserEntity.builder()
                .username("john")
                .password("pass")
                .email("john@gmail.com")
                .build();

        doAnswer((Answer<UserEntity>) invocation -> {
            UserEntity userEntity = invocation.getArgument(0);
            userEntity.setId(123L);
            return userEntity;
        }).when(userService).createUser(user);

        mockMvc.perform(post("/api/v1/users")
                .content(asJsonString(user))
                .contentType("application/json"))
                .andExpect(status().isCreated());

    }

    @Test
    void shouldProvide409ForDuplicateUserExceptionWhenCreateUser() throws Exception {

        UserEntity user = UserEntity.builder()
                .username("john")
                .password("pass")
                .email("john@gmail.com")
                .build();

        doThrow(DuplicateUserException.class).when(userService).createUser(user);

        mockMvc.perform(post("/api/v1/users")
                .content(asJsonString(user))
                .contentType("application/json"))
                .andExpect(status().isConflict());

    }

    @Test
    void shouldProvide400ForReservedUsernameExceptionWhenCreateUser() throws Exception {

        UserEntity user = UserEntity.builder()
                .username("john")
                .password("pass")
                .email("john@gmail.com")
                .build();

        doThrow(ReservedUsernameException.class).when(userService).createUser(user);

        mockMvc.perform(post("/api/v1/users")
                .content(asJsonString(user))
                .contentType("application/json"))
                .andExpect(status().isBadRequest());

    }

    @Test
    void shouldProvideValidationErrorForInvalidInputWhenCreateUser() throws Exception {

        UserEntity user = UserEntity.builder()
                .username("")
                .password("pass")
                .email("john@gmail.com")
                .build();

        doThrow(ReservedUsernameException.class).when(userService).createUser(user);

        mockMvc.perform(post("/api/v1/users")
                .content(asJsonString(user))
                .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string(containsString("username: username cannot be null or empty")));

    }

    public static String asJsonString(final Object obj) {
        try {
            final ObjectMapper mapper = new ObjectMapper();
            final String jsonContent = mapper.writeValueAsString(obj);
            System.out.println(jsonContent);
            return jsonContent;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
