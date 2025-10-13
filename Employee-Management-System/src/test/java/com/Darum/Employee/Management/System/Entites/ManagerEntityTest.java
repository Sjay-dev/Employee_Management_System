package com.Darum.Employee.Management.System.Entites;

import com.Darum.Employee.Management.System.Entites.Enum.Role;
import com.Darum.Employee.Management.System.Entites.Enum.Status;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ManagerEntityTest {
    @Test
    void testManagerDefaultsAndRelationships() {
        Manager manager = new Manager();
        manager.setFirstName("Jane");
        manager.setLastName("Doe");
        manager.setPhoneNumber("08123456789");
        manager.setEmail("manager@mail.com");
        manager.setPassword("123456");
        manager.setDateOfBirth(LocalDate.of(1990, 5, 15));
        manager.setGender("Female");

        assertEquals(Role.MANAGER, manager.getRole());
        assertEquals(Status.INACTIVE, manager.getStatus());
        assertEquals("Jane", manager.getFirstName());
        assertEquals("Female", manager.getGender());
        assertTrue(manager.getEmployees().isEmpty());
    }
}
