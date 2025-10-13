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
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testPasswordEncoderBean() {
        PasswordEncoder encoder = securityConfig.passwordEncoder();
        assertThat(encoder).isInstanceOf(BCryptPasswordEncoder.class);
        String encoded = encoder.encode("password");
        assertThat(encoder.matches("password", encoded)).isTrue();
    }

    @Test
    void testSecurityFilterChainConfiguration() throws Exception {
        HttpSecurity http = mock(HttpSecurity.class, RETURNS_DEEP_STUBS);

        when(http.csrf(any())).thenReturn(http);
        when(http.authorizeHttpRequests(any())).thenReturn(http);
        when(http.addFilterBefore(any(), any())).thenReturn(http);
        when(http.sessionManagement(any())).thenReturn(http);
        when(http.httpBasic(any())).thenReturn(http);

        SecurityFilterChain chain = securityConfig.securityFilterChain(http);

        assertThat(chain).isNotNull();
        verify(http).addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class);
        verify(http).sessionManagement(any());
    }

    @Test
    void testDataSeederWhenNoUserExists() throws Exception {
        when(userRepository.count()).thenReturn(0L);
        when(passwordEncoder.encode(any())).thenReturn("encodedPass");

        CommandLineRunner runner = securityConfig.dataSeeder(userRepository, passwordEncoder);
        runner.run();

        verify(userRepository, times(1)).save(adminCaptor.capture());
        Admin savedAdmin = adminCaptor.getValue();
        assertThat(savedAdmin.getEmail()).isEqualTo("admin@darum.com");
        assertThat(savedAdmin.getRole()).isEqualTo(Role.ADMIN);
        assertThat(savedAdmin.getPassword()).isEqualTo("encodedPass");
    }

    @Test
    void testDataSeederWhenUserAlreadyExists() throws Exception {
        when(userRepository.count()).thenReturn(5L);

        CommandLineRunner runner = securityConfig.dataSeeder(userRepository, passwordEncoder);
        runner.run();

        verify(userRepository, never()).save(any(Admin.class));
    }
}
