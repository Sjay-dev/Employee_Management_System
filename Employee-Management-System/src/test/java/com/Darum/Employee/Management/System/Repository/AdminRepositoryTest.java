package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Entites.Admin;
import com.Darum.Employee.Management.System.Entites.Enum.Role;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class AdminRepositoryTest {
    @Autowired
    private AdminRepository adminRepository;

    @Test
    void testSaveAndFindAdminByEmail() {
        Admin admin = new Admin();
        admin.setEmail("admin@mail.com");
        admin.setPassword("pass");
        admin.setFirstName("Joseph");
        admin.setLastName("Sanusi");
        admin.setPhoneNumber("08123456789");
        adminRepository.save(admin);

        Optional<Admin> found = adminRepository.findAdminByEmail("admin@mail.com");
        assertTrue(found.isPresent());
        assertEquals("Joseph", found.get().getFirstName());
        assertEquals(Role.ADMIN, found.get().getRole());
    }

    @Test
    void testFindAdminByName() {
        Admin a1 = new Admin();
        a1.setEmail("one@mail.com");
        a1.setPassword("123");
        a1.setFirstName("Jane");
        a1.setLastName("Doe");
        a1.setPhoneNumber("0801");
        adminRepository.save(a1);

        Admin a2 = new Admin();
        a2.setEmail("two@mail.com");
        a2.setPassword("123");
        a2.setFirstName("John");
        a2.setLastName("Smith");
        a2.setPhoneNumber("0802");
        adminRepository.save(a2);

        List<Admin> results = adminRepository.findAdminByName("Jane");
        assertEquals(1, results.size());
        assertEquals("Jane", results.get(0).getFirstName());
    }
}
