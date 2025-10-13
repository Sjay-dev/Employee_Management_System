package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.Enum.Role;
import com.Darum.Employee.Management.System.Entites.Enum.Status;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class EmployeeRepositoryTest {

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * Test saving an Employee entity and retrieving it by email.
     * Ensures persistence, correct role, status, and field values.
     */
    @Test
    void testSaveAndFindByEmail() {
        Employee emp = new Employee();
        emp.setEmail("micheal@mail.com"); // Nigerian-style name
        emp.setPassword("pass");
        emp.setRole(Role.EMPLOYEE);
        emp.setFirstName("Micheal");
        emp.setLastName("Okoro");
        emp.setPhoneNumber("08099998888");
        emp.setDateOfBirth(LocalDate.of(1995, 3, 12));
        emp.setGender("MALE"); // Ensure consistency with enum or string field
        emp.setStatus(Status.INACTIVE);
        employeeRepository.save(emp);

        Optional<Employee> found = employeeRepository.findEmployeeByEmail("micheal@mail.com");
        assertTrue(found.isPresent(), "Employee should be found by email");
        assertEquals("Micheal", found.get().getFirstName(), "First name should match");
        assertEquals(Status.INACTIVE, found.get().getStatus(), "Status should match");
        assertEquals(Role.EMPLOYEE, found.get().getRole(), "Role should be EMPLOYEE");
    }

    /**
     * Test finding employees by first name.
     * Ensures repository can return a list of employees whose first name matches (case-insensitive if implemented).
     */
    @Test
    void testFindEmployeeByName() {
        // Save first employee
        Employee e1 = new Employee();
        e1.setEmail("jide@mail.com");
        e1.setPassword("pass");
        e1.setRole(Role.EMPLOYEE);
        e1.setFirstName("Jide");
        e1.setLastName("Adeyemi");
        e1.setPhoneNumber("0812000000");
        e1.setDateOfBirth(LocalDate.of(1998, 1, 1));
        e1.setGender("MALE");
        e1.setStatus(Status.ACTIVE);
        employeeRepository.save(e1);

        // Save second employee
        Employee e2 = new Employee();
        e2.setEmail("jumoke@mail.com");
        e2.setPassword("pass");
        e2.setRole(Role.EMPLOYEE);
        e2.setFirstName("Jumoke");
        e2.setLastName("Oladipo");
        e2.setPhoneNumber("0813000000");
        e2.setDateOfBirth(LocalDate.of(1999, 2, 2));
        e2.setGender("FEMALE");
        e2.setStatus(Status.ACTIVE);
        employeeRepository.save(e2);

        // Test finding employees with "J" in first name
        List<Employee> found = employeeRepository.findEmployeeByName("J");
        assertEquals(2, found.size(), "Should find two employees with 'J' in their first names");
        assertTrue(found.stream().anyMatch(emp -> emp.getFirstName().equals("Jide")), "Jide should be in the result");
        assertTrue(found.stream().anyMatch(emp -> emp.getFirstName().equals("Jumoke")), "Jumoke should be in the result");
    }
}


