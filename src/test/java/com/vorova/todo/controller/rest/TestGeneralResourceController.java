package com.vorova.todo.controller.rest;

import com.vorova.todo.models.dto.UserRegDto;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class TestGeneralResourceController extends AbstractControllerTest {

    @Test
    public void checkAuth() throws Exception {
        String accessTokenValid = getUserAccessToken("email1@mail.ru", "password1");
        mockMvc.perform(
                        post("/api/general/check_auth")
                                .header("Authorization", accessTokenValid))
                .andExpect(status().is(200));

        mockMvc.perform(post("/api/general/check_auth"))
                .andExpect(status().is(403));
    }

    @Test
    public void addUser() throws Exception {
        final String API = "/api/general/add_user";

        UserRegDto userCorrect = new UserRegDto("email@mail.ru", "password");
        UserRegDto userInCorrect = new UserRegDto("email.ru", "pass");

        // check create
        {mockMvc.perform(
                        post(API)
                                .content(objectMapper.writeValueAsString(userCorrect))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));
        }

        // check unique email
        {mockMvc.perform(
                        post(API)
                                .content(objectMapper.writeValueAsString(userCorrect))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].type", is(1)));
        }

        // check valid data
        {mockMvc.perform(
                        post(API)
                                .content(objectMapper.writeValueAsString(userInCorrect))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].type", is(2)))
                .andExpect(jsonPath("$.[1].type", is(3)));
        }

        // check request with valid JWT
        {
            String accessTokenValid = getUserAccessToken("email1@mail.ru", "password1");
            mockMvc.perform(
                          post("/api/general/add_user")
                            .header("Authorization", accessTokenValid)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(new UserRegDto(
                                    "newEmail@mail.ru",
                                    "password"))))
                    .andExpect(status().is(403));
        }
    }

}
