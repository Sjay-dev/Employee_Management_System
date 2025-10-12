package com.Darum.Employee.Management.System.Service;

import com.Darum.Employee.Management.System.Entites.Department;
import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.Leave;
import com.Darum.Employee.Management.System.Entites.Manager;

import java.time.LocalDate;
import java.util.List;

public interface ManagerService {

    //Manager

    public List<Manager> getAllManagersByName(String name);

    public Manager getManagerById(Long managerId);

    public Manager getManagerByEmail(String email);

    public List<Manager> getAllManagers();

    public Manager getManagerByDepartmentId(Long departmentId);

    public Manager updateManager(Long managerId , Manager manager);


    //Employees
    public Employee addEmployee(Employee employee);

    public Employee getEmployeeById(Long employeeId);

    public Employee getEmployeeByEmail(String email);

    public List<Employee> getAllEmployees();

    public List<Employee> getAllEmployeesByName(String name);

    public List<Employee> getAllEmployeesByDepartmentId(Long departmentId);

    public List<Employee> getAllEmployeesByManagerId(Long managerId);

    public Employee updateEmployee(Long employeeId , Employee employee);

    //Department
    public Department addDepartment(Department department);

    public Department getDepartmentById(Long departmentId);

    public List<Department> getAllDepartments();

    public List<Department> getAllDepartmentsByName(String name);

    public Employee updateDepartmentDetails(Long departmentId , Department department);

    public void deleteDepartment(Long departmentId);


    //Leave
    public  Leave addLeave(Leave leave);

    public Leave getLeaveById(Long leaveId);

    public List<Leave> getLeavesByManagerId(Long managerId);

    public List<Leave> getLeavesByEmployeeId(Long employeeId);

    public List<Leave> getLeavesByStartDate(LocalDate startDate);

    public List<Leave> getLeavesByEndDate(LocalDate endDate);

    public List<Leave> getAllLeaves();

    public Leave updateLeave(Long leaveId , Leave leave);

    public void deleteLeave(Long leaveId);


}
