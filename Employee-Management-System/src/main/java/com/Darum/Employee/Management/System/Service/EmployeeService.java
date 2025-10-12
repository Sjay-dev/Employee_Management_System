package com.Darum.Employee.Management.System.Service;

import com.Darum.Employee.Management.System.Entites.Department;
import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.Leave;

import java.time.LocalDate;
import java.util.List;

public interface EmployeeService {

    //Employees
    public Employee getEmployeeById(Long employeeId);

    public Employee getEmployeeByEmail(String email);

    public List<Employee> getAllEmployees();

    public List<Employee> getAllEmployeesByName(String name);

    public Employee updateEmployee(Long employeeId , Employee employee);

    public void deleteEmployee(Long employeeId);

    //Leave
    public Leave addLeave(Leave leave);

    public Leave getLeaveById(Long leaveId);

    public List<Leave> getLeavesByEmployeeId(Long employeeId);

    public Leave updateLeave(Long leaveId , Leave leave);






}
