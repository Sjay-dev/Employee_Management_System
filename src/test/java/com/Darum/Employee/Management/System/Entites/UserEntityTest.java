package com.Darum.Employee.Management.System.Entites;

import com.Darum.Employee.Management.System.Entites.Enum.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserEntityTest {

    @Test
    void testUserFields() {
        User user = new Admin(); // Admin extends User
        user.setUserId(1L);
        user.setEmail("test@mail.com");
        user.setPassword("secret123");
        user.setRole(Role.ADMIN);

        // Verify basic fields
        assertEquals(1L, user.getUserId());
        assertEquals("test@mail.com", user.getEmail());
        assertEquals("secret123", user.getPassword());
        assertEquals(Role.ADMIN, user.getRole());

        // Verify timestamps are null by default
        assertNull(user.getCreatedAt());
    }
}
