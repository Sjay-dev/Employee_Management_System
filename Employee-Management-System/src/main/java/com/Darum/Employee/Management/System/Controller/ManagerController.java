package com.Darum.Employee.Management.System.Controller;


import com.Darum.Employee.Management.System.Model.Employee;
import com.Darum.Employee.Management.System.Model.Leave;
import com.Darum.Employee.Management.System.Service.EmployeeService;
import com.Darum.Employee.Management.System.Service.LeaveService;
import com.Darum.Employee.Management.System.Service.ManagerService;
import com.Darum.Employee.Management.System.Untils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/manager")
@CrossOrigin("*")
public class ManagerController {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private ManagerService managerService;
    @Autowired
    LeaveService leaveService;

    private boolean isAuthorized(String authHeader, String expectedRole){
        try {
            String token = authHeader.substring(7);
            String role = jwtUtil.validateToken(token).get("role");

            return role.equals(expectedRole);
        }
        catch (Exception e){
            return false;
        }
    }

        @GetMapping("/viewAllEmployees")
    public ResponseEntity<List<Employee>> viewAllEmployees(@RequestHeader("Authorization")  String authHeader) {
        if(!isAuthorized(authHeader,"MANAGER")){
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok(managerService.findAllEmployees());
        }

        @PutMapping("/updateEmployeeStatus")
            public ResponseEntity<String> updateEmployeeStatus(@RequestParam Long employeeId,
               @RequestParam String status, @RequestHeader("Authorization")  String authHeader) {

            String token = authHeader.substring(7);
                if(!jwtUtil.validateToken(token).get("role").equals("MANAGER")){
                    return ResponseEntity.status(403).body("Access denied");
                }

                String message = managerService.updateEmployeeStatus(employeeId, status.toUpperCase());
                return ResponseEntity.ok(message);
        }

        @PostMapping("/applyForLeave")
            public ResponseEntity<Leave> applyForLeave(@RequestBody Leave leave,
                @RequestParam Long managerId,
                @RequestHeader("Authorization")  String authHeader) {

            String token = authHeader.substring(7);

            if(!jwtUtil.validateToken(token).get("role").equals("MANAGER")){
                return ResponseEntity.status(403).body(null);
            }
            Leave leaveResponse = leaveService.leaveApplicationByManager(leave, managerId);
            return ResponseEntity.ok(leaveResponse);
        }

    @GetMapping("/viewOwnLeaves")
    public ResponseEntity<List<Leave>> viewOwnLeaves(@RequestParam Long managerId , @RequestHeader("Authorization")  String authHeader) {

            String token = authHeader.substring(7);

        if(!jwtUtil.validateToken(token).get("role").equals("MANAGER")) {
            return ResponseEntity.status(403).body(null);
        }
            List<Leave> leaveResponse = leaveService.leavesByManager(managerId);
        return ResponseEntity.ok(leaveResponse);


    }

    @PutMapping("/updateEmployeeLeaveStatus")
    public ResponseEntity<String> updateEmployeeLeaveStatus(@RequestParam Long leaveId,
        @RequestParam String status,
        @RequestHeader("Authorization")  String authHeader) {

        String token = authHeader.substring(7);

        if(!jwtUtil.validateToken(token).get("role").equals("MANAGER")){
            return ResponseEntity.status(403).body("Access denied");
        }
        String leaveStatus = leaveService.updateLeaveStatus(leaveId, status.toUpperCase());
        return ResponseEntity.ok(leaveStatus);
    }

}
