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

    @Test
    void testSaveAndFindDepartmentByName() {
        Department d1 = new Department();
        d1.setName("Engineering");
        d1.setDescription("Handles all technical projects");
        departmentRepository.save(d1);

        Department d2 = new Department();
        d2.setName("Finance");
        d2.setDescription("Handles company finances");
        departmentRepository.save(d2);

        List<Department> found = departmentRepository.findDepartmentByByName("engine");
        assertEquals(1, found.size());
        assertEquals("Engineering", found.get(0).getName());
    }
}
