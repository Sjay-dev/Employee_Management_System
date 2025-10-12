package com.Darum.Employee.Management.System.Controller;


import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.Enum.Role;
import com.Darum.Employee.Management.System.Entites.Manager;
import com.Darum.Employee.Management.System.Security.JwtToken;
import com.Darum.Employee.Management.System.Service.EmployeeService;
import com.Darum.Employee.Management.System.Service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/manager")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ManagerController {

    private final EmployeeService employeeService;
    private final ManagerService managerService;
    private final JwtToken jwtToken;

    private String getEmailFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }

        String token = authHeader.substring(7);
        if (!jwtToken.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }
        return jwtToken.getEmailFromToken(token);
    }

    private String getRoleFromHeader(String authHeader) {
        String token = authHeader.substring(7);
        return jwtToken.getRoleFromToken(token);
    }

    @GetMapping("/department/employees")
    public ResponseEntity<?> getEmployeesInMyDepartment(@RequestHeader("Authorization") String authHeader) {
        String role = getRoleFromHeader(authHeader);
        if (!"MANAGER".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied: only managers allowed");
        }

        String email = getEmailFromHeader(authHeader);
        Manager manager = managerService.getManagerByEmail(email);

        if (manager.getDepartment() == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Manager has no department assigned");
        }

        List<Employee> employees = employeeService.getAllEmployeesByDepartmentId(manager.getDepartment().getDepartmentId());
        return ResponseEntity.ok(employees);
    }
}
