package com.Darum.Employee.Management.System.Service;

import com.Darum.Employee.Management.System.Entites.Manager;
import org.springframework.stereotype.Service;

import java.util.List;


public interface ManagerService {

    public Manager addManager(Manager manager);

    public List<Manager> getAllManagersByName(String name);

    public Manager getManagerById(Long managerId);

    public Manager getManagerByEmail(String email);

    public List<Manager> getAllManagers();

    public Manager getManagerByDepartmentId(Long departmentId);

    public Manager updateManager(Long managerId , Manager manager);

    public void deleteManager(Long managerId);

}
