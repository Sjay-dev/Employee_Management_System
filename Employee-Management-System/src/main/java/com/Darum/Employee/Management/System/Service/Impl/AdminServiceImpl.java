package com.Darum.Employee.Management.System.Service.Impl;

import com.Darum.Employee.Management.System.Model.*;
import com.Darum.Employee.Management.System.Repository.*;
import com.Darum.Employee.Management.System.Service.AdminService;
import com.Darum.Employee.Management.System.Service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.parameters.P;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

    @Value("${manager.default.password}")
    private String defaultPassword;
    private final BCryptPasswordEncoder passwordEncoder;  // Injected


    @Autowired
    private AdminRepository adminRepository;

    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private EmailRepository emailRepository;
    @Autowired
    private EmailService emailService;

    @Autowired
    private LeaveRepository  leaveRepository;


    @Override
    public Admin checkAdmin(String fullName, String password) {
        return adminRepository.findByFullNameAndPassword(fullName, password);
    }

    @Override
    public Manager addManager(Manager manager) {

        manager.setPassword(passwordEncoder.encode(defaultPassword));
        Manager savedManager = managerRepository.save(manager);

        Email email = new Email();
        String subject = "Welcome to Darum's Employee Management System";
        String message = "Hi " + manager.getFullName()
                + "\n\nYou have been added to the Darum's Employee Management System Sucessfully."
                + "Here are your details below:"
                + "\nYour Manager Id: " + manager.getManagerId()
                + "\nYour Full Name: " + manager.getFullName()
                + "\nYour Password: " + manager.getPassword();


            email.setRecipient(manager.getEmail());
            email.setSubject(subject);
            email.setBody(message);

            emailRepository.save(email);
            emailService.sendEmail(manager.getEmail(), subject, message);

            return savedManager;

    }

    @Override
    public List<Manager> findAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    public String deleteManager(Long managerId) {

        Optional<Manager> manager = managerRepository.findById((managerId));

        if(manager.isPresent()){
            managerRepository.deleteById(managerId);
            return "Manager Deleted";
        }
        else {
            return "Manager Not Found";
        }
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public String deleteEmployee(Long employeeId) {

        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if(employee.isPresent()){
            employeeRepository.deleteById(employeeId);
            return "Employee Deleted";
        }
        else{
            return "Employee Not Found";
        }

    }

    @Override
    public long numberOfManagers() {
        return managerRepository.count();
    }

    @Override
    public long numberOfEmployees() {
        return employeeRepository.count();
    }

    @Override
    public List<Leave> findAllLeaves() {
        return leaveRepository.findAll();
    }
}
