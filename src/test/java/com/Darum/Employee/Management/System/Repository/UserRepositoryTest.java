package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Entites.Admin;
import com.Darum.Employee.Management.System.Entites.Enum.Role;
import com.Darum.Employee.Management.System.Entites.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AdminRepository adminRepository;

    /**
     * Test retrieving a User by email across the inheritance hierarchy.
     * Ensures that a subclass (Admin) can be found when querying the parent type (User).
     */
    @Test
    void testFindUserByEmailAcrossInheritance() {
        // Create an Admin instance (subclass of User)
        Admin admin = new Admin();
        admin.setEmail("admin@mail.com");
        admin.setPassword("pass");
        admin.setFirstName("Joseph");
        admin.setLastName("Sanusi");
        admin.setPhoneNumber("08123456789");

        // Persist the admin entity
        adminRepository.save(admin);

        // Query using the parent repository (UserRepository)
        Optional<User> found = userRepository.findByEmail("admin@mail.com");

        // Verify that the entity is retrieved correctly
        assertTrue(found.isPresent(), "User should be found by email");
        assertEquals("admin@mail.com", found.get().getEmail(), "Email should match the saved admin");
        assertEquals(Role.ADMIN, found.get().getRole(), "Role should be ADMIN for Admin entity");
    }
}

