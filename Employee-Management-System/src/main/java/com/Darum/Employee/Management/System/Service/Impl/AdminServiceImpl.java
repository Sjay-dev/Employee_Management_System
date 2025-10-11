package com.Darum.Employee.Management.System.Service.Impl;

import com.Darum.Employee.Management.System.Entites.*;
import com.Darum.Employee.Management.System.Repository.*;
import com.Darum.Employee.Management.System.Service.AdminService;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final ManagerRepository managerRepository;
    private final EmployeeRepository employeeRepository;

    // ------------------ Admin CRUD ------------------


    @Override
    public Admin addAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Admin getAdminById(Long adminId) {
        return adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    @Override
    public Admin getAdminByEmail(String email) {
        return adminRepository.findAdminByEmail(email);
    }

    @Override
    public List<Admin> getAdminByName(String name) {
        return adminRepository.getAdminByName(name);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public Admin updateAdmin(Long adminId, Admin adminDetails) {
        Admin existingAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));

        if (adminDetails.getFirstName() != null)
            existingAdmin.setFirstName(adminDetails.getFirstName());

        if (adminDetails.getLastName() != null)
            existingAdmin.setLastName(adminDetails.getLastName());

        if (adminDetails.getEmail() != null)
            existingAdmin.setEmail(adminDetails.getEmail());

        if (adminDetails.getPassword() != null)
            existingAdmin.setPassword(adminDetails.getPassword());

        if (adminDetails.getRole() != null)
            existingAdmin.setRole(adminDetails.getRole());

        return adminRepository.save(existingAdmin);
    }


    @Override
    public void deleteAdmin(Long adminId) {
        if (!adminRepository.existsById(adminId)) {
            throw new RuntimeException("Admin not found");
        }
        adminRepository.deleteById(adminId);
    }

    // ------------------ Manager CRUD ------------------

    @Override
    public Manager addManager(Manager manager) {
        return managerRepository.save(manager);
    }

    @Override
    public Manager getManagerById(Long managerId) {
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));
    }

    @Override
    public Manager getManagerByEmail(String email) {
        return managerRepository.getManagerByEmail(email);
    }

    @Override
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    public List<Manager> getAllManagersByName(String name) {
        return managerRepository.getManagerByName(name);
    }

    @Override
    public Manager updateManager(Long managerId, Manager manager) {
        Manager existingManager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found"));

        existingManager.setFirstName(manager.getFirstName());
        existingManager.setLastName(manager.getLastName());
        existingManager.setEmail(manager.getEmail());
        existingManager.setDepartment(manager.getDepartment());
        existingManager.setPassword(manager.getPassword());
        existingManager.setPhoneNumber(manager.getPhoneNumber());
        existingManager.setRole(manager.getRole());
        existingManager.setDepartment(manager.getDepartment());
        existingManager.setPosition(existingManager.getPosition());

        return managerRepository.save(existingManager);
    }

    @Override
    public void deleteManager(Long managerId) {
        if (!managerRepository.existsById(managerId)) {
            throw new RuntimeException("Manager not found");
        }
        managerRepository.deleteById(managerId);
    }

    // ------------------ Employee CRUD ------------------

    @Override
    public Employee addEmployee(Employee employee) {
        return employeeRepository.save(employee);
    }

    @Override
    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Override
    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.getEmployeeByEmail(email);
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> getAllEmployeesByName(String name) {
        return employeeRepository.getEmployeeByName(name);
    }

    @Override
    public Employee updateEmployee(Long employeeId, Employee employee) {
        Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));

        existingEmployee.setFirstName(employee.getFirstName());
        existingEmployee.setLastName(employee.getLastName());
        existingEmployee.setEmail(employee.getEmail());
        existingEmployee.setPhoneNumber(employee.getPhoneNumber());
        existingEmployee.setDepartment(employee.getDepartment());
        existingEmployee.setPhoneNumber(employee.getPhoneNumber());
        existingEmployee.setPosition(employee.getPosition());
        existingEmployee.setEmploymentType(employee.getEmploymentType());
        existingEmployee.setSalary(employee.getSalary());
        existingEmployee.setDateOfBirth(employee.getDateOfBirth());
        existingEmployee.setAddress(employee.getAddress());
        existingEmployee.setRole(employee.getRole());
        existingEmployee.setHireDate(employee.getHireDate());
        existingEmployee.setGender(employee.getGender());
        existingEmployee.setPassword(employee.getPassword());


        return employeeRepository.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found");
        }
        employeeRepository.deleteById(employeeId);
    }
}

