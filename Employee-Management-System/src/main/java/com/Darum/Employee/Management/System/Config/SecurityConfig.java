package com.Darum.Employee.Management.System.Config;

import com.Darum.Employee.Management.System.Entites.Admin;
import com.Darum.Employee.Management.System.Entites.Enum.Role;
import com.Darum.Employee.Management.System.Repository.AdminRepository;
import com.Darum.Employee.Management.System.Repository.UserRepository;
import com.Darum.Employee.Management.System.Security.JwtTokenFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenFilter jwtTokenFilter;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for APIs
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/auth/**").permitAll() // Public endpoints
                        .requestMatchers("/api/admin/**").hasRole("ADMIN") // Admin-only endpoints
                        .requestMatchers("/api/manager/**").hasAnyRole("MANAGER", "ADMIN") // Manager or Admin
                        .requestMatchers("/api/employee/**").hasAnyRole("EMPLOYEE", "MANAGER", "ADMIN") // Employee, Manager, Admin
                        .requestMatchers(
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                "/swagger-ui/**"
                        ).permitAll()// Swagger endpoints
                        .anyRequest().authenticated() // All other requests require authentication
                )
                .addFilterBefore(jwtTokenFilter, UsernamePasswordAuthenticationFilter.class) // Add JWT filter
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS)) // Stateless sessions
                .httpBasic(AbstractHttpConfigurer::disable); // Disable default HTTP Basic auth

        return http.build();
    }

    /**
     * Seeds initial data (admin user) when the application starts
     */
    @Bean
    public CommandLineRunner dataSeeder(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            if (userRepository.count() == 0) { // Only seed if no users exist
                Admin admin = new Admin();
                admin.setFirstName("Super");
                admin.setLastName("Admin");
                admin.setEmail("admin@darum.com");
                admin.setPhoneNumber("000000000");
                admin.setPassword(passwordEncoder.encode("ChangeMe123!")); // Encode password
                admin.setRole(Role.ADMIN); // Set role to ADMIN
                userRepository.save(admin); // Save admin to database
            }
        };
    }

    /**
     * Password encoder for hashing passwords
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(); // Use BCrypt hashing algorithm
    }
}



