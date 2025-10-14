package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Entites.Enum.Role;
import com.Darum.Employee.Management.System.Entites.Manager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class ManagerRepositoryTest {

    @Autowired
    private ManagerRepository managerRepository;

    /**
     * Test saving a Manager entity and retrieving it by email.
     * Verifies that the entity is persisted correctly and that default role is set to MANAGER.
     */
    @Test
    void testSaveAndFindManagerByEmail() {
        Manager manager = new Manager();
        manager.setEmail("manager@mail.com");
        manager.setPassword("pass");
        manager.setFirstName("Jane");
        manager.setLastName("Doe");
        manager.setPhoneNumber("08123456789");
        manager.setGender("Female");
        manager.setDateOfBirth(LocalDate.of(1990, 5, 15));

        // Persist the manager to the in-memory database
        managerRepository.save(manager);

        // Retrieve by email and verify
        Optional<Manager> found = managerRepository.findManagerByEmail("manager@mail.com");
        assertTrue(found.isPresent(), "Manager should be found by email");
        assertEquals("Jane", found.get().getFirstName(), "First name should match");
        assertEquals(Role.MANAGER, found.get().getRole(), "Role should default to MANAGER");
    }

    /**
     * Test finding managers by first name.
     * Verifies case-insensitive search and correct filtering.
     */
    @Test
    void testFindManagerByName() {
        // Create and persist first manager
        Manager m1 = new Manager();
        m1.setEmail("one@mail.com");
        m1.setPassword("pass");
        m1.setFirstName("John");
        m1.setLastName("Doe");
        m1.setPhoneNumber("0801");
        m1.setGender("Male");
        m1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        managerRepository.save(m1);

        // Create and persist second manager
        Manager m2 = new Manager();
        m2.setEmail("two@mail.com");
        m2.setPassword("pass");
        m2.setFirstName("Mary");
        m2.setLastName("Smith");
        m2.setPhoneNumber("0802");
        m2.setGender("Female");
        m2.setDateOfBirth(LocalDate.of(1988, 2, 2));
        managerRepository.save(m2);

        // Retrieve managers by first name (case-insensitive search)
        List<Manager> found = managerRepository.findManagerByName("mary");
        assertEquals(1, found.size(), "Only one manager should match the name 'mary'");
        assertEquals("Mary", found.get(0).getFirstName(), "First name should be Mary");
    }
}

