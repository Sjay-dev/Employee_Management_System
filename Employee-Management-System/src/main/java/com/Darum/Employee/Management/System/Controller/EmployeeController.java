package com.Darum.Employee.Management.System.Controller;


import com.Darum.Employee.Management.System.Model.Employee;
import com.Darum.Employee.Management.System.Model.Leave;
import com.Darum.Employee.Management.System.Service.EmployeeService;
import com.Darum.Employee.Management.System.Service.LeaveService;
import com.Darum.Employee.Management.System.Untils.JWTUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/employee")
@CrossOrigin("*")
public class EmployeeController {

    @Autowired
    private JWTUtil jwtUtil;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private LeaveService leaveService;

    private boolean isAuthorized(String authHeader , String expectedRole){
        try {
            String token = authHeader.substring(7);
            String role = jwtUtil.validateToken(token).get("role").toString();

            return role.equals(expectedRole);
        } catch (Exception e) {
            return false;
        }
    }

    @PostMapping(value = "/addEmployee")
        public ResponseEntity<String> addEmployee(
                @RequestParam String fullName,
                @RequestParam String password,
                @RequestParam String email,
                @RequestParam String phoneNumber,
                @RequestParam String status,
                @RequestParam String department,
                @RequestParam String position,
                @RequestParam String employmentType,
                @RequestParam String salary,
                @RequestParam LocalDate dateOfBirth,
                @RequestParam LocalDate hireDate,
                @RequestParam String address,
                @RequestParam String gender
    ){
            try {
                Employee employee = new Employee();
                employee.setFullName(fullName);
                employee.setPassword(password);
                employee.setEmail(email);
                employee.setPhoneNumber(phoneNumber);
                employee.setStatus(status);
                employee.setDepartment(department);
                employee.setPosition(position);
                employee.setEmploymentType(employmentType);
                employee.setSalary(salary);
                employee.setDateOfBirth(dateOfBirth);
                employee.setHireDate(hireDate);
                employee.setAddress(address);
                employee.setGender(gender);

                return ResponseEntity.ok(employeeService.registerEmployee(employee));
            }
                catch (Exception e){
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An Error occurred while registering employee");
                }
    }

    @GetMapping("/viewProfile")
    public ResponseEntity<Employee> viewProfile(@RequestParam Long employeeId , @RequestHeader("Authorization")  String authHeader) {
        if(!isAuthorized(authHeader,"EMPLOYEE")){
            return ResponseEntity.status(403).body(null);
        }
        return ResponseEntity.ok(employeeService.findEmployeeById(employeeId));
    }

    @PostMapping("/applyForLeave")
    public ResponseEntity<Leave> applyForLeave(@RequestBody Leave leave,
                                               @RequestParam Long employeeId,
                                               @RequestHeader("Authorization")  String authHeader) {

        String token = authHeader.substring(7);

        if(!jwtUtil.validateToken(token).get("role").equals("EMPLOYEE")){
            return ResponseEntity.status(403).body(null);
        }
        Leave leaveResponse = leaveService.leaveApplicationByEmployee(leave, employeeId);
        return ResponseEntity.ok(leaveResponse);
    }

    @GetMapping("/viewOwnLeaves")
    public ResponseEntity<List<Leave>> viewOwnLeaves(@RequestParam Long employeeId , @RequestHeader("Authorization")  String authHeader) {

        String token = authHeader.substring(7);

        if(!jwtUtil.validateToken(token).get("role").equals("EMPLOYEE")) {
            return ResponseEntity.status(403).body(null);
        }
        List<Leave> leaveResponse = leaveService.leavesByEmployee(employeeId);
        return ResponseEntity.ok(leaveResponse);


    }
}
