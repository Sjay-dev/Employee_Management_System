package com.Darum.Employee.Management.System.Service.Impl;

import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Event.Enum.Event;
import com.Darum.Employee.Management.System.Event.KafkaEvent;
import com.Darum.Employee.Management.System.Repository.DepartmentRepository;
import com.Darum.Employee.Management.System.Repository.EmployeeRepository;
import com.Darum.Employee.Management.System.Repository.ManagerRepository;
import com.Darum.Employee.Management.System.Service.EmployeeService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;


@RequiredArgsConstructor
@Service
@Transactional
public class EmployeeServiceImpl implements EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final KafkaTemplate<String, Object> kafka;
    public static final String TOPIC = "Employee.events";

    // ------------------------- EMPLOYEE CRUD -------------------------

    /**
     * Add a new employee.
     * Sends a Kafka CREATE event to notify other services.
     */
    @Override
    public Employee addEmployee(Employee employee) {
        Employee saved = employeeRepository.save(employee);

//        try {
//            kafka.send(TOPIC, new KafkaEvent<>(Event.CREATE, saved));
//        } catch (Exception e) {
//            System.err.println("Failed to send Kafka event for employee: " + e.getMessage());
//        }

        return saved;
    }

    /**
     * Get an employee by ID.
     */
    @Override
    public Employee getEmployeeById(Long employeeId) {
        return employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Employee not found with ID: " + employeeId
                ));
    }

    /**
     * Get an employee by email.
     */
    @Override
    public Employee getEmployeeByEmail(String email) {
        return employeeRepository.findEmployeeByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Employee not found with email: " + email
                ));
    }

    /**
     * Get all employees.
     */
    @Override
    public List<Employee> getAllEmployees() {
        return employeeRepository.findAll();
    }

    /**
     * Get all employees by name.
     */
    @Override
    public List<Employee> getAllEmployeesByName(String name) {
        List<Employee> employees = employeeRepository.findEmployeeByName(name);
        if (employees.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No employees found with name: " + name
            );
        }
        return employees;
    }

    /**
     * Get all employees in a specific department.
     */
    @Override
    public List<Employee> getAllEmployeesByDepartmentId(Long departmentId) {
        return employeeRepository.findByDepartment_DepartmentId(departmentId);
    }

    /**
     * Get all employees under a specific manager.
     */
    @Override
    public List<Employee> getAllEmployeesByManagerId(Long managerId) {
        return employeeRepository.findByManager_UserId(managerId);
    }

    /**
     * Update an existing employee's details.
     * Sends a Kafka UPDATE event to notify other services.
     */
    @Override
    public Employee updateEmployee(Long employeeId, Employee employeeDetails) {
        Employee existingEmployee = getEmployeeById(employeeId);

        if (employeeDetails.getFirstName() != null) existingEmployee.setFirstName(employeeDetails.getFirstName());
        if (employeeDetails.getLastName() != null) existingEmployee.setLastName(employeeDetails.getLastName());
        if (employeeDetails.getEmail() != null) existingEmployee.setEmail(employeeDetails.getEmail());
        if (employeeDetails.getPhoneNumber() != null) existingEmployee.setPhoneNumber(employeeDetails.getPhoneNumber());
        if (employeeDetails.getDepartment() != null) existingEmployee.setDepartment(employeeDetails.getDepartment());
        if (employeeDetails.getPosition() != null) existingEmployee.setPosition(employeeDetails.getPosition());
        if (employeeDetails.getEmploymentType() != null) existingEmployee.setEmploymentType(employeeDetails.getEmploymentType());
        if (employeeDetails.getSalary() != null) existingEmployee.setSalary(employeeDetails.getSalary());
        if (employeeDetails.getDateOfBirth() != null) existingEmployee.setDateOfBirth(employeeDetails.getDateOfBirth());
        if (employeeDetails.getAddress() != null) existingEmployee.setAddress(employeeDetails.getAddress());
        if (employeeDetails.getRole() != null) existingEmployee.setRole(employeeDetails.getRole());
        if (employeeDetails.getGender() != null) existingEmployee.setGender(employeeDetails.getGender());
        if (employeeDetails.getPassword() != null) existingEmployee.setPassword(employeeDetails.getPassword());

        try {
            kafka.send(TOPIC, new KafkaEvent<>(Event.UPDATE, existingEmployee));
        } catch (Exception e) {
            System.err.println("Failed to send Kafka event for employee: " + e.getMessage());
        }

        return employeeRepository.save(existingEmployee);
    }

    /**
     * Delete an employee by ID.
     * Sends a Kafka DELETE event to notify other services.
     */
    @Override
    public void deleteEmployee(Long employeeId) {
        Employee employee = getEmployeeById(employeeId);
        employeeRepository.delete(employee);

        try {
            kafka.send(TOPIC, new KafkaEvent<>(Event.DELETE, employee));
        } catch (Exception e) {
            System.err.println("Failed to send Kafka event for employee: " + e.getMessage());
        }
    }
}


