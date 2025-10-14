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
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final EmployeeService employeeService;
    private final ManagerService managerService;
    private final AdminService adminService;
    private final DepartmentService departmentService;
    private final JwtToken jwtToken;

    // ------------------------- Helper Methods -------------------------

    // Extract role from JWT token in Authorization header
    private String getRoleFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        if (!jwtToken.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }
        return jwtToken.getRoleFromToken(token);
    }

    // Extract email from JWT token in Authorization header
    private String getEmailFromHeader(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Missing or invalid Authorization header");
        }
        String token = authHeader.substring(7);
        if (!jwtToken.validateToken(token)) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid or expired token");
        }
        return jwtToken.getEmailFromToken(token);
    }

    // Check if the user has ADMIN role
    private void checkAdminRole(String role) {
        if (!"ADMIN".equals(role)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Access denied");
        }
    }

    // ------------------------- ADMIN MANAGEMENT -------------------------

    @GetMapping("/me")
    public ResponseEntity<Admin> getMyProfile(@RequestHeader("Authorization") String authHeader) {
        String email = getEmailFromHeader(authHeader);
        Admin me = adminService.getAdminByEmail(email);
        return ResponseEntity.ok(me);
    }

    @PutMapping("/me")
    public ResponseEntity<Admin> updateMyProfile(@RequestHeader("Authorization") String authHeader,
                                                 @RequestBody Admin admin) {
        String email = getEmailFromHeader(authHeader);
        Admin me = adminService.getAdminByEmail(email);
        return ResponseEntity.ok(adminService.updateAdmin(me.getUserId(), admin));
    }

    @PostMapping("/create")
    public ResponseEntity<Admin> createAdmin(@RequestHeader("Authorization") String authHeader,
                                             @RequestBody Admin admin) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        admin.setRole(Role.ADMIN);
        return ResponseEntity.ok(adminService.addAdmin(admin));
    }

    @GetMapping("/all")
    public ResponseEntity<List<Admin>> getAllAdmins(@RequestHeader("Authorization") String authHeader) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Admin>> getAdminsByName(@RequestParam String name) {
        List<Admin> admins = adminService.getAdminByName(name);
        return ResponseEntity.ok(admins);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@RequestHeader("Authorization") String authHeader,
                                            @PathVariable("id") Long adminId) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        adminService.deleteAdmin(adminId);
        return ResponseEntity.noContent().build();
    }

    // ------------------------- MANAGER MANAGEMENT -------------------------

    @PostMapping("/managers")
    public ResponseEntity<Manager> createManager(@RequestHeader("Authorization") String authHeader,
                                                 @RequestBody Manager manager) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        return ResponseEntity.ok(managerService.addManager(manager));
    }

    @PutMapping("/managers/{id}")
    public ResponseEntity<Manager> updateManager(@RequestHeader("Authorization") String authHeader,
                                                 @PathVariable("id") Long managerId,
                                                 @RequestBody Manager manager) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        return ResponseEntity.ok(managerService.updateManager(managerId, manager));
    }

    @DeleteMapping("/managers/{id}")
    public ResponseEntity<Void> deleteManager(@RequestHeader("Authorization") String authHeader,
                                              @PathVariable("id") Long managerId) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        managerService.deleteManager(managerId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/managers/{id}")
    public ResponseEntity<Manager> getManager(@RequestHeader("Authorization") String authHeader,
                                              @PathVariable("id") Long managerId) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        return ResponseEntity.ok(managerService.getManagerById(managerId));
    }

    @GetMapping("/managers/departments/{departmentId}")
    public ResponseEntity<Manager> getManagerByDepartment(@PathVariable Long departmentId) {
        Manager manager = managerService.getManagerByDepartmentId(departmentId);
        return ResponseEntity.ok(manager);
    }

    @GetMapping("/managers/search")
    public ResponseEntity<List<Manager>> getManagersByName(@RequestParam String name) {
        List<Manager> managers = managerService.getAllManagersByName(name);
        return ResponseEntity.ok(managers);
    }

    @GetMapping("/managers")
    public ResponseEntity<List<Manager>> getAllManagers(@RequestHeader("Authorization") String authHeader) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        return ResponseEntity.ok(managerService.getAllManagers());
    }

    // ------------------------- EMPLOYEE MANAGEMENT -------------------------

    @PostMapping("/employees")
    public ResponseEntity<Employee> createEmployee(@RequestHeader("Authorization") String authHeader,
                                                   @RequestBody Employee employee) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        return ResponseEntity.ok(employeeService.addEmployee(employee));
    }

    @PutMapping("/employees/{id}")
    public ResponseEntity<Employee> updateEmployee(@RequestHeader("Authorization") String authHeader,
                                                   @PathVariable("id") Long employeeId,
                                                   @RequestBody Employee employee) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        return ResponseEntity.ok(employeeService.updateEmployee(employeeId, employee));
    }

    @DeleteMapping("/employees/{id}")
    public ResponseEntity<Void> deleteEmployee(@RequestHeader("Authorization") String authHeader,
                                               @PathVariable("id") Long employeeId) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        employeeService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employees/{id}")
    public ResponseEntity<Employee> getEmployee(@RequestHeader("Authorization") String authHeader,
                                                @PathVariable("id") Long employeeId) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        return ResponseEntity.ok(employeeService.getEmployeeById(employeeId));
    }

    @GetMapping("/employees/search")
    public ResponseEntity<List<Employee>> getEmployeesByName(@RequestParam String name) {
        List<Employee> employees = employeeService.getAllEmployeesByName(name);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employees/manager/{managerId}")
    public ResponseEntity<List<Employee>> getEmployeesByManager(@PathVariable Long managerId) {
        List<Employee> employees = employeeService.getAllEmployeesByManagerId(managerId);
        return ResponseEntity.ok(employees);
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees(@RequestHeader("Authorization") String authHeader) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        return ResponseEntity.ok(employeeService.getAllEmployees());
    }

    // ------------------------- DEPARTMENT MANAGEMENT -------------------------

    @PostMapping("/departments")
    public ResponseEntity<Department> createDepartment(@RequestHeader("Authorization") String authHeader,
                                                       @RequestBody Department department) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        return ResponseEntity.ok(departmentService.addDepartment(department));
    }

    @PutMapping("/departments/{id}")
    public ResponseEntity<Department> updateDepartment(@RequestHeader("Authorization") String authHeader,
                                                       @PathVariable("id") Long departmentId,
                                                       @RequestBody Department department) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        return ResponseEntity.ok(departmentService.updateDepartmentDetails(departmentId, department));
    }

    @PostMapping("/departments/{departmentId}/managers/{managerId}")
    public ResponseEntity<Department> addManagerToDepartment(@PathVariable Long departmentId,
                                                             @PathVariable Long managerId) {
        Department department = departmentService.addExistingManagerToDepartment(departmentId, managerId);
        return ResponseEntity.ok(department);
    }

    @DeleteMapping("/departments/{id}")
    public ResponseEntity<Void> deleteDepartment(@RequestHeader("Authorization") String authHeader,
                                                 @PathVariable("id") Long departmentId) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        departmentService.deleteDepartment(departmentId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/departments/{departmentId}/managers/{managerId}")
    public ResponseEntity<Department> removeManagerFromDepartment(@PathVariable Long departmentId,
                                                                  @PathVariable Long managerId) {
        Department department = departmentService.removeManagerFromDepartment(departmentId, managerId);
        return ResponseEntity.ok(department);
    }

    @PostMapping("/departments/{departmentId}/employees/{employeeId}")
    public ResponseEntity<Department> addEmployeeToDepartment(@PathVariable Long departmentId,
                                                              @PathVariable Long employeeId) {
        Department department = departmentService.addExistingEmployeeToDepartment(departmentId, employeeId);
        return ResponseEntity.ok(department);
    }

    @DeleteMapping("/departments/{departmentId}/employees/{employeeId}")
    public ResponseEntity<Department> removeEmployeeFromDepartment(@PathVariable Long departmentId,
                                                                   @PathVariable Long employeeId) {
        Department department = departmentService.removeEmployeeFromDepartment(departmentId, employeeId);
        return ResponseEntity.ok(department);
    }

    @GetMapping("/departments/{id}")
    public ResponseEntity<Department> getDepartment(@RequestHeader("Authorization") String authHeader,
                                                    @PathVariable("id") Long departmentId) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        return ResponseEntity.ok(departmentService.getDepartmentById(departmentId));
    }

    @GetMapping("/departments")
    public ResponseEntity<List<Department>> getAllDepartments(@RequestHeader("Authorization") String authHeader) {
        String role = getRoleFromHeader(authHeader);
        checkAdminRole(role);

        return ResponseEntity.ok(departmentService.getAllDepartments());
    }
}



