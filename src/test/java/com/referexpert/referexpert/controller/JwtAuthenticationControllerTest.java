package com.referexpert.referexpert.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.referexpert.referexpert.beans.JwtRequest;
import com.referexpert.referexpert.beans.RefreshToken;
import com.referexpert.referexpert.beans.TokenRefreshRequest;
import com.referexpert.referexpert.beans.UserRegistration;
import com.referexpert.referexpert.security.JwtTokenUtil;
import com.referexpert.referexpert.service.JwtUserDetailsService;
import com.referexpert.referexpert.service.MySQLService;
import com.referexpert.referexpert.service.impl.RefreshTokenService;

@ExtendWith(MockitoExtension.class)
class JwtAuthenticationControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenUtil jwtTokenUtil;

    @Mock
    private JwtUserDetailsService userDetailsService;

    @Mock
    private RefreshTokenService refreshTokenService;

    @Mock
    private MySQLService mySQLService;

    @InjectMocks
    private JwtAuthenticationController controller;

    private ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .build();
    }    @Test
    void whenValidCredentials_thenAuthenticateSuccessfully() throws Exception {
        JwtRequest request = new JwtRequest("test@example.com", "password");
        UserDetails userDetails = new User("test@example.com", "password", new ArrayList<>());
        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setUserId("123");
        userRegistration.setEmail("test@example.com");

        RefreshToken refreshToken = new RefreshToken();
        refreshToken.setToken("refresh-token");

        when(userDetailsService.loadUserByUsername("test@example.com")).thenReturn(userDetails);
        when(jwtTokenUtil.generateToken(userDetails)).thenReturn("jwt-token");
        when(mySQLService.selectUser(any())).thenReturn(userRegistration);
        when(refreshTokenService.createRefreshToken("123")).thenReturn(refreshToken);

        mockMvc.perform(post("/validateuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("jwt-token"))
                .andExpect(jsonPath("$.refreshToken").value("refresh-token"));
    }

    @Test
    void whenInvalidCredentials_thenReturn401() throws Exception {
        JwtRequest request = new JwtRequest("test@example.com", "wrongpassword");

        doThrow(new BadCredentialsException("Invalid credentials"))
            .when(authenticationManager)
            .authenticate(any());

        mockMvc.perform(post("/validateuser")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void whenRefreshToken_thenIssueNewAccessToken() throws Exception {
        TokenRefreshRequest refreshRequest = new TokenRefreshRequest();
        refreshRequest.setRefreshToken("valid-refresh-token");

        RefreshToken storedToken = new RefreshToken();
        storedToken.setUserId("123");

        UserRegistration userRegistration = new UserRegistration();
        userRegistration.setEmail("test@example.com");

        when(refreshTokenService.findByToken("valid-refresh-token")).thenReturn(storedToken);
        when(refreshTokenService.verifyExpiration(storedToken)).thenReturn(storedToken);
        when(mySQLService.selectUser(any())).thenReturn(userRegistration);
        when(jwtTokenUtil.generateTokenFromUsername("test@example.com")).thenReturn("new-jwt-token");

        mockMvc.perform(post("/refreshtoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").value("new-jwt-token"))
                .andExpect(jsonPath("$.refreshToken").value("valid-refresh-token"));
    }

    @Test
    void whenExpiredRefreshToken_thenReturn403() throws Exception {
        TokenRefreshRequest refreshRequest = new TokenRefreshRequest();
        refreshRequest.setRefreshToken("expired-token");

        RefreshToken storedToken = new RefreshToken();
        storedToken.setUserId("123");

        when(refreshTokenService.findByToken("expired-token")).thenReturn(storedToken);
        when(refreshTokenService.verifyExpiration(storedToken))
            .thenThrow(new TokenRefreshException(storedToken.getToken(), "Refresh token was expired"));

        mockMvc.perform(post("/refreshtoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenInvalidRefreshToken_thenReturn403() throws Exception {
        TokenRefreshRequest refreshRequest = new TokenRefreshRequest();
        refreshRequest.setRefreshToken("invalid-token");

        when(refreshTokenService.findByToken("invalid-token"))
            .thenThrow(new TokenRefreshException("invalid-token", "Refresh token is not in database!"));

        mockMvc.perform(post("/refreshtoken")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(refreshRequest)))
                .andExpect(status().isForbidden());
    }

    @Test
    void whenLogout_thenDeleteRefreshToken() throws Exception {
        TokenRefreshRequest request = new TokenRefreshRequest();
        request.setRefreshToken("token-to-delete");

        when(refreshTokenService.deleteByToken("token-to-delete")).thenReturn(1);

        mockMvc.perform(post("/logout")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk());
    }
}
