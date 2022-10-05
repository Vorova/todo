package com.vorova.todo.controller.rest;

import com.vorova.todo.models.dto.RefreshJwtRequestDto;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class TestAuthController extends AbstractControllerTest {

    @Test
    public void login() throws Exception {
        String accessTokenValid = getUserAccessToken("email1@mail.ru", "password1");
        mockMvc.perform(
                        post("/api/general/check_auth")
                                .header("Authorization", accessTokenValid))
                .andExpect(status().is(200));
    }

    @Test
    public void token() throws Exception {
        String refreshTokenValid = getUserRefreshToken("email1@mail.ru", "password1");
        RefreshJwtRequestDto jwtRequestDto = new RefreshJwtRequestDto();
        jwtRequestDto.setRefreshToken(refreshTokenValid);

        String result = mockMvc.perform(
                        post("/api/auth/token")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jwtRequestDto)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        JSONObject jsonObject = new JSONObject(result);
        String accessTokenValid = "Bearer " + jsonObject.get("accessToken").toString();

        mockMvc.perform(
                        post("/api/general/check_auth")
                                .header("Authorization", accessTokenValid))
                .andExpect(status().is(200));
    }

    @Test
    public void refresh() throws Exception {
        String refreshTokenValid = getUserRefreshToken("email1@mail.ru", "password1");
        RefreshJwtRequestDto jwtRequestDto = new RefreshJwtRequestDto();
        jwtRequestDto.setRefreshToken(refreshTokenValid);

        String result = mockMvc.perform(
                        post("/api/auth/refresh")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(jwtRequestDto)))
                .andExpect(status().is(200))
                .andReturn().getResponse().getContentAsString();

        JSONObject jsonObject = new JSONObject(result);
        String accessTokenValid = "Bearer " + jsonObject.get("accessToken").toString();

        mockMvc.perform(
                        post("/api/general/check_auth")
                                .header("Authorization", accessTokenValid))
                .andExpect(status().is(200));
    }
}
