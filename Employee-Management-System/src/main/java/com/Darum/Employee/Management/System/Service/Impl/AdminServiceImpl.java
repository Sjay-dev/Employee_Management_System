package com.Darum.Employee.Management.System.Service.Impl;

import com.Darum.Employee.Management.System.Entites.*;
import com.Darum.Employee.Management.System.Repository.*;
import com.Darum.Employee.Management.System.Service.AdminService;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
@Transactional
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final ManagerRepository managerRepository;
    private final EmployeeRepository employeeRepository;
    private final UserRepository userRepository;
    private final DepartmentRepository departmentRepository;

    private final KafkaTemplate<String,Object> kafka;
    public static final String TOPIC = "Employee.events";

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
        return adminRepository.findAdminByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    @Override
    public List<Admin> getAdminByName(String name) {
        return adminRepository.findAdminByName(name);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public List<User> getAllUsers() {return userRepository.findAll(); }

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
        return managerRepository.findManagerByEmail(email).orElseThrow(() -> new RuntimeException("Manager not found"));
    }

    @Override
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    public Manager getManagerByDepartmentId(Long departmentId) {
        return null;
    }

    @Override
    public List<Manager> getAllManagersByName(String name) {
        return managerRepository.findManagerByName(name);
    }

    @Override
    public Manager updateManager(Long managerId, Manager managerDetails) {
        Manager existingManager = managerRepository.findById(managerId)
                .orElseThrow(() -> new RuntimeException("Manager not found with ID: " + managerId));

        if (managerDetails.getFirstName() != null)
            existingManager.setFirstName(managerDetails.getFirstName());

        if (managerDetails.getLastName() != null)
            existingManager.setLastName(managerDetails.getLastName());

        if (managerDetails.getEmail() != null)
            existingManager.setEmail(managerDetails.getEmail());

        if (managerDetails.getPassword() != null)
            existingManager.setPassword(managerDetails.getPassword());

        if (managerDetails.getPhoneNumber() != null)
            existingManager.setPhoneNumber(managerDetails.getPhoneNumber());

        if (managerDetails.getRole() != null)
            existingManager.setRole(managerDetails.getRole());

        if (managerDetails.getDepartment() != null)
            existingManager.setDepartment(managerDetails.getDepartment());

        if (managerDetails.getPosition() != null)
            existingManager.setPosition(managerDetails.getPosition());

        if (managerDetails.getStatus() != null)
            existingManager.setStatus(managerDetails.getStatus());

        if (managerDetails.getEmploymentDate() != null)
            existingManager.setEmploymentDate(managerDetails.getEmploymentDate());

        if (managerDetails.getEmploymentType() != null)
            existingManager.setEmploymentType(managerDetails.getEmploymentType());

        if (managerDetails.getSalary() != null)
            existingManager.setSalary(managerDetails.getSalary());

        if (managerDetails.getAddress() != null)
            existingManager.setAddress(managerDetails.getAddress());

        if (managerDetails.getGender() != null)
            existingManager.setGender(managerDetails.getGender());

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
        return employeeRepository.findEmployeeByEmail(email).orElseThrow(() -> new RuntimeException("Employee not found"));
    }

    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public List<Employee> getAllEmployeesByName(String name) {
        return employeeRepository.findEmployeeByName(name);
    }

    @Override
    public List<Employee> getAllEmployeesByDepartmentId(Long departmentId) {
        return List.of();
    }

    @Override
    public List<Employee> getAllEmployeesByManagerId(Long managerId) {
        return List.of();
    }

    @Override
    public Employee updateEmployee(Long employeeId, Employee employeeDetails) {
        Employee existingEmployee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found with ID: " + employeeId));

        if (employeeDetails.getFirstName() != null)
            existingEmployee.setFirstName(employeeDetails.getFirstName());

        if (employeeDetails.getLastName() != null)
            existingEmployee.setLastName(employeeDetails.getLastName());

        if (employeeDetails.getEmail() != null)
            existingEmployee.setEmail(employeeDetails.getEmail());

        if (employeeDetails.getPhoneNumber() != null)
            existingEmployee.setPhoneNumber(employeeDetails.getPhoneNumber());

        if (employeeDetails.getDepartment() != null)
            existingEmployee.setDepartment(employeeDetails.getDepartment());

        if (employeeDetails.getPosition() != null)
            existingEmployee.setPosition(employeeDetails.getPosition());

        if (employeeDetails.getEmploymentType() != null)
            existingEmployee.setEmploymentType(employeeDetails.getEmploymentType());

        if (employeeDetails.getSalary() != null)
            existingEmployee.setSalary(employeeDetails.getSalary());

        if (employeeDetails.getDateOfBirth() != null)
            existingEmployee.setDateOfBirth(employeeDetails.getDateOfBirth());

        if (employeeDetails.getAddress() != null)
            existingEmployee.setAddress(employeeDetails.getAddress());

        if (employeeDetails.getRole() != null)
            existingEmployee.setRole(employeeDetails.getRole());

        if (employeeDetails.getGender() != null)
            existingEmployee.setGender(employeeDetails.getGender());

        if (employeeDetails.getPassword() != null)
            existingEmployee.setPassword(employeeDetails.getPassword());

        return employeeRepository.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {
        if (!employeeRepository.existsById(employeeId)) {
            throw new RuntimeException("Employee not found");
        }
        employeeRepository.deleteById(employeeId);
    }

    @Override
    public Department addDepartment(Department department) {
        return departmentRepository.save(department) ;
    }

    @Override
    public Department getDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId).orElseThrow(() -> new RuntimeException("Department not found"));
    }

    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    @Override
    public List<Department> getAllDepartmentsByName(String name) {
        return departmentRepository.findDepartmentByByName(name);
    }

    @Override
    public Department updateDepartmentDetails(Long departmentId, Department department) {

        Department existingDepartment = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found with ID: " + departmentId));

        if (department.getName() != null)
            existingDepartment.setName(department.getName());

        if (department.getDescription() != null)
            existingDepartment.setDescription(department.getDescription());

        return departmentRepository.save(existingDepartment);


    }

    @Override
    public void deleteDepartment(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new RuntimeException("Department not found");
        }
        departmentRepository.deleteById(departmentId);    }
}

