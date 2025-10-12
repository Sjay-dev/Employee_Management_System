package com.Darum.Employee.Management.System.Service;

import com.Darum.Employee.Management.System.Entites.*;

import java.util.List;

public interface AdminService {

    //Admin
    public Admin addAdmin(Admin admin);

    public Admin getAdminById(Long adminId);

    public Admin getAdminByEmail(String email);

    public List<Admin> getAdminByName(String name);

    public List<Admin> getAllAdmins();

    public List<User> getAllUsers();

    public Admin updateAdmin(Long adminId , Admin admin);

    public void deleteAdmin(Long adminId);


    //Managers
    public Manager addManager(Manager manager);

    public List<Manager> getAllManagersByName(String name);

    public Manager getManagerById(Long managerId);

    public Manager getManagerByEmail(String email);

    public List<Manager> getAllManagers();

    public Manager getManagerByDepartmentId(Long departmentId);

    public Manager updateManager(Long managerId , Manager manager);

    public void deleteManager(Long managerId);


    //Employees
    public Employee addEmployee(Employee employee);

    public Employee getEmployeeById(Long employeeId);

    public Employee getEmployeeByEmail(String email);

    public List<Employee> getAllEmployees();

    public List<Employee> getAllEmployeesByName(String name);

    public List<Employee> getAllEmployeesByDepartmentId(Long departmentId);

    public List<Employee> getAllEmployeesByManagerId(Long managerId);

    public Employee updateEmployee(Long employeeId , Employee employee);

    public void deleteEmployee(Long employeeId);

    //Department
    public Department addDepartment(Department department);

    public Department getDepartmentById(Long departmentId);

    public List<Department> getAllDepartments();

    public List<Department> getAllDepartmentsByName(String name);

    public Department updateDepartmentDetails(Long departmentId , Department department);

    public void deleteDepartment(Long departmentId);

}
