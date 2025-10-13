package com.Darum.Employee.Management.System.ServiceImpl;

import com.Darum.Employee.Management.System.Entites.Department;
import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.Manager;
import com.Darum.Employee.Management.System.Event.Enum.Event;
import com.Darum.Employee.Management.System.Event.KafkaEvent;
import com.Darum.Employee.Management.System.Repository.DepartmentRepository;
import com.Darum.Employee.Management.System.Repository.ManagerRepository;
import com.Darum.Employee.Management.System.Service.Impl.ManagerServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ManagerServiceImplTest {

    @Mock
    private ManagerRepository managerRepository;
    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private ManagerServiceImpl managerService;

    @Test
    void testAddManager() {
        Manager m = new Manager();
        m.setFirstName("Jane");

        when(managerRepository.save(m)).thenReturn(m);
        Manager saved = managerService.addManager(m);
        assertEquals("Jane", saved.getFirstName());
    }

    @Test
    void testUpdateManager() {
        Manager existing = new Manager();
        existing.setUserId(1L);
        existing.setFirstName("Old");

        Manager updates = new Manager();
        updates.setFirstName("New");

        when(managerRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(managerRepository.save(existing)).thenReturn(existing);

        Manager updated = managerService.updateManager(1L, updates);
        assertEquals("New", updated.getFirstName());
    }

    @Test
    void testDeleteManager() {
        when(managerRepository.existsById(1L)).thenReturn(true);
        managerService.deleteManager(1L);
        verify(managerRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteManager_NotFound() {
        when(managerRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> managerService.deleteManager(1L));
    }

    @Test
    void testConsumeEmployeeCreateEvent_AddsEmployeeToDepartment() {
        Department dept = new Department();
        dept.setDepartmentId(1L);
        dept.setEmployees(new ArrayList<>());

        Employee emp = new Employee();
        emp.setUserId(10L);
        emp.setFirstName("Alice");
        emp.setDepartment(dept);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));
        when(departmentRepository.save(dept)).thenReturn(dept);

        managerService.consumeEmployeeEvent(new KafkaEvent<>(Event.CREATE, emp));

        verify(departmentRepository, times(1)).save(dept);
        assert(dept.getEmployees().contains(emp));
    }

    @Test
    void testConsumeEmployeeDeleteEvent_RemovesEmployeeFromDepartment() {
        Employee emp = new Employee();
        emp.setUserId(20L);

        Department dept = new Department();
        dept.setDepartmentId(2L);
        dept.setEmployees(new ArrayList<>(List.of(emp)));

        emp.setDepartment(dept);

        when(departmentRepository.findById(2L)).thenReturn(Optional.of(dept));
        when(departmentRepository.save(dept)).thenReturn(dept);

        managerService.consumeEmployeeEvent(new KafkaEvent<>(Event.DELETE, emp));

        verify(departmentRepository, times(1)).save(dept);
        assert(dept.getEmployees().isEmpty());
    }

    @Test
    void testConsumeEmployeeUpdateEvent_LogsUpdateOnly() {
        Employee emp = new Employee();
        emp.setUserId(30L);
        emp.setDepartment(null);

        managerService.consumeEmployeeEvent(new KafkaEvent<>(Event.UPDATE, emp));
        verifyNoInteractions(departmentRepository);
    }

}
