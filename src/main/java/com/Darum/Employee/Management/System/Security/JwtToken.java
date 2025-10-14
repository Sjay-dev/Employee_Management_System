package com.Darum.Employee.Management.System.Security;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JwtToken {

    private final SecretKey SECURITYKEY = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // Token expiration time in milliseconds (30 minutes)
    private final long EXPIRATION_TIME = 1800000;


     // Generate a JWT token containing email and role as claims.
    public String generateToken(String email, String role) {
        return Jwts.builder()
                .setSubject(email) // Set the email as subject
                .claim("role", role) // Include role in claims
                .setIssuedAt(new Date()) // Token issue time
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME)) // Expiration
                .signWith(SECURITYKEY) // Sign with secret key
                .compact();
    }


//      Validate a JWT token.
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder()
                    .setSigningKey(SECURITYKEY)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (JwtException | IllegalArgumentException e) {
            return false;
        }
    }


     // Extract the email (subject) from a JWT token.
    public String getEmailFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECURITYKEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

     //Extract the role from a JWT token.
    public String getRoleFromToken(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(SECURITYKEY)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.get("role", String.class);
    }
}
