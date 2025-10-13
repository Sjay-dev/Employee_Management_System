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

}
