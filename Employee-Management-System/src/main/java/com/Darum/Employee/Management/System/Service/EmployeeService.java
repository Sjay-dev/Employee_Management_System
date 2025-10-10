package com.Darum.Employee.Management.System.Service;

import com.Darum.Employee.Management.System.Entites.Employee;

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







}
