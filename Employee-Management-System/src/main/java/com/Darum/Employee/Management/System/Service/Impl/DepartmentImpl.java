package com.Darum.Employee.Management.System.Service.Impl;

import com.Darum.Employee.Management.System.Entites.Department;
import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Event.Enum.Event;
import com.Darum.Employee.Management.System.Event.KafkaEvent;
import com.Darum.Employee.Management.System.Repository.DepartmentRepository;
import com.Darum.Employee.Management.System.Service.DepartmentService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class DepartmentImpl implements DepartmentService {

    private final DepartmentRepository departmentRepository;

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


    @KafkaListener(topics = "employee-events", groupId = "department-service")
    public void consumeEmployeeEvent(KafkaEvent<Employee> event) {
        Event eventType = event.getAction();
        Employee employee = event.getPayload();

        log.info("📨 Received Kafka event: {} for Employee ID {}", eventType, employee.getUserId());

        switch (eventType) {
            case CREATE -> handleEmployeeCreated(employee);
            case UPDATE -> handleEmployeeUpdated(employee);
            case DELETE -> handleEmployeeDeleted(employee);
        }
    }

    // -------------------- EVENT HANDLERS --------------------

    private void handleEmployeeCreated(Employee employee) {
        if (employee.getDepartment() == null) return;

        departmentRepository.findById(employee.getDepartment().getDepartmentId()).ifPresentOrElse(dept -> {
            dept.getEmployees().add(employee);
            departmentRepository.save(dept);
            log.info("Employee {} added to Department {}", employee.getFirstName(), dept.getName());
        }, () -> log.warn("Department not found for Employee ID {}", employee.getUserId()));
    }

    private void handleEmployeeUpdated(Employee employee) {
        log.info("Employee {} updated — syncing with department", employee.getUserId());
    }

    private void handleEmployeeDeleted(Employee employee) {
        if (employee.getDepartment() == null) return;

        departmentRepository.findById(employee.getDepartment().getDepartmentId()).ifPresentOrElse(dept -> {
            dept.getEmployees().removeIf(e -> e.getUserId().equals(employee.getUserId()));
            departmentRepository.save(dept);
            log.info("Employee {} removed from Department {}", employee.getUserId(), dept.getName());
        }, () -> log.warn("Department not found for Employee ID {}", employee.getUserId()));
    }

}
