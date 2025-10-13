package com.Darum.Employee.Management.System.Security;


import io.jsonwebtoken.JwtException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;

class JwtTokenTest {

    private JwtToken jwtToken;

    @BeforeEach
    void setUp() {
        jwtToken = new JwtToken();
    }

    @Test
    void testGenerateTokenAndParseClaims() {
        String email = "test@example.com";
        String role = "ADMIN";

        String token = jwtToken.generateToken(email, role);

        assertThat(token).isNotNull();
        assertThat(jwtToken.validateToken(token)).isTrue();
        assertThat(jwtToken.getEmailFromToken(token)).isEqualTo(email);
        assertThat(jwtToken.getRoleFromToken(token)).isEqualTo(role);
    }

    @Test
    void testValidateToken_invalidToken() {
        String invalidToken = "invalid.token.value";
        assertThat(jwtToken.validateToken(invalidToken)).isFalse();
    }

    @Test
    void testGetEmailFromToken_invalidToken_throws() {
        String invalidToken = "invalid.token.value";
        assertThatThrownBy(() -> jwtToken.getEmailFromToken(invalidToken))
                .isInstanceOf(JwtException.class);
    }

    @Test
    void testGetRoleFromToken_invalidToken_throws() {
        String invalidToken = "invalid.token.value";
        assertThatThrownBy(() -> jwtToken.getRoleFromToken(invalidToken))
                .isInstanceOf(JwtException.class);
    }
}

