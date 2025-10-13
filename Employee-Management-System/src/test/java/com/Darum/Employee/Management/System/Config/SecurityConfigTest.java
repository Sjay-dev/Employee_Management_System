package com.Darum.Employee.Management.System.Config;

import com.Darum.Employee.Management.System.Entites.Admin;
import com.Darum.Employee.Management.System.Entites.Enum.Role;
import com.Darum.Employee.Management.System.Repository.UserRepository;
import com.Darum.Employee.Management.System.Security.JwtTokenFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SecurityConfigTest {

    @Mock
    private JwtTokenFilter jwtTokenFilter;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Captor
    private ArgumentCaptor<Admin> adminCaptor;

    @InjectMocks
    private SecurityConfig securityConfig;

    @BeforeEach
    void setUp() {
        // Initialize mocks and inject them into SecurityConfig
        MockitoAnnotations.openMocks(this);
    }

    // ------------------- PASSWORD ENCODER -------------------

    @Test
    void testPasswordEncoderBean() {
        // Test that the passwordEncoder bean is BCryptPasswordEncoder
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertThat(encoder).isInstanceOf(BCryptPasswordEncoder.class);

        // Test encoding and matching functionality
        String encoded = encoder.encode("password");
        assertThat(encoder.matches("password", encoded)).isTrue();
    }

    // ------------------- SECURITY FILTER CHAIN -------------------

    @Test
    void testSecurityFilterChainConfiguration() throws Exception {
        // Mock HttpSecurity and its chained methods
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        when(http.csrf(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.addFilterBefore(any(), any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.httpBasic(any())).thenReturn(http);

        // Get SecurityFilterChain bean
        SecurityFilterChain chain = securityConfig.securityFilterChain(http);

        // Validate chain is created
        assertThat(chain).isNotNull();

        // Verify JWT filter was added
        verify(http).addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);

        // Verify session management was configured
        verify(http).sessionManagement(any());
    }

    // ------------------- DATA SEEDER -------------------

    @Test
    void testDataSeederWhenNoUserExists() throws Exception {
        // Setup: no users in repository
        when(userRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode(any())).thenReturn("encodedPass");

        // Run the CommandLineRunner
        CommandLineRunner runner = securityConfig.dataSeeder(userRepository, passwordEncoder);
        runner.run();

        // Verify a new admin is saved
        verify(userRepository, times(1)).save(adminCaptor.capture());
        Admin savedAdmin = adminCaptor.getValue();

        assertThat(savedAdmin.getEmail()).isEqualTo("admin@darum.com");
        assertThat(savedAdmin.getRole()).isEqualTo(Role.ADMIN);
        assertThat(savedAdmin.getPassword()).isEqualTo("encodedPass");
    }

    @Test
    void testDataSeederWhenUserAlreadyExists() throws Exception {
        // Setup: some users already exist
        when(userRepository.count()).thenReturn(5L);

        // Run the CommandLineRunner
        CommandLineRunner runner = securityConfig.dataSeeder(userRepository, passwordEncoder);
        runner.run();

        // Verify no new admin is created
        verify(userRepository, never()).save(any(Admin.class));
    }

}

