package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Entites.Employee;
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

    @Test
    void testSaveAndFindByEmail() {
        Employee emp = new Employee();
        emp.setEmail("emp@mail.com");
        emp.setPassword("pass");
        emp.setFirstName("John");
        emp.setLastName("Smith");
        emp.setPhoneNumber("08099998888");
        emp.setDateOfBirth(LocalDate.of(1995, 3, 12));
        emp.setGender("Male");
        employeeRepository.save(emp);

        Optional<Employee> found = employeeRepository.findEmployeeByEmail("emp@mail.com");
        assertTrue(found.isPresent());
        assertEquals("John", found.get().getFirstName());
        assertEquals(Status.INACTIVE, found.get().getStatus());
    }

    @Test
    void testFindEmployeeByName() {
        Employee e1 = new Employee();
        e1.setEmail("jane@mail.com");
        e1.setPassword("pass");
        e1.setFirstName("Jane");
        e1.setLastName("Doe");
        e1.setPhoneNumber("0812");
        e1.setDateOfBirth(LocalDate.of(1998, 1, 1));
        e1.setGender("Female");
        employeeRepository.save(e1);

        Employee e2 = new Employee();
        e2.setEmail("john@mail.com");
        e2.setPassword("pass");
        e2.setFirstName("John");
        e2.setLastName("Brown");
        e2.setPhoneNumber("0813");
        e2.setDateOfBirth(LocalDate.of(1999, 2, 2));
        e2.setGender("Male");
        employeeRepository.save(e2);

        List<Employee> found = employeeRepository.findEmployeeByName("john");
        assertEquals(1, found.size());
        assertEquals("John", found.get(0).getFirstName());
    }
}
