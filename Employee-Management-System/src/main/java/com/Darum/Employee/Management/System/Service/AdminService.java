package com.Darum.Employee.Management.System.Service;

import com.Darum.Employee.Management.System.Model.Admin;
import com.Darum.Employee.Management.System.Model.Employee;
import com.Darum.Employee.Management.System.Model.Leave;
import com.Darum.Employee.Management.System.Model.Manager;

import java.util.List;

public interface AdminService {

    public Admin checkAdmin(String fullName , String password);

    public Manager addManager(Manager manager);
    public List<Manager> viewAllManagers();
    public String deleteManager();

    public List<Employee> viewAllEmployees();
    public String deleteEmployee();

    public long numberOfManagers();
    public long numberOfEmployees();

    public List<Leave> viewAllLeaves();

}
