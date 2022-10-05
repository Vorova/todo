package com.vorova.todo.controller.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.vorova.todo.models.dto.JwtRequestDto;
import com.vorova.todo.webapp.configs.TodoApplication;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK, classes = TodoApplication.class)
@AutoConfigureMockMvc
public class AbstractControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    protected String getUserAccessToken(String email, String password) throws Exception {
        JwtRequestDto user = new JwtRequestDto(email, password);
        String result = mockMvc.perform(
                post("/api/auth/login")
                        .content(objectMapper.writeValueAsString(user))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        return "Bearer " + new JSONObject(result).get("accessToken").toString();
    }

    protected String getUserRefreshToken (String email, String password) throws Exception{
        JwtRequestDto user = new JwtRequestDto(email, password);
        String result = mockMvc.perform(
                        post("/api/auth/login")
                                .content(objectMapper.writeValueAsString(user))
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        return new JSONObject(result).get("refreshToken").toString();
    }

}