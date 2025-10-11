package com.Darum.Employee.Management.System.Controller;


import com.Darum.Employee.Management.System.Entites.Admin;
import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.Manager;
import com.Darum.Employee.Management.System.Service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class AdminController {

    private final AdminService adminService;

    // ---------------------------
    // 🧩 Admin Endpoints
    // ---------------------------

    @PostMapping()
    public ResponseEntity<Admin> addAdmin(@RequestBody Admin admin) {
        System.out.println("Incoming admin: " + admin);
        Admin savedAdmin = adminService.addAdmin(admin);
        return ResponseEntity.ok(savedAdmin);
    }

    @GetMapping("/{adminId}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long adminId) {
        return ResponseEntity.ok(adminService.getAdminById(adminId));
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<Admin> getAdminByEmail(@PathVariable String email) {
        return ResponseEntity.ok(adminService.getAdminByEmail(email));
    }

    @GetMapping()
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @GetMapping("/search")
    public ResponseEntity<List<Admin>> searchAdmins(@RequestParam String name) {
        return ResponseEntity.ok(adminService.getAdminByName(name));
    }

    @PutMapping("/{adminId}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Long adminId, @RequestBody Admin admin) {
        return ResponseEntity.ok(adminService.updateAdmin(adminId, admin));
    }

    @DeleteMapping("/{adminId}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long adminId) {
        adminService.deleteAdmin(adminId);
        return ResponseEntity.noContent().build();
    }


    // ---------------------------
    // 👨‍💼 Manager Endpoints
    // ---------------------------

    @PostMapping("/managers")
    public ResponseEntity<Manager> addManager(@RequestBody Manager manager) {
        return ResponseEntity.ok(adminService.addManager(manager));
    }

    @GetMapping("/managers/{managerId}")
    public ResponseEntity<Manager> getManagerById(@PathVariable Long managerId) {
        return ResponseEntity.ok(adminService.getManagerById(managerId));
    }

    @GetMapping("/managers/email/{email}")
    public ResponseEntity<Manager> getManagerByEmail(@PathVariable String email) {
        return ResponseEntity.ok(adminService.getManagerByEmail(email));
    }

    @GetMapping("/managers")
    public ResponseEntity<List<Manager>> getAllManagers() {
        return ResponseEntity.ok(adminService.getAllManagers());
    }

    @GetMapping("/managers/search")
    public ResponseEntity<List<Manager>> searchManagers(@RequestParam String name) {
        return ResponseEntity.ok(adminService.getAllManagersByName(name));
    }

    @PutMapping("/managers/{managerId}")
    public ResponseEntity<Manager> updateManager(@PathVariable Long managerId, @RequestBody Manager manager) {
        return ResponseEntity.ok(adminService.updateManager(managerId, manager));
    }

    @DeleteMapping("/managers/{managerId}")
    public ResponseEntity<Void> deleteManager(@PathVariable Long managerId) {
        adminService.deleteManager(managerId);
        return ResponseEntity.noContent().build();
    }


    // ---------------------------
    // 👨‍🔧 Employee Endpoints
    // ---------------------------

    @PostMapping("/employees")
    public ResponseEntity<Employee> addEmployee(@RequestBody Employee employee) {
        return ResponseEntity.ok(adminService.addEmployee(employee));
    }

    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployeeById(@PathVariable Long employeeId) {
        return ResponseEntity.ok(adminService.getEmployeeById(employeeId));
    }

    @GetMapping("/employees/email/{email}")
    public ResponseEntity<Employee> getEmployeeByEmail(@PathVariable String email) {
        return ResponseEntity.ok(adminService.getEmployeeByEmail(email));
    }

    @GetMapping("/employees")
    public ResponseEntity<List<Employee>> getAllEmployees() {
        return ResponseEntity.ok(adminService.getAllEmployees());
    }

    @GetMapping("/employees/search")
    public ResponseEntity<List<Employee>> searchEmployees(@RequestParam String name) {
        return ResponseEntity.ok(adminService.getAllEmployeesByName(name));
    }

    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> updateEmployee(@PathVariable Long employeeId, @RequestBody Employee employee) {
        return ResponseEntity.ok(adminService.updateEmployee(employeeId, employee));
    }

    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<Void> deleteEmployee(@PathVariable Long employeeId) {
        adminService.deleteEmployee(employeeId);
        return ResponseEntity.noContent().build();
    }
}