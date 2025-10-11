package com.Darum.Employee.Management.System.Controller;

import com.Darum.Employee.Management.System.DTO.LoginRequest;
import com.Darum.Employee.Management.System.Entites.Admin;
import com.Darum.Employee.Management.System.Repository.AdminRepository;
import com.Darum.Employee.Management.System.Security.JwtToken;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.util.Map;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AdminRepository adminRepository;
    private final JwtToken jwtToken;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Admin admin = adminRepository.findAdminByEmail(request.getEmail());

        if (!admin.getPassword().equals(request.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        String token = jwtToken.generateToken(admin.getEmail(), admin.getRole().name());

        return ResponseEntity.ok(Map.of(
                "token", token,
                "expires_in", "1800 seconds",
                "role", admin.getRole()
        ));
    }
}
