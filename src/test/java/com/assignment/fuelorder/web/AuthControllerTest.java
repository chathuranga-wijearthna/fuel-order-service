package com.assignment.fuelorder.web;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.assignment.fuelorder.dto.AuthDtos;
import com.assignment.fuelorder.dto.AuthDtos.TokenResponse;
import com.assignment.fuelorder.dto.constants.Role;
import com.assignment.fuelorder.service.AuthService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        AuthController controller = new AuthController(authService);

        this.mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
        this.objectMapper = new ObjectMapper();
    }

    @Test
    void register_shouldReturn201_andUserId() throws Exception {
        
        when(authService.register(any(AuthDtos.RegisterRequest.class))).thenReturn(10L);
        AuthDtos.RegisterRequest req = new AuthDtos.RegisterRequest("user@example.com", "secret", Role.AIRCRAFT_OPERATOR);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$", is(10)));
    }

    @Test
    void login_shouldReturnToken() throws Exception {
        
        when(authService.login(any(AuthDtos.LoginRequest.class))).thenReturn(new TokenResponse("jwt"));

        AuthDtos.LoginRequest req = new AuthDtos.LoginRequest("user@example.com", "secret");

        mockMvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token", is("jwt")));
    }

    @Test
    void register_shouldReturn400_onValidationError() throws Exception {

        AuthDtos.RegisterRequest req = new AuthDtos.RegisterRequest("not-an-email", "", null);

        mockMvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Validation failed")));
    }
}
