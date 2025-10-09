package com.Darum.Employee.Management.System.Service.Impl;

import com.Darum.Employee.Management.System.Model.Admin;
import com.Darum.Employee.Management.System.Model.Employee;
import com.Darum.Employee.Management.System.Model.Leave;
import com.Darum.Employee.Management.System.Model.Manager;
import com.Darum.Employee.Management.System.Repository.AdminRepository;
import com.Darum.Employee.Management.System.Service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AdminServiceImpl implements AdminService {

    @Autowired
    private AdminRepository adminRepository;

    @Override
    public Admin checkAdmin(String fullName, String password) {
        return adminRepository.findByFullNameAndPassword(fullName, password);
    }

    @Override
    public Manager addManager(Manager manager) {
        return null;
    }

    @Override
    public List<Manager> viewAllManagers() {
        return List.of();
    }

    @Override
    public String deleteManager() {
        return "";
    }

    @Override
    public List<Employee> viewAllEmployees() {
        return List.of();
    }

    @Override
    public String deleteEmployee() {
        return "";
    }

    @Override
    public long numberOfManagers() {
        return 0;
    }

    @Override
    public long numberOfEmployees() {
        return 0;
    }

    @Override
    public List<Leave> viewAllLeaves() {
        return List.of();
    }
}
