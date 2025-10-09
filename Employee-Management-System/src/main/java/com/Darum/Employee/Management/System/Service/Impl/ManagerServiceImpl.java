package com.Darum.Employee.Management.System.Service.Impl;

import com.Darum.Employee.Management.System.Model.Employee;
import com.Darum.Employee.Management.System.Model.Manager;
import com.Darum.Employee.Management.System.Model.ResetToken;
import com.Darum.Employee.Management.System.Repository.EmployeeRepository;
import com.Darum.Employee.Management.System.Repository.ManagerRepository;
import com.Darum.Employee.Management.System.Repository.ResetTokenRepository;
import com.Darum.Employee.Management.System.Service.ManagerService;
import com.Darum.Employee.Management.System.Untils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ManagerServiceImpl implements ManagerService {

    private final JWTUtil jwtUtil;  // <-- this is your "jwtUtil" variable


    @Autowired
    private ManagerRepository managerRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    @Override
    public Manager checkManager(String fullName, String password) {
        return managerRepository.findManagerByFullNameAndPassword(fullName,password);
    }

    @Override
    public Manager findManagerById(Long managerId) {
        return managerRepository.findById(managerId).get();
    }


    @Override
    public Manager findManagerByName(String managerFullName) {
        return managerRepository.findManagerByFullName(managerFullName);
    }

    @Override
    public Manager findManagerByEmail(String managerEmail) {
        return managerRepository.findManagerByEmail(managerEmail);
    }

    @Override
    public List<Manager> findAllManagers() {
        return managerRepository.findAll();
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public String updateEmployeeStatus(Long employeeId, String status) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if (employee.isPresent()) {
            Employee emp = new Employee();

            emp.setStatus(status);
            employeeRepository.save(emp);

            return "Employee status successfully  updated";
        }
        else {
            return "Employee not found";
        }

    }

    @Override
    public String updateEmployeeDepartment(Long employeeId, String department) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if (employee.isPresent()) {
            Employee emp = new Employee();

            emp.setDepartment(department);
            employeeRepository.save(emp);

            return "Employee department successfully  updated";
        }
        else {
            return "Employee not found";
        }    }

    @Override
    public String updateEmployeePosition(Long employeeId, String position) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if (employee.isPresent()) {
            Employee emp = new Employee();

            emp.setPosition(position);
            employeeRepository.save(emp);

            return "Employee position successfully  updated";
        }
        else {
            return "Employee not found";
        }    }

    @Override
    public String updateEmployeeEmploymentType(Long employeeId, String employmentType) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if (employee.isPresent()) {
            Employee emp = new Employee();

            emp.setEmploymentType(employmentType);
            employeeRepository.save(emp);

            return "Employee employment type successfully  updated";
        }
        else {
            return "Employee not found";
        }    }

    @Override
    public String updateEmployeeSalary(Long employeeId, String salary) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if (employee.isPresent()) {
            Employee emp = new Employee();

            emp.setEmploymentType(salary);
            employeeRepository.save(emp);

            return "Employee salary successfully  updated";
        }
        else {
            return "Employee not found";
        }    }

    @Override
    public String updateEmployeeHireDate(Long employeeId, LocalDate hireDate) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if (employee.isPresent()) {
            Employee emp = new Employee();

            emp.setHireDate(hireDate);
            employeeRepository.save(emp);

            return "Employee hireDate successfully  updated";
        }
        else {
            return "Employee not found";
        }
    }

    @Override
    public String generateResetPasswordToken(String email) {

        Optional<Manager> managerOpt = managerRepository.findManagerByEmailOpt(email);

        if(managerOpt.isPresent()) {
            String token = jwtUtil.generateToken(email, 15);

            ResetToken resetToken = new ResetToken();
            resetToken.setToken(token);
            resetToken.setEmail(email);
            resetToken.setCreatedAt(LocalDateTime.now());
            resetToken.setExpiresAt(LocalDateTime.now().plusMinutes(15));

            resetTokenRepository.save(resetToken);

            return token;
        }

        return null;
    }


    @Override
    public boolean validateResetPasswordToken(String token) {

        Optional<ResetToken>  resetToken = resetTokenRepository.findByToken(token);
        return resetToken.isPresent() && !isTokenExpired(token);

    }

    @Override
    public boolean changePassword(Manager manager, String oldPassword, String newPassword) {

        if(manager.getPassword().equals(oldPassword)) {
            manager.setPassword(newPassword);
            managerRepository.save(manager);
            return true;
        }
        else {
            return false;
        }
    }

    @Override
    public void updateEmployeePassword(String token, String password) {

        Optional<ResetToken> resetToken = resetTokenRepository.findByToken(token);
        if (resetToken.isPresent() &&  !isTokenExpired(token)) {
            Manager manager = new Manager();
            manager.setPassword(password);
            managerRepository.save(manager);
            deleteResetPasswordToken(token);
        }
    }

    @Override
    public void deleteResetPasswordToken(String token)
    {
            resetTokenRepository.deleteByToken(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        Optional<ResetToken> resetToken = resetTokenRepository.findByToken(token);

        if (resetToken.isPresent()) {
            resetToken.get().getExpiresAt().isBefore(LocalDateTime.now());
        }

        return true;

    }

}
