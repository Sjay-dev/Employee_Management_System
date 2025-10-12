package com.Darum.Employee.Management.System.Controller;


import com.Darum.Employee.Management.System.Entites.Admin;
import com.Darum.Employee.Management.System.Entites.Department;
import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.Enum.Role;
import com.Darum.Employee.Management.System.Entites.Manager;
import com.Darum.Employee.Management.System.Security.JwtToken;
import com.Darum.Employee.Management.System.Service.AdminService;
import com.Darum.Employee.Management.System.Service.DepartmentService;
import com.Darum.Employee.Management.System.Service.EmployeeService;
import com.Darum.Employee.Management.System.Service.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@CrossOrigin("*")
public class AdminController {

    private final EmployeeService employeeService;
    private final ManagerService managerService;
    private final AdminService adminService;
    private final DepartmentService departmentService;
    private final JwtToken jwtToken;

    private String getRoleFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        if (!jwtToken.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }
        return jwtToken.getRoleFromToken(token);
    }

    private String getEmailFromHeader(String authHeader) {
        String token = authHeader.substring(7);
        return jwtToken.getEmailFromToken(token);
    }

    //  ADMIN PROFILE MANAGEMENT

    // Get own profile
    @GetMapping("/me")
    public ResponseEntity<Admin> getMyProfile(@RequestHeader("Authorization") String authHeader) {
        String email = getEmailFromHeader(authHeader);
        Admin me = adminService.getAdminByEmail(email);
        return ResponseEntity.ok(me);
    }

    // Update own profile
    @PutMapping("/me")
    public ResponseEntity<Admin> updateMyProfile(@RequestHeader("Authorization") String authHeader,
                                                 @RequestBody Admin admin) {
        String email = getEmailFromHeader(authHeader);
        Admin me = adminService.getAdminByEmail(email);


        return ResponseEntity.ok(adminService.updateAdmin(me.getUserId(), admin));
    }


    // Create a new Admin (admin-only)
    @PostMapping("/create")
    public ResponseEntity<?> createAdmin(@RequestHeader("Authorization") String authHeader,
                                         @RequestBody Admin admin) {
        String role = getRoleFromHeader(authHeader);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        admin.setRole(Role.ADMIN);
        return ResponseEntity.ok(adminService.addAdmin(admin));
    }

    // Get all admins (admin-only)
    @GetMapping("/all")
    public ResponseEntity<?> getAllAdmins(@RequestHeader("Authorization") String authHeader) {
        String role = getRoleFromHeader(authHeader);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    // Delete another admin (admin-only)
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAdmin(@RequestHeader("Authorization") String authHeader,
                                         @PathVariable Long id) {
        String role = getRoleFromHeader(authHeader);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    // 🔹 EMPLOYEE MANAGEMENT (for admins only)

    @PostMapping("/employees")
    public ResponseEntity<?> createEmployee(@RequestHeader("Authorization") String authHeader,
                                            @RequestBody Employee employee) {
        String role = getRoleFromHeader(authHeader);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        return ResponseEntity.ok(employeeService.addEmployee(employee));
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<?> updateEmployee(@RequestHeader("Authorization") String authHeader,Long employeeId,
                                            @RequestBody Employee employee) {
        String role = getRoleFromHeader(authHeader);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        return ResponseEntity.ok(employeeService.updateEmployee(employeeId, employee));
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<?> deleteEmployee(@RequestHeader("Authorization") String authHeader,Long employeeId) {
        String role = getRoleFromHeader(authHeader);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<?> getEmployee(@RequestHeader("Authorization") String authHeader,Long employeeId) {
        String role = getRoleFromHeader(authHeader);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        return ResponseEntity.ok(employeeService.getEmployeeById(employeeId));
    }

    @GetMapping("/employees")
    public ResponseEntity<?> getAllEmployees(@RequestHeader("Authorization") String authHeader) {
        String role = getRoleFromHeader(authHeader);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        return ResponseEntity.ok(employeeService.getAllEmployees());
    }


    // 🔹 MANAGER MANAGEMENT (for admins only)

    @PostMapping("/managers")
    public ResponseEntity<?> createManager(@RequestHeader("Authorization") String authHeader,
                                           @RequestBody Manager manager) {
        String role = getRoleFromHeader(authHeader);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        return ResponseEntity.ok(managerService.addManager(manager));
    }

    @PutMapping("/managers/{id}")
    public ResponseEntity<?> updateManager(@RequestHeader("Authorization") String authHeader,
                                           Long managerId,
                                           @RequestBody Manager manager) {
        String role = getRoleFromHeader(authHeader);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        return ResponseEntity.ok(managerService.updateManager(managerId, manager));
    }

    @DeleteMapping("/managers/{id}")
    public ResponseEntity<?> deleteManager(@RequestHeader("Authorization") String authHeader,
                                          Long managerId) {
        String role = getRoleFromHeader(authHeader);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        managerService.deleteManager(managerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/managers/{id}")
    public ResponseEntity<?> getManager(@RequestHeader("Authorization") String authHeader,
                                        Long managerId) {
        String role = getRoleFromHeader(authHeader);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        return ResponseEntity.ok(managerService.getManagerById(managerId));
    }

    @GetMapping("/managers")
    public ResponseEntity<?> getAllManagers(@RequestHeader("Authorization") String authHeader) {
        String role = getRoleFromHeader(authHeader);
        if (!"ADMIN".equals(role)) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }

        return ResponseEntity.ok(managerService.getAllManagers());
    }

    // DEPARTMENT MANAGEMENT

    @PostMapping("/departments")
    public ResponseEntity<?> createDepartment(@RequestHeader("Authorization") String authHeader,
                                              @RequestBody Department department) {
        if (!"ADMIN".equals(getRoleFromHeader(authHeader))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(departmentService.addDepartment(department));
    }

    @PutMapping("/departments/{id}")
    public ResponseEntity<?> updateDepartment(@RequestHeader("Authorization") String authHeader, Long departmentId,
                                              @RequestBody Department department) {
        if (!"ADMIN".equals(getRoleFromHeader(authHeader))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(departmentService.updateDepartmentDetails(departmentId, department));
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<?> deleteDepartment(@RequestHeader("Authorization") String authHeader, Long departmentId) {
        if (!"ADMIN".equals(getRoleFromHeader(authHeader))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<?> getDepartment(@RequestHeader("Authorization") String authHeader, Long departmentId) {
        if (!"ADMIN".equals(getRoleFromHeader(authHeader))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(departmentService.getDepartmentById(departmentId));
    }

    @GetMapping("/departments")
    public ResponseEntity<?> getAllDepartments(@RequestHeader("Authorization") String authHeader) {
        if (!"ADMIN".equals(getRoleFromHeader(authHeader))) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Access denied");
        }
        return ResponseEntity.ok(departmentService.getAllDepartments());
    }
}

