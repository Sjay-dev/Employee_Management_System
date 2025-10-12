package com.Darum.Employee.Management.System.Service.Impl;

import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Event.Enum.Event;
import com.Darum.Employee.Management.System.Event.KafkaEvent;
import com.Darum.Employee.Management.System.Repository.EmployeeRepository;
import com.Darum.Employee.Management.System.Service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;


@RequiredArgsConstructor
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;

    private final KafkaTemplate<String,Object> kafka;
    public static final String TOPIC = "Employee.events";

     @Override
    public Employee addEmployee(Employee employee) {

        Employee saved = employeeRepository.save(employee);
        kafka.send(TOPIC , new KafkaEvent<Employee>(Event.CREATE, saved));
        return employeeRepository.save(saved);
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

        kafka.send(TOPIC , new KafkaEvent<Employee>(Event.UPDATE, existingEmployee));
        return employeeRepository.save(existingEmployee);
    }

    @Override
    public void deleteEmployee(Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        employeeRepository.delete(employee);

        kafka.send(TOPIC , new KafkaEvent<Employee>(Event.DELETE, employee));

    }



}
