package com.Darum.Employee.Management.System.Controller;

import com.Darum.Employee.Management.System.DTO.LoginRequest;
import com.Darum.Employee.Management.System.Entites.Admin;
import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.Manager;
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


}
