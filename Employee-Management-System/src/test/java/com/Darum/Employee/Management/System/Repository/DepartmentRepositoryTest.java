package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Entites.Department;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class DepartmentRepositoryTest {

    @Autowired
    private DepartmentRepository departmentRepository;

    /**
     * Test saving multiple Department entities and finding one by name (case-insensitive, partial match).
     * Ensures that the repository correctly persists departments and can search using a keyword.
     */
    @Test
    void testSaveAndFindDepartmentByName() {
        // Save a few departments with Nigerian-themed names
        Department d1 = new Department();
        d1.setName("IT Department");
        d1.setDescription("Handles all tech operations");
        departmentRepository.save(d1);

        Department d2 = new Department();
        d2.setName("Accounts");
        d2.setDescription("Handles company finances");
        departmentRepository.save(d2);

        Department d3 = new Department();
        d3.setName("Human Resources");
        d3.setDescription("Manages employee relations");
        departmentRepository.save(d3);

        // Attempt to find departments with a name containing "IT"
        List<Department> found = departmentRepository.findDepartmentByByName("IT");

        // Assertions
        assertEquals(1, found.size(), "Should find only one department containing 'IT'");
        assertEquals("IT Department", found.get(0).getName(), "Department name should match");
    }
}

