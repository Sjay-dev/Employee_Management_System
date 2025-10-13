package com.Darum.Employee.Management.System.Controller;

import com.Darum.Employee.Management.System.Entites.Enum.Role;
import com.Darum.Employee.Management.System.Entites.User;
import com.Darum.Employee.Management.System.Repository.UserRepository;
import com.Darum.Employee.Management.System.Security.JwtToken;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;


import java.util.Optional;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtToken jwtToken;

    @InjectMocks
    private AuthController authController;

    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testLoginSuccess() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        User user = new User() {{
            setEmail("test@example.com");
            setPassword("encodedPass");
            setRole(Role.ADMIN);
        }};

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("rawPass", "encodedPass")).thenReturn(true);
        when(jwtToken.generateToken("test@example.com", "ADMIN")).thenReturn("fake-jwt");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Object() {{
                            put("email", "test@example.com");
                            put("password", "rawPass");
                        }})))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("fake-jwt"))
                .andExpect(jsonPath("$.role").value("ADMIN"));
    }

    @Test
    void testLoginInvalidPassword() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();

        User user = new User() {{
            setEmail("test@example.com");
            setPassword("encodedPass");
            setRole(Role.ADMIN);
        }};

        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("wrongPass", "encodedPass")).thenReturn(false);

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new Object() {{
                            put("email", "test@example.com");
                            put("password", "wrongPass");
                        }})))
                .andExpect(status().isInternalServerError()); // RuntimeException -> 500
    }
}
