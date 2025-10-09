package com.Darum.Employee.Management.System.Service.Impl;

import com.Darum.Employee.Management.System.Model.Employee;
import com.Darum.Employee.Management.System.Model.ResetToken;
import com.Darum.Employee.Management.System.Repository.EmployeeRepository;
import com.Darum.Employee.Management.System.Repository.ResetTokenRepository;
import com.Darum.Employee.Management.System.Service.EmployeeService;
import com.Darum.Employee.Management.System.Untils.JWTUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class EmployeeServiceImpl implements EmployeeService {

    private final JWTUtil jwtUtil;

    @Value("${default.password}")
    private String defaultPassword;


    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private ResetTokenRepository resetTokenRepository;

    @Override
    public Employee checkEmployee(String fullName, String password) {
        return employeeRepository.findEmployeeByFullNameAndPassword(fullName , password);
    }

    @Override
    public String registerEmployee(Employee employee) {

            employee.setPassword(defaultPassword);
            employee.setStatus("Pending");

            employeeRepository.save(employee);
                return "Employee registered successfully";
    }

    @Override
    public String updateEmployee(Employee employee) {
        employeeRepository.save(employee);
        return "Employee updated successfully";
    }

    @Override
    public Employee findEmployeeById(Long id) {

        return employeeRepository.findById(id).orElse(null);
    }

    @Override
    public Employee findEmployeeByName(String fullName) {
        return employeeRepository.findEmployeeByName(fullName);
    }

    @Override
    public Employee  findEmployeeByEmail(String email) {
        return employeeRepository.findEmployeeByEmail(email);
    }

    @Override
    public List<Employee> findAllEmployees() {
        return employeeRepository.findAll();
    }

    @Override
    public String updatePhoneNumber(Long employeeId, String phoneNumber) {

        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if (employee.isPresent()) {
            Employee emp = new Employee();

                emp.setPhoneNumber(phoneNumber);
                return "Employee's phone number updated successfully";
        }
        else {
            return "Employee not found";
        }

        }

    @Override
    public String updateDOB(Long employeeId, LocalDate DOB) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if (employee.isPresent()) {
            Employee emp = new Employee();

            emp.setDateOfBirth(DOB);
            return "Employee's date of birth updated successfully";
        }
        else {
            return "Employee not found";
        }
    }

    @Override
    public String updateAddress(Long employeeId, String address) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if (employee.isPresent()) {
            Employee emp = new Employee();

            emp.setAddress(address);
            return "Employee's address updated successfully";
        }
        else {
            return "Employee not found";
        }
    }

    @Override
    public String updateGender(Long employeeId, String gender) {
        Optional<Employee> employee = employeeRepository.findById(employeeId);

        if (employee.isPresent()) {
            Employee emp = new Employee();

            emp.setGender(gender);
            return "Employee's gender updated successfully";
        }
        else {
            return "Employee not found";
        }
    }

    @Override
    public String generateResetPasswordToken(String email) {

        Optional<Employee> employeeOpt = employeeRepository.findEmployeeByEmailOpt(email);

        if(employeeOpt.isPresent()) {
            String token = jwtUtil.generatePasswordToken(email, 15);

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
    public boolean changePassword(Employee employee, String oldPassword, String newPassword) {

        if(employee.getPassword().equals(oldPassword)) {
            employee.setPassword(newPassword);
            employeeRepository.save(employee);
            return true;
        }
        else {
            return false;
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
