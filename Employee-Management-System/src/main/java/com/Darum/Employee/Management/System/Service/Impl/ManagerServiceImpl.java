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
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class ManagerServiceImpl implements ManagerService {

    private final ManagerRepository managerRepository;
    private final DepartmentRepository departmentRepository;





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

    //  EVENT HANDLERS

    private void handleEmployeeCreated(Employee employee) {
        if (employee.getDepartment() == null) {
            return;
        }

        departmentRepository.findById(employee.getDepartment().getDepartmentId()).ifPresentOrElse(dept -> {
            dept.getEmployees().add(employee);
            departmentRepository.save(dept);
            log.info("👨‍💼 Employee {} added to Department {}", employee.getFirstName(), dept.getName());
        }, () -> log.warn("⚠️ Department not found for Employee ID {}", employee.getUserId()));
    }

    private void handleEmployeeUpdated(Employee employee) {
        log.info("🔁 Employee {} updated — syncing department info", employee.getUserId());
        // Optional: update department info if employee moved departments
    }

    private void handleEmployeeDeleted(Employee employee) {
        if (employee.getDepartment() == null) {
            log.warn("⚠️ Employee {} deleted without department reference — skipping cleanup", employee.getEmail());
            return;
        }

        departmentRepository.findById(employee.getDepartment().getDepartmentId()).ifPresentOrElse(dept -> {
            dept.getEmployees().removeIf(e -> e.getUserId().equals(employee.getUserId()));
            departmentRepository.save(dept);
            log.info("🧹 Employee {} removed from Department {}", employee.getUserId(), dept.getName());
        }, () -> log.warn("⚠️ Department not found for Employee ID {}", employee.getUserId()));

    }
}
