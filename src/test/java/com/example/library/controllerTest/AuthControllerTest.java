package com.example.library.controllerTest;

import com.example.library.controller.AuthController;
import com.example.library.model.Role;
import com.example.library.model.User;
import com.example.library.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.SpringBootDependencyInjectionTestExecutionListener;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.ServletTestExecutionListener;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import reactor.core.publisher.Mono;

import java.util.Set;


@ExtendWith(SpringExtension.class)
@WebFluxTest(AuthController.class)
public class AuthControllerTest {

    @MockBean
    private UserService userService;

    @Autowired
    private WebTestClient webTestClient;

    @Test
    public void registerUserTest() throws Exception {

        var user = User.builder()
                .username("user")
                .password("123")
                .passwordConfirmation("123")
                .roles(Set.of(Role.ADMIN))
                .enabled(true)
                .build();

       var userFromDB = User.builder()
                .id("a#2a")
                .username("user")
                .password("123")
                .passwordConfirmation("123")
                .roles(Set.of(Role.ADMIN))
                .enabled(true)
                .build();


        System.out.println(userService.registerNewUser(user));
        Mockito
                .when(userService.registerNewUser(user))
                .thenReturn(Mono.just(userFromDB));

        webTestClient.post()
                .uri("/register")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .body(Mono.just(userFromDB), User.class)
                .exchange()
                .expectStatus().isCreated();

    }

    private static String toJson(User user) {
        try {
            return new ObjectMapper().writeValueAsString(user);
        } catch (Exception e) {
            throw new IllegalStateException("json exception");
        }
    }
}
