package com.Darum.Employee.Management.System.Controller;


import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Security.JwtToken;
import com.Darum.Employee.Management.System.Service.EmployeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;


@RestController
@RequestMapping("/api/employee")
@RequiredArgsConstructor
public class EmployeeController {

    private final EmployeeService employeeService;
    private final JwtToken jwtToken;

    // ------------------------- Helper Methods -------------------------

    /**
     * Extract Employee from JWT token and validate role.
     * @param authHeader   Authorization header from request
     * @param requiredRole Role required to access the endpoint
     */
    private Employee getUserFromToken(String authHeader, String requiredRole) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7); // Remove "Bearer "
        if (!jwtToken.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Invalid or expired token");
        }

        String email = jwtToken.getEmailFromToken(token);
        String role = jwtToken.getRoleFromToken(token);

        // Role validation
        if (!role.equals(requiredRole)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Access denied: required role is " + requiredRole);
        }

        Employee employee = employeeService.getEmployeeByEmail(email);
        if (employee == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Employee not found");
        }

        return employee;
    }

    // ------------------------- EMPLOYEE ENDPOINTS -------------------------

    /**
     * Get the currently logged-in employee's profile.
     * Only accessible by users with role "EMPLOYEE".
     */
    @GetMapping("/me")
    public ResponseEntity<Employee> getMyProfile(@RequestHeader("Authorization") String authHeader) {
        Employee employee = getUserFromToken(authHeader, "EMPLOYEE");
        return ResponseEntity.ok(employee);
    }
}


