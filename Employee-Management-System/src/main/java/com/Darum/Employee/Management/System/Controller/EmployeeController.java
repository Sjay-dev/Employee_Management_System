package com.Darum.Employee.Management.System.Controller;


import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.User;
import com.Darum.Employee.Management.System.Security.JwtToken;
import com.Darum.Employee.Management.System.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/employees")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final JwtToken jwtToken;

    private User getUserFromToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7); // Remove "Bearer "
        if (!jwtToken.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }

        String email = jwtToken.getEmailFromToken(token);
        String role = jwtToken.getRoleFromToken(token);

        return employeeService.getEmployeeByEmail(email);
    }


    @GetMapping("/me")
    public ResponseEntity<Employee> getMyProfile(@RequestHeader("Authorization") String authHeader) {
        Employee employee = (Employee) getUserFromToken(authHeader);
        return ResponseEntity.ok(employee);
    }


}
