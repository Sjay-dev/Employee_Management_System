package com.Darum.Employee.Management.System.Controller;

import com.Darum.Employee.Management.System.DTO.LoginRequest;
import com.Darum.Employee.Management.System.Model.Admin;
import com.Darum.Employee.Management.System.Model.Employee;
import com.Darum.Employee.Management.System.Model.Manager;
import com.Darum.Employee.Management.System.Service.AdminService;
import com.Darum.Employee.Management.System.Service.EmployeeService;
import com.Darum.Employee.Management.System.Service.ManagerService;
import com.Darum.Employee.Management.System.Untils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/auth/checkapi")
@CrossOrigin("*")
public class AuthController {

    @Autowired
    private AdminService adminService;
    @Autowired
    private ManagerService managerService;
    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JWTUtil jwtUtil;


    @GetMapping("/")
        public String home(){
        return "Darum's Employee Management System is Running";
    }

    @PostMapping("/checklogin")
        public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest){
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        Admin admin = adminService.checkAdmin(email,password);
        Manager manager = managerService.checkManager(email,password);
        Employee employee = employeeService.checkEmployee(email,password);

        if(admin != null){
            String token = jwtUtil.generateToken(admin.getFullName(), "ADMIN");
            Map<String,Object> map = new HashMap<String, Object>();

            map.put("role","admin");
            map.put("message","Login Successful");
            map.put("token",token);
            map.put("data", admin);

            return ResponseEntity.ok(map);

        }

        if(manager != null){
            String token = jwtUtil.generateToken(manager.getFullName(), "MANAGER");
            Map<String,Object> map = new HashMap<String, Object>();

            map.put("role","manager");
            map.put("message","Login Successful");
            map.put("token",token);
            map.put("data", manager);

            return ResponseEntity.ok(map);

        }

        if(admin != null){
            String token = jwtUtil.generateToken(admin.getFullName(), "ADMIN");
            Map<String,Object> map = new HashMap<String, Object>();

            map.put("role","admin");
            map.put("message","Login Successful");
            map.put("token",token);
            map.put("data", admin);

            return ResponseEntity.ok(map);

        }
        if(employee != null){

            if(employee.getStatus().equalsIgnoreCase("Active")){
                String token = jwtUtil.generateToken(employee.getFullName(), "EMPLOYEE");
                Map<String,Object> map = new HashMap<String, Object>();

                map.put("role","employee");
                map.put("message","Login Successful");
                map.put("token",token);
                map.put("data", employee);

                return ResponseEntity.ok(map);
            }
            else{
                return ResponseEntity.status(401).body(Map.of("message","Access Denied\n\n" +
                        "Please contact the managers or adminstrators\n\n" +
                        "Current Account status: " + employee.getStatus()
                        ));
            }


        }

            return ResponseEntity.status(401).body(Map.of("message" , "Invalid Login details"));
    }
}
