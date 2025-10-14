package com.Darum.Employee.Management.System.Entites;

import com.Darum.Employee.Management.System.Entites.Enum.Role;
import com.Darum.Employee.Management.System.Entites.Enum.Status;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

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

        // Verify default role and status
        assertEquals(Role.MANAGER, manager.getRole());
        assertEquals(Status.INACTIVE, manager.getStatus());

        // Verify individual fields
        assertEquals("Jane", manager.getFirstName());
        assertEquals("Doe", manager.getLastName());
        assertEquals("08123456789", manager.getPhoneNumber());
        assertEquals("manager@mail.com", manager.getEmail());
        assertEquals("123456", manager.getPassword());
        assertEquals(LocalDate.of(1990, 5, 15), manager.getDateOfBirth());
        assertEquals("Female", manager.getGender());

        // Verify list initialization
        assertNotNull(manager.getEmployeeNames());
        assertTrue(manager.getEmployeeNames().isEmpty());
    }
}
