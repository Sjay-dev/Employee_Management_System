package com.Darum.Employee.Management.System.Security;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.*;

class JwtTokenFilterTest {

    @Mock
    private JwtToken jwtToken;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @InjectMocks
    private JwtTokenFilter jwtTokenFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        // Clear any existing authentication context before each test
        SecurityContextHolder.clearContext();
    }

    /**
     * Test case: When a valid JWT token is provided in the Authorization header,
     * the filter should set the SecurityContext with the correct authentication.
     */
    @Test
    void testDoFilterInternal_withValidToken() throws Exception {
        String token = "valid.token";
        String email = "user@example.com";
        String role = "ADMIN";

        // Mock request header and JWT token validation
        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtToken.validateToken(token)).thenReturn(true);
        when(jwtToken.getEmailFromToken(token)).thenReturn(email);
        when(jwtToken.getRoleFromToken(token)).thenReturn(role);

        // Execute filter
        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // Verify that SecurityContext is set correctly
        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertThat(auth).isInstanceOf(UsernamePasswordAuthenticationToken.class);
        assertThat(auth.getName()).isEqualTo(email);
        assertThat(auth.getAuthorities().stream().map(Object::toString).toList())
                .containsExactly("ROLE_ADMIN");

        // Verify filter chain continues
        verify(filterChain).doFilter(request, response);
    }

    /**
     * Test case: When no Authorization header is present,
     * SecurityContext should remain empty and filter chain continues.
     */
    @Test
    void testDoFilterInternal_noToken() throws Exception {
        when(request.getHeader("Authorization")).thenReturn(null);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // SecurityContext should be empty
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        // Verify filter chain continues
        verify(filterChain).doFilter(request, response);
    }

    /**
     * Test case: When an invalid JWT token is provided,
     * SecurityContext should remain empty and filter chain continues.
     */
    @Test
    void testDoFilterInternal_invalidToken() throws Exception {
        String token = "invalid.token";

        when(request.getHeader("Authorization")).thenReturn("Bearer " + token);
        when(jwtToken.validateToken(token)).thenReturn(false);

        jwtTokenFilter.doFilterInternal(request, response, filterChain);

        // SecurityContext should be empty
        assertThat(SecurityContextHolder.getContext().getAuthentication()).isNull();

        // Verify filter chain continues
        verify(filterChain).doFilter(request, response);
    }
}


