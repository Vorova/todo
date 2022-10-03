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
    public void add_user() throws Exception {
        final String API = "/api/general/add_user";

        UserRegDto userCorrect = new UserRegDto("email@mail.ru", "password");
        UserRegDto userInCorrect = new UserRegDto("email.ru", "pass");

        // check create
        mockMvc.perform(
                        post(API)
                                .content(objectMapper.writeValueAsString(userCorrect))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(201));

        // check unique email
        mockMvc.perform(
                        post(API)
                                .content(objectMapper.writeValueAsString(userCorrect))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$.[0].type", is(1)));

        // check valid data
        mockMvc.perform(
                post(API)
                        .content(objectMapper.writeValueAsString(userInCorrect))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(400))
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$.[0].type", is(2)))
                .andExpect(jsonPath("$.[1].type", is(3)));
    }

}
