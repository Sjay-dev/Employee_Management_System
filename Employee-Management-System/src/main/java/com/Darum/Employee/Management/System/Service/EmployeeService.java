package com.Darum.Employee.Management.System.Service;

import com.Darum.Employee.Management.System.Model.Employee;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {

    public Employee checkEmployee(String fullName, String password);

    public String registerEmployee(Employee employee);
    public String updateEmployee(Employee employee);

    public Employee findEmployeeById(Long id);
     public Employee findEmployeeByName(String fullName);
    public Employee  findEmployeeByEmail(String email);
    public List<Employee> findAllEmployees();

    public String updatePhoneNumber(Long employeeId , String phoneNumber);
    public String updateDOB(Long employeeId , LocalDate DOB);
    public String updateAddress(Long employeeId , String address);
    public String updateGender(Long employeeId , String gender);

    public String generateResetPasswordToken(String email);
    public boolean validateResetPasswordToken(String token);
    public boolean changePassword(Employee employee, String oldPassword, String newPassword);
    public void deleteResetPasswordToken(String token);
    public boolean isTokenExpired(String token);





}
