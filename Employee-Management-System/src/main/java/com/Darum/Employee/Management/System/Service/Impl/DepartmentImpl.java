package com.Darum.Employee.Management.System.Service.Impl;

import com.Darum.Employee.Management.System.Entites.Department;
import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.Manager;
import com.Darum.Employee.Management.System.Event.Enum.Event;
import com.Darum.Employee.Management.System.Event.KafkaEvent;
import com.Darum.Employee.Management.System.Repository.DepartmentRepository;
import com.Darum.Employee.Management.System.Repository.EmployeeRepository;
import com.Darum.Employee.Management.System.Repository.ManagerRepository;
import com.Darum.Employee.Management.System.Service.DepartmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DepartmentImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;
    private final ManagerRepository managerRepository;
    private final EmployeeRepository employeeRepository;

    // ------------------------- DEPARTMENT CRUD -------------------------

    /**
     * Add a new department.
     */
    @Override
    public Department addDepartment(Department department) {
        return departmentRepository.save(department);
    }

    /**
     * Get a department by its ID.
     */
    @Override
    public Department getDepartmentById(Long departmentId) {
        return departmentRepository.findById(departmentId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Department not found with ID: " + departmentId
                ));
    }

    /**
     * Get all departments.
     */
    @Override
    public List<Department> getAllDepartments() {
        return departmentRepository.findAll();
    }

    /**
     * Get all departments by name.
     */
    @Override
    public List<Department> getAllDepartmentsByName(String name) {
        List<Department> departments = departmentRepository.findDepartmentByByName(name);
        if (departments.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No departments found with name: " + name);
        }
        return departments;
    }

    /**
     * Update an existing department's details.
     */
    @Override
    public Department updateDepartmentDetails(Long departmentId, Department department) {
        Department existingDepartment = getDepartmentById(departmentId);

        if (department.getName() != null)
            existingDepartment.setName(department.getName());

        if (department.getDescription() != null)
            existingDepartment.setDescription(department.getDescription());

        return departmentRepository.save(existingDepartment);
    }

    /**
     * Delete a department by its ID.
     */
    @Override
    public void deleteDepartment(Long departmentId) {
        if (!departmentRepository.existsById(departmentId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Department not found with ID: " + departmentId
            );
        }
        departmentRepository.deleteById(departmentId);
    }

    // ------------------------- MANAGER MANAGEMENT -------------------------

    /**
     * Add an existing manager to a department.
     */
    @Override
    public Department addExistingManagerToDepartment(Long departmentId, Long userId) {
        Department department = getDepartmentById(departmentId);
        Manager manager = managerRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Manager not found with ID: " + userId
                ));

        department.addManager(manager);
        return departmentRepository.save(department);
    }

    /**
     * Remove a manager from a department.
     */
    @Override
    public Department removeManagerFromDepartment(Long departmentId, Long managerId) {
        Department department = getDepartmentById(departmentId);
        Manager manager = managerRepository.findById(managerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Manager not found with ID: " + managerId
                ));

        department.removeManager(manager);
        return departmentRepository.save(department);
    }

    // ------------------------- EMPLOYEE MANAGEMENT -------------------------

    /**
     * Add an existing employee to a department.
     */
    @Override
    public Department addExistingEmployeeToDepartment(Long departmentId, Long employeeId) {
        Department department = getDepartmentById(departmentId);
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Employee not found with ID: " + employeeId
                ));

        department.getEmployees().add(employee);
        employee.setDepartment(department);
        employeeRepository.save(employee);
        return departmentRepository.save(department);
    }

    /**
     * Remove an employee from a department.
     */
    @Override
    public Department removeEmployeeFromDepartment(Long departmentId, Long employeeId) {
        Department department = getDepartmentById(departmentId);
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Employee not found with ID: " + employeeId
                ));

        department.getEmployees().remove(employee);
        employee.setDepartment(null);
        employeeRepository.save(employee);
        return departmentRepository.save(department);
    }

    // ------------------------- KAFKA EVENT HANDLING -------------------------

    /**
     * Consume employee-related Kafka events.
     */
    @KafkaListener(topics = "employee-events", groupId = "department-service")
    public void consumeEmployeeEvent(KafkaEvent<Employee> event) {
        Event eventType = event.getAction();
        Employee employee = event.getPayload();

        switch (eventType) {
            case CREATE -> handleEmployeeCreated(employee);
            case UPDATE -> handleEmployeeUpdated(employee);
            case DELETE -> handleEmployeeDeleted(employee);
        }
    }

    private void handleEmployeeCreated(Employee employee) {
        if (employee.getDepartment() == null) return;

        departmentRepository.findById(employee.getDepartment().getDepartmentId()).ifPresent(dept -> {
            dept.getEmployees().add(employee);
            departmentRepository.save(dept);
        });
    }

    private void handleEmployeeUpdated(Employee employee) {
        // Placeholder for future syncing logic
    }

    private void handleEmployeeDeleted(Employee employee) {
        if (employee.getDepartment() == null) return;

        departmentRepository.findById(employee.getDepartment().getDepartmentId()).ifPresent(dept -> {
            dept.getEmployees().removeIf(e -> e.getUserId().equals(employee.getUserId()));
            departmentRepository.save(dept);
        });
    }
}



