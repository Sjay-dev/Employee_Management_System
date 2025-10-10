package com.Darum.Employee.Management.System.Untils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JWTUtil {

    private final SecretKey secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    public SecretKey getSecretKey() {
        return secretKey;
    }

    public String generateToken(String fullName, String role) {
        Map<String, Object> claims = new HashMap<>();
        claims.put("fullName", fullName);
        claims.put("role", role);

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(fullName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 2)) // 2 hours
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }

    public Map<String, String> validateToken(String token) {
        Map<String, String> map = new HashMap<>();
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(secretKey)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            map.put("fullName", claims.get("fullName", String.class));
            map.put("role", claims.get("role", String.class));
            map.put("code", "200");
        } catch (ExpiredJwtException e) {
            map.put("code", "401");
            map.put("error", "Token expired. Please log in again.");
        } catch (JwtException e) {
            map.put("code", "400");
            map.put("error", "Invalid token.");
        }
        return map;
    }

    public String generatePasswordToken(String email, long minutes) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + minutes * 60 * 1000))
                .signWith(secretKey, SignatureAlgorithm.HS256)
                .compact();
    }
}