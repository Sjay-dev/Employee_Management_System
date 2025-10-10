package com.Darum.Employee.Management.System.Service;

import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.Manager;

import java.util.List;

public interface ManagerService {

    //Manager

    public Manager getManagerById(Long managerId);

    public List<Manager> getManagerByName(String name);

    public Manager getManagerByEmail(String managerEmail);

    public List<Manager> getAllManagers();

    public Manager updateManager(Long managerId , Manager manager);

    public void deleteManager(Long managerId);


    //Employees
    public Employee addEmployee(Employee employee);

    public Employee getEmployeeById(Long employeeId);

    public Employee getEmployeeByEmail(String email);

    public List<Employee> getAllEmployees();

    public List<Employee> getAllEmployeesByName(String name);

    public Employee updateEmployee(Long employeeId , Employee employee);

    public void deleteEmployee(Long employeeId);




}
