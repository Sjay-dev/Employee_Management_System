package com.Darum.Employee.Management.System.Entites;

import com.Darum.Employee.Management.System.Entites.Enum.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserEntityTest {

    @Test
    void testUserFields() {
        User user = new Admin();
        user.setUserId(1L);
        user.setEmail("test@mail.com");
        user.setPassword("secret123");
        user.setRole(Role.ADMIN);

        assertEquals(1L, user.getUserId());
        assertEquals("test@mail.com", user.getEmail());
        assertEquals("secret123", user.getPassword());
        assertEquals(Role.ADMIN, user.getRole());
        assertNull(user.getCreatedAt());
    }
}
