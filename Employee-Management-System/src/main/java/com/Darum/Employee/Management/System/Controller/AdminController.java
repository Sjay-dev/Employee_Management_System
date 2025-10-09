package com.Darum.Employee.Management.System.Controller;


import com.Darum.Employee.Management.System.Model.Employee;
import com.Darum.Employee.Management.System.Model.Leave;
import com.Darum.Employee.Management.System.Model.Manager;
import com.Darum.Employee.Management.System.Service.AdminService;
import com.Darum.Employee.Management.System.Service.ManagerService;
import com.Darum.Employee.Management.System.Untils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/admin")
@CrossOrigin("*")
public class AdminController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private ManagerService  managerService;

    @Autowired
    private JWTUtil jwtUtil;

    @PostMapping("/addmanager")
        public ResponseEntity<String> addManager(@RequestBody Manager manager,
            @RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);

            if(!jwtUtil.validateToken(token).get("role").equals("ADMIN")){
                    return ResponseEntity.status(403).body("Access Denied");
            }

                adminService.addManager(manager);
            return ResponseEntity.ok("Manager added successfully\n\n" +
                    "Manager ID: " + manager.getManagerId());
    }

    @GetMapping("/viewAllManagers")
        public ResponseEntity<List<Manager>> viewAllManagers(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        if(!jwtUtil.validateToken(token).get("role").equals("ADMIN")){
            return ResponseEntity.status(403).body(null);
        }
            return ResponseEntity.ok(adminService.findAllManagers());
    }

    @GetMapping("/viewAllEmployees")
         public ResponseEntity<List<Employee>> viewAllEmployees(@RequestHeader("Authorization") String authHeader){
        String token = authHeader.substring(7);
        if(!jwtUtil.validateToken(token).get("role").equals("ADMIN")){
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok(adminService.findAllEmployees());

    }

        @PutMapping("/updateEmployeeStatus")
            public ResponseEntity<String> updateEmployeeStatus(
                    @RequestParam Long employeeId , @RequestParam String status,
                    @RequestHeader("Authorization") String authHeader){

            String token = authHeader.substring(7);
            if(!jwtUtil.validateToken(token).get("role").equals("ADMIN")){
                return ResponseEntity.status(403).body("Access Denied");
            }
            String message = managerService.updateEmployeeStatus(employeeId, status.toUpperCase());
            return ResponseEntity.ok(message);
        }

         @GetMapping("/viewAllLeaveApplications")
            public ResponseEntity<List<Leave>> viewAllLeaveApplications(@RequestHeader("Authorization") String authHeader){
                 String token = authHeader.substring(7);
                 if(!jwtUtil.validateToken(token).get("role").equals("ADMIN")){
                 return ResponseEntity.status(403).body(null);
                }
                     List<Leave> leaves = adminService.findAllLeaves();
                    return ResponseEntity.ok(leaves);

    }

        @DeleteMapping("/deleteEmployee")
             public ResponseEntity<String> deleteEmployee(
            @RequestParam Long employeeId , @RequestHeader("Authorization") String authHeader){

            String token = authHeader.substring(7);
             if(!jwtUtil.validateToken(token).get("role").equals("ADMIN")){
            return ResponseEntity.status(403).body("Access Denied");
             }
                String message = adminService.deleteEmployee(employeeId);
                return ResponseEntity.ok(message);
    }

        @DeleteMapping("/deleteManager")
            public ResponseEntity<String> deleteManager(
            @RequestParam Long managerId , @RequestHeader("Authorization") String authHeader){

                String token = authHeader.substring(7);
                if(!jwtUtil.validateToken(token).get("role").equals("ADMIN")){
            return ResponseEntity.status(403).body("Access Denied");
             }
                 String message = adminService.deleteManager(managerId);
                 return ResponseEntity.ok(message);
    }

        @GetMapping("/managerCount")
             public ResponseEntity<Long> getManagerCount(@RequestHeader("Authorization") String authHeader){
             String token = authHeader.substring(7);
                 if(!jwtUtil.validateToken(token).get("role").equals("ADMIN")){
             return ResponseEntity.status(403).body(null);
        }

        return ResponseEntity.ok(adminService.numberOfManagers());

    }

            @GetMapping("/employeeCount")
                public ResponseEntity<Long> getEmployeeCount(@RequestHeader("Authorization") String authHeader){
                String token = authHeader.substring(7);
        if(!jwtUtil.validateToken(token).get("role").equals("ADMIN")){
            return ResponseEntity.status(403).body(null);
        }

        return ResponseEntity.ok(adminService.numberOfEmployees());

    }



}
