package com.Darum.Employee.Management.System.Service.Impl;

import com.Darum.Employee.Management.System.Entites.Department;
import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.Manager;
import com.Darum.Employee.Management.System.Event.Enum.Event;
import com.Darum.Employee.Management.System.Event.KafkaEvent;
import com.Darum.Employee.Management.System.Repository.DepartmentRepository;
import com.Darum.Employee.Management.System.Repository.EmployeeRepository;
import com.Darum.Employee.Management.System.Repository.ManagerRepository;
import com.Darum.Employee.Management.System.Service.ManagerService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final DepartmentRepository departmentRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;


    // ------------------------- MANAGER CRUD -------------------------

    /**
     * Add a new manager.
     */
    @Override
    public Manager addManager(Manager manager) {
        // Encode password
        if (manager.getPassword() != null) {
            manager.setPassword(passwordEncoder.encode(manager.getPassword()));
        }

        // Save the manager first
        Manager saved = managerRepository.save(manager);

        // Assign this manager to all employees in the same department without a manager
        if (saved.getDepartment() != null) {
            List<Employee> employees = employeeRepository.findByDepartmentAndManagerIsNull(saved.getDepartment());
            for (Employee e : employees) {
                e.setManager(saved);
            }
            employeeRepository.saveAll(employees);
        }

        return saved;
    }


    /**
     * Get a manager by ID.
     */
    @Override
    public Manager getManagerById(Long managerId) {
        return managerRepository.findById(managerId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Manager not found with ID: " + managerId
                ));
    }

    /**
     * Get a manager by email.
     */
    @Override
    public Manager getManagerByEmail(String email) {
        return managerRepository.findManagerByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Manager not found with email: " + email
                ));
    }

    /**
     * Get all managers.
     */
    @Override
    public List<Manager> getAllManagers() {
        return managerRepository.findAll();
    }

    /**
     * Get a manager by department ID.
     * Currently not implemented; return null for now.
     */
    @Override
    public Manager getManagerByDepartmentId(Long departmentId) {
        return managerRepository.findAll().stream()
                .filter(manager -> manager.getDepartment() != null
                        && manager.getDepartment().getDepartmentId().equals(departmentId))
                .findFirst()
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "No manager found for department ID: " + departmentId
                ));
    }

    /**
     * Get all managers by name.
     */
    @Override
    public List<Manager> getAllManagersByName(String name) {
        List<Manager> managers = managerRepository.findManagerByName(name);
        if (managers.isEmpty()) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "No managers found with name: " + name
            );
        }
        return managers;
    }

    /**
     * Update an existing manager's details.
     */
    @Override
    public Manager updateManager(Long managerId, Manager managerDetails) {
        Manager existingManager = getManagerById(managerId);

        if (managerDetails.getFirstName() != null) existingManager.setFirstName(managerDetails.getFirstName());
        if (managerDetails.getLastName() != null) existingManager.setLastName(managerDetails.getLastName());
        if (managerDetails.getEmail() != null) existingManager.setEmail(managerDetails.getEmail());
        if (managerDetails.getPassword() != null) existingManager.setPassword(passwordEncoder.encode(managerDetails.getPassword()));
        if (managerDetails.getPhoneNumber() != null) existingManager.setPhoneNumber(managerDetails.getPhoneNumber());
        if (managerDetails.getRole() != null) existingManager.setRole(managerDetails.getRole());
        if (managerDetails.getDepartment() != null) existingManager.setDepartment(managerDetails.getDepartment());
        if (managerDetails.getPosition() != null) existingManager.setPosition(managerDetails.getPosition());
        if (managerDetails.getStatus() != null) existingManager.setStatus(managerDetails.getStatus());
        if (managerDetails.getEmploymentDate() != null) existingManager.setEmploymentDate(managerDetails.getEmploymentDate());
        if (managerDetails.getEmploymentType() != null) existingManager.setEmploymentType(managerDetails.getEmploymentType());
        if (managerDetails.getSalary() != null) existingManager.setSalary(managerDetails.getSalary());
        if (managerDetails.getAddress() != null) existingManager.setAddress(managerDetails.getAddress());
        if (managerDetails.getGender() != null) existingManager.setGender(managerDetails.getGender());

        return managerRepository.save(existingManager);
    }


    /**
     * Delete a manager by ID.
     */
    @Override
    public void deleteManager(Long managerId) {
        if (!managerRepository.existsById(managerId)) {
            throw new ResponseStatusException(
                    HttpStatus.NOT_FOUND, "Manager not found with ID: " + managerId
            );
        }
        managerRepository.deleteById(managerId);
    }

    // -------------------- Kafka Employee Event Handling --------------------

    @KafkaListener(topics = "employee-events", groupId = "manager-service")
    public void consumeEmployeeEvent(KafkaEvent<Employee> event) {
        Event eventType = event.getAction();
        Employee employee = event.getPayload();

        switch (eventType) {
            case CREATE -> handleEmployeeCreated(employee);
            case UPDATE -> handleEmployeeUpdated(employee);
            case DELETE -> handleEmployeeDeleted(employee);
        }
    }

    /**
     * Handle employee creation event.
     */
    private void handleEmployeeCreated(Employee employee) {
        if (employee.getDepartment() == null) return;

        departmentRepository.findById(employee.getDepartment().getDepartmentId()).ifPresent(dept -> {
            dept.getEmployees().add(employee);
            departmentRepository.save(dept);
        });
    }

    /**
     * Handle employee update event.
     */
    private void handleEmployeeUpdated(Employee employee) {

    }

    /**
     * Handle employee deletion event.
     */
    private void handleEmployeeDeleted(Employee employee) {
        if (employee.getDepartment() == null) return;

        departmentRepository.findById(employee.getDepartment().getDepartmentId()).ifPresent(dept -> {
            dept.getEmployees().removeIf(e -> e.getUserId().equals(employee.getUserId()));
            departmentRepository.save(dept);
        });
    }
}


