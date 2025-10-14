package com.Darum.Employee.Management.System.Service;

import com.Darum.Employee.Management.System.Entites.Employee;
import org.springframework.stereotype.Service;

import java.util.List;

public interface EmployeeService {

    public Employee addEmployee(Employee employee);

    public Employee getEmployeeById(Long employeeId);

    public Employee getEmployeeByEmail(String email);

    public List<Employee> getAllEmployees();

    public List<Employee> getAllEmployeesByName(String name);

    public List<Employee> getAllEmployeesByDepartmentId(Long departmentId);

    public List<Employee> getAllEmployeesByManagerId(Long managerId);

    public Employee updateEmployee(Long employeeId , Employee employee);

    public void deleteEmployee(Long employeeId);

}
