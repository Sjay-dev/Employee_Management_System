package com.Darum.Employee.Management.System.Entites;

import com.Darum.Employee.Management.System.Entites.Enum.Status;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class EmployeeEntityTest {
    @Test
    void testEmployeeDefaultsAndRelations() {
        Employee emp = new Employee();
        emp.setFirstName("John");
        emp.setLastName("Smith");
        emp.setEmail("john@mail.com");
        emp.setPassword("secret");
        emp.setPhoneNumber("09012345678");
        emp.setDateOfBirth(LocalDate.of(1998, 3, 12));
        emp.setGender("Male");

        assertEquals(Status.INACTIVE, emp.getStatus());
        assertEquals("John", emp.getFirstName());
        assertNull(emp.getDepartment());
        assertNull(emp.getManager());
    }
}
