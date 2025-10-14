package com.Darum.Employee.Management.System.Entites;

import com.Darum.Employee.Management.System.Entites.Enum.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AdminEntityTest {

    @Test
    void testAdminDefaultRoleAndFields() {
        Admin admin = new Admin();
        admin.setFirstName("Joseph");
        admin.setLastName("Sanusi");
        admin.setPhoneNumber("08012345678");
        admin.setEmail("admin@mail.com");
        admin.setPassword("password");

        // Check that default role is ADMIN
        assertEquals(Role.ADMIN, admin.getRole());

        // Check individual fields
        assertEquals("Joseph", admin.getFirstName());
        assertEquals("Sanusi", admin.getLastName());
        assertEquals("08012345678", admin.getPhoneNumber());
        assertEquals("admin@mail.com", admin.getEmail());
        assertEquals("password", admin.getPassword());
    }
}
