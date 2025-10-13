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

        managerRepository.save(manager);

        Optional<Manager> found = managerRepository.findManagerByEmail("manager@mail.com");
        assertTrue(found.isPresent());
        assertEquals("Jane", found.get().getFirstName());
        assertEquals(Role.MANAGER, found.get().getRole());
    }

    @Test
    void testFindManagerByName() {
        Manager m1 = new Manager();
        m1.setEmail("one@mail.com");
        m1.setPassword("pass");
        m1.setFirstName("John");
        m1.setLastName("Doe");
        m1.setPhoneNumber("0801");
        m1.setGender("Male");
        m1.setDateOfBirth(LocalDate.of(1990, 1, 1));
        managerRepository.save(m1);

        Manager m2 = new Manager();
        m2.setEmail("two@mail.com");
        m2.setPassword("pass");
        m2.setFirstName("Mary");
        m2.setLastName("Smith");
        m2.setPhoneNumber("0802");
        m2.setGender("Female");
        m2.setDateOfBirth(LocalDate.of(1988, 2, 2));
        managerRepository.save(m2);

        List<Manager> found = managerRepository.findManagerByName("mary");
        assertEquals(1, found.size());
        assertEquals("Mary", found.get(0).getFirstName());
    }
}
