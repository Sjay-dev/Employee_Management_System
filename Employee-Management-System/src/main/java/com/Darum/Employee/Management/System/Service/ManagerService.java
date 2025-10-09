package com.Darum.Employee.Management.System.Service;

import com.Darum.Employee.Management.System.Model.Employee;
import com.Darum.Employee.Management.System.Model.Manager;

import java.time.LocalDate;
import java.util.List;

public interface ManagerService {

    public Manager checkManager(String fullName, String password);

    public Manager findManagerById(Long managerId);

    public Manager findManagerByName(String managerFullName);
    public Manager findManagerByEmail(String managerEmail);
    public List<Manager> findAllManagers();

    public List<Employee> findAllEmployees();

    public String updateEmployeeStatus(Long employeeId, String status);
    public String updateEmployeeDepartment(Long employeeId, String department);
    public String updateEmployeePosition(Long employeeId, String position);
    public String updateEmployeeEmploymentType(Long employeeId, String employmentType);
    public String updateEmployeeSalary(Long employeeId, String salary);
    public String updateEmployeeHireDate(Long employeeId, LocalDate hireDate);


    public String generateResetPasswordToken(String email);
    public boolean validateResetPasswordToken(String token);
    public boolean changePassword(Manager manager, String oldPassword, String newPassword);
    public void updateEmployeePassword(String token, String password);
    public void deleteResetPasswordToken(String token);
    public boolean isTokenExpired(String token);


}
