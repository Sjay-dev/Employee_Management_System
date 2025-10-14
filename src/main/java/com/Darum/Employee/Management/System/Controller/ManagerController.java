package com.Darum.Employee.Management.System.Controller;


import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.Manager;
import com.Darum.Employee.Management.System.Security.JwtToken;
import com.Darum.Employee.Management.System.Service.EmployeeService;
import com.Darum.Employee.Management.System.Service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
public class ManagerController {

    private final EmployeeService employeeService;
    private final ManagerService managerService;
    private final JwtToken jwtToken;

    // ------------------------- Helper Methods -------------------------

    // Extract email from JWT token in Authorization header
    private String getEmailFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        if (!jwtToken.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Invalid or expired token");
        }
        return jwtToken.getEmailFromToken(token);
    }

    // Extract role from JWT token in Authorization header
    private String getRoleFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        if (!jwtToken.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED,
                    "Invalid or expired token");
        }
        return jwtToken.getRoleFromToken(token);
    }

    // ------------------------- MANAGER ENDPOINTS -------------------------

    /**
     * Get all employees in the manager's department.
     * Only accessible by users with MANAGER role.
     */
    @GetMapping("/department/employees")
    public ResponseEntity<List<Employee>> getEmployeesInMyDepartment(
            @RequestHeader("Authorization") String authHeader) {

        String role = getRoleFromHeader(authHeader);
        if (!"MANAGER".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN,
                    "Access denied: only managers allowed");
        }

        String email = getEmailFromHeader(authHeader);
        Manager manager = managerService.getManagerByEmail(email);
        if (manager == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Manager not found");
        }

        if (manager.getDepartment() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Manager has no department assigned");
        }

        // Fetch employees in manager's department
        List<Employee> employees = employeeService.getAllEmployeesByDepartmentId(
                manager.getDepartment().getDepartmentId()
        );

        return ResponseEntity.ok(employees);
    }
}
