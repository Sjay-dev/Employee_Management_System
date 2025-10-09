package com.Darum.Employee.Management.System.Untils;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Component
@Service
public class JWTUtil {

    private final String SECRET_KEY = "SECRET_KEY";
    private final SecretKey key = Keys.hmacShaKeyFor(SECRET_KEY.getBytes());

    public String generateToken(String fullName , String role){
        Map<String, Object> map = new HashMap<>();
        map.put("fullName", fullName);
        map.put("role", role);

        return Jwts.builder()
                .setClaims(map)
                .setSubject(fullName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*60*2))
                .signWith(SignatureAlgorithm.HS256, key)
                .compact();
    }

    public Map<String,String> validateToken(String token){
        Map<String,String> map = new HashMap<>();
        try {
            Claims claims = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            map.put("fullName",claims.get("fullName" , String.class));
            map.put("role",claims.get("role" , String.class));
            map.put("code", "200");
        }
        catch (ExpiredJwtException e){
            map.put("code", "401");
            map.put("error", "token expired. Please login again");
        }
        return map;
    }



    public String generatePasswordToken(String email, long minutes) {
        return Jwts.builder()
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + minutes * 60 * 1000))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

}