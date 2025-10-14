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

    /**
     * Test saving an Admin entity and retrieving it by email.
     * Ensures the repository correctly persists the Admin
     * and can find it using findAdminByEmail method.
     */
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

        assertTrue(found.isPresent(), "Admin should be found by email");
        assertEquals("Joseph", found.get().getFirstName(), "First name should match");
        assertEquals(Role.ADMIN, found.get().getRole(), "Role should be ADMIN");
    }

    /**
     * Test retrieving Admin entities by exact name.
     * Ensures findAdminByName returns correct results.
     */
    @Test
    void testFindAdminByName() {
        Admin a1 = new Admin();
        a1.setEmail("uche@mail.com");
        a1.setPassword("123");
        a1.setFirstName("Jane");
        a1.setLastName("Okoro");
        a1.setPhoneNumber("08012345678");
        adminRepository.save(a1);

        Admin a2 = new Admin();
        a2.setEmail("bola@mail.com");
        a2.setPassword("123");
        a2.setFirstName("John");
        a2.setLastName("Ibrahim");
        a2.setPhoneNumber("08098765432");
        adminRepository.save(a2);

        List<Admin> results = adminRepository.findAdminByName("Jane");

        assertEquals(1, results.size(), "Only one admin with the name Jane should be found");
        assertEquals("Jane", results.get(0).getFirstName(), "First name should match 'Jane'");
    }

    /**
     * Test retrieving Admins whose first names start with 'J'.
     * Useful for partial or prefix search scenarios.
     */
    @Test
    void testFindAdminsWithNamesStartingWithJ() {
        Admin a1 = new Admin();
        a1.setEmail("jide@mail.com");
        a1.setPassword("123");
        a1.setFirstName("Jide");
        a1.setLastName("Adebayo");
        a1.setPhoneNumber("08011112222");
        adminRepository.save(a1);

        Admin a2 = new Admin();
        a2.setEmail("joy@mail.com");
        a2.setPassword("123");
        a2.setFirstName("Joy");
        a2.setLastName("Chukwu");
        a2.setPhoneNumber("08033334444");
        adminRepository.save(a2);

        Admin a3 = new Admin();
        a3.setEmail("femi@mail.com");
        a3.setPassword("123");
        a3.setFirstName("Femi");
        a3.setLastName("Ola");
        a3.setPhoneNumber("08055556666");
        adminRepository.save(a3);

        // Fetch all admins and filter names starting with 'J'
        List<Admin> allAdmins = adminRepository.findAll();
        List<Admin> jNames = allAdmins.stream()
                .filter(admin -> admin.getFirstName().startsWith("J"))
                .toList();

        assertEquals(2, jNames.size(), "Two admins should have names starting with 'J'");
        assertTrue(jNames.stream().anyMatch(a -> a.getFirstName().equals("Jide")));
        assertTrue(jNames.stream().anyMatch(a -> a.getFirstName().equals("Joy")));
    }
}


