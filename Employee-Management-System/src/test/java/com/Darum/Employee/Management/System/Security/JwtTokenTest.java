package com.Darum.Employee.Management.System.Security;


import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class JwtTokenTest {

    private JwtToken jwtToken;

    @BeforeEach
    void setUp() {
        // Initialize JwtToken instance before each test
        jwtToken = new JwtToken();
    }

    /**
     * Test case: Generate a JWT token and verify its claims.
     * Ensures token is not null, valid, and contains correct email and role.
     */
    @Test
    void testGenerateTokenAndParseClaims() {
        String email = "test@example.com";
        String role = "ADMIN";

        // Generate token
        String token = jwtToken.generateToken(email, role);

        // Assertions
        assertThat(token).isNotNull();
        assertThat(jwtToken.validateToken(token)).isTrue();
        assertThat(jwtToken.getEmailFromToken(token)).isEqualTo(email);
        assertThat(jwtToken.getRoleFromToken(token)).isEqualTo(role);
    }

    /**
     * Test case: Validate an invalid JWT token.
     * Should return false for validation check.
     */
    @Test
    void testValidateToken_invalidToken() {
        String invalidToken = "invalid.token.value";
        assertThat(jwtToken.validateToken(invalidToken)).isFalse();
    }

    /**
     * Test case: Attempt to extract email from an invalid token.
     * Should throw JwtException.
     */
    @Test
    void testGetEmailFromToken_invalidToken_throws() {
        String invalidToken = "invalid.token.value";
        assertThatThrownBy(() -> jwtToken.getEmailFromToken(invalidToken))
                .isInstanceOf(JwtException.class);
    }

    /**
     * Test case: Attempt to extract role from an invalid token.
     * Should throw JwtException.
     */
    @Test
    void testGetRoleFromToken_invalidToken_throws() {
        String invalidToken = "invalid.token.value";
        assertThatThrownBy(() -> jwtToken.getRoleFromToken(invalidToken))
                .isInstanceOf(JwtException.class);
    }
}


