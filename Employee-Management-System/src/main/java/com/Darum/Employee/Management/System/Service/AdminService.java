package com.Darum.Employee.Management.System.Service;

import com.Darum.Employee.Management.System.Model.Admin;
import com.Darum.Employee.Management.System.Model.Employee;
import com.Darum.Employee.Management.System.Model.Leave;
import com.Darum.Employee.Management.System.Model.Manager;

import java.util.List;

public interface AdminService {

    public Admin checkAdmin(String fullName , String password);

    public Manager addManager(Manager manager);
    public List<Manager> findAllManagers();
    public String deleteManager(Long managerId);

    public List<Employee> findAllEmployees();
    public String deleteEmployee(Long employeeId);

    public long numberOfManagers();
    public long numberOfEmployees();

    public List<Leave> findAllLeaves();

}
