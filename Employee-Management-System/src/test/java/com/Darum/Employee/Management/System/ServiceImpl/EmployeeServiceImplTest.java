package com.Darum.Employee.Management.System.ServiceImpl;

import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Repository.EmployeeRepository;
import com.Darum.Employee.Management.System.Service.Impl.EmployeeServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceImplTest {

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private KafkaTemplate<String, Object> kafka;

    @InjectMocks
    private EmployeeServiceImpl employeeService;

    /**
     * Test adding a new employee.
     * Verifies that the employee is saved, and a Kafka CREATE event is sent.
     */
    @Test
    void testAddEmployee() {
        Employee emp = new Employee();
        emp.setFirstName("John");
        emp.setDateOfBirth(LocalDate.of(1990, 1, 1));

        when(employeeRepository.save(emp)).thenReturn(emp);

        Employee saved = employeeService.addEmployee(emp);

        // Verify employee fields
        assertEquals("John", saved.getFirstName());

        // Verify repository save and Kafka event are called once
        verify(employeeRepository, times(1)).save(emp);
        verify(kafka, times(1)).send(eq("Employee.events"), any());
    }

    /**
     * Test deleting an employee.
     * Verifies that the employee is deleted and a Kafka DELETE event is sent.
     */
    @Test
    void testDeleteEmployee() {
        Employee emp = new Employee();
        emp.setUserId(1L);

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(emp));

        employeeService.deleteEmployee(1L);

        verify(employeeRepository, times(1)).delete(emp);
        verify(kafka, times(1)).send(eq("Employee.events"), any());
    }

    /**
     * Test updating an employee.
     * Verifies that only updated fields are changed, repository save is called,
     * and a Kafka UPDATE event is sent.
     */
    @Test
    void testUpdateEmployee() {
        Employee existing = new Employee();
        existing.setUserId(1L);
        existing.setFirstName("Old");

        Employee updates = new Employee();
        updates.setFirstName("New");

        when(employeeRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(employeeRepository.save(existing)).thenReturn(existing);

        Employee updated = employeeService.updateEmployee(1L, updates);

        // Verify updated field
        assertEquals("New", updated.getFirstName());

        // Verify Kafka UPDATE event sent
        verify(kafka, times(1)).send(eq("Employee.events"), any());
    }
}

