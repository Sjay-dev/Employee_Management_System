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

    @Test
    void testFindUserByEmailAcrossInheritance() {
        Admin admin = new Admin();
        admin.setEmail("admin@mail.com");
        admin.setPassword("pass");
        admin.setFirstName("Joseph");
        admin.setLastName("Sanusi");
        admin.setPhoneNumber("08123456789");
        adminRepository.save(admin);

        Optional<User> found = userRepository.findByEmail("admin@mail.com");
        assertTrue(found.isPresent());
        assertEquals("admin@mail.com", found.get().getEmail());
        assertEquals(Role.ADMIN, found.get().getRole());
    }
}
