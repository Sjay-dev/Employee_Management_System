package com.Darum.Employee.Management.System.ServiceImpl;

import com.Darum.Employee.Management.System.Entites.Department;
import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Event.Enum.Event;
import com.Darum.Employee.Management.System.Event.KafkaEvent;
import com.Darum.Employee.Management.System.Repository.DepartmentRepository;
import com.Darum.Employee.Management.System.Service.Impl.DepartmentImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DepartmentServiceImplTest {

    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentImpl departmentService;

    @Test
    void testAddDepartment() {
        Department dept = new Department();
        dept.setName("IT");

        when(departmentRepository.save(dept)).thenReturn(dept);
        Department saved = departmentService.addDepartment(dept);

        assertEquals("IT", saved.getName());
        verify(departmentRepository, times(1)).save(dept);
    }

    @Test
    void testUpdateDepartment() {
        Department existing = new Department();
        existing.setDepartmentId(1L);
        existing.setName("OldName");

        Department updates = new Department();
        updates.setName("NewName");

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(departmentRepository.save(existing)).thenReturn(existing);

        Department updated = departmentService.updateDepartmentDetails(1L, updates);
        assertEquals("NewName", updated.getName());
    }

    @Test
    void testDeleteDepartment() {
        when(departmentRepository.existsById(1L)).thenReturn(true);
        departmentService.deleteDepartment(1L);
        verify(departmentRepository, times(1)).deleteById(1L);
    }

    @Test
    void testDeleteDepartment_NotFound() {
        when(departmentRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> departmentService.deleteDepartment(1L));
    }


    @Test
    void testConsumeEmployeeCreateEvent_AddsEmployeeToDepartment() {
        Department dept = new Department();
        dept.setDepartmentId(1L);
        dept.setEmployees(new ArrayList<>());

        Employee emp = new Employee();
        emp.setUserId(10L);
        emp.setFirstName("John");
        emp.setDepartment(dept);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));
        when(departmentRepository.save(dept)).thenReturn(dept);

        departmentService.consumeEmployeeEvent(new KafkaEvent<>(Event.CREATE, emp));

        verify(departmentRepository, times(1)).save(dept);
        assert(dept.getEmployees().contains(emp));
    }

    @Test
    void testConsumeEmployeeDeleteEvent_RemovesEmployeeFromDepartment() {
        Employee emp = new Employee();
        emp.setUserId(10L);

        Department dept = new Department();
        dept.setDepartmentId(1L);
        dept.setEmployees(new ArrayList<>(List.of(emp)));

        emp.setDepartment(dept);

        when(departmentRepository.findById(1L)).thenReturn(Optional.of(dept));
        when(departmentRepository.save(dept)).thenReturn(dept);

        departmentService.consumeEmployeeEvent(new KafkaEvent<>(Event.DELETE, emp));

        verify(departmentRepository, times(1)).save(dept);
        assert(dept.getEmployees().isEmpty());
    }

    @Test
    void testConsumeEmployeeUpdateEvent_LogsUpdate() {
        Employee emp = new Employee();
        emp.setUserId(10L);
        emp.setDepartment(null);

        departmentService.consumeEmployeeEvent(new KafkaEvent<>(Event.UPDATE, emp));
    }

}
