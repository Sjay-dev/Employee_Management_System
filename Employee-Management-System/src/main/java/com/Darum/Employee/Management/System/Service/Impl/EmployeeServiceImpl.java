package com.Darum.Employee.Management.System.Service.Impl;

import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.ResetToken;
import com.Darum.Employee.Management.System.Repository.EmployeeRepository;
import com.Darum.Employee.Management.System.Repository.ResetTokenRepository;
import com.Darum.Employee.Management.System.Service.EmployeeService;
import com.Darum.Employee.Management.System.Untils.JWTUtil;
import jakarta.transaction.Transactional;
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
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;


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
