package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Entites.Admin;
import com.Darum.Employee.Management.System.Entites.Department;
import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.Enum.Role;
import com.Darum.Employee.Management.System.Entites.Manager;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
public class EntityPersistenceTest {
    @Autowired
    private EntityManager entityManager;

    @Test
    void testAdminPersistence() {
        Admin admin = new Admin();
        admin.setEmail("admin@mail.com");
        admin.setPassword("adminpass");
        admin.setFirstName("Joseph");
        admin.setLastName("Sanusi");
        admin.setPhoneNumber("08012345678");

        entityManager.persist(admin);
        entityManager.flush();

        assertNotNull(admin.getUserId());
        assertEquals(Role.ADMIN, admin.getRole());
    }

    @Test
    void testDepartmentAndManagerEmployeeRelationship() {
        Department dept = new Department();
        dept.setName("Engineering");
        dept.setDescription("Software Department");
        entityManager.persist(dept);

        Manager mgr = new Manager();
        mgr.setEmail("manager@mail.com");
        mgr.setPassword("managerpass");
        mgr.setFirstName("Jane");
        mgr.setLastName("Doe");
        mgr.setPhoneNumber("09011112222");
        mgr.setGender("Female");
        mgr.setDateOfBirth(LocalDate.of(1990, 1, 1));
        mgr.setDepartment(dept);
        entityManager.persist(mgr);

        Employee emp = new Employee();
        emp.setEmail("emp@mail.com");
        emp.setPassword("emppass");
        emp.setFirstName("John");
        emp.setLastName("Smith");
        emp.setPhoneNumber("08099998888");
        emp.setGender("Male");
        emp.setDateOfBirth(LocalDate.of(1995, 6, 12));
        emp.setManager(mgr);
        emp.setDepartment(dept);
        entityManager.persist(emp);

        entityManager.flush();
        entityManager.clear();

        Department foundDept = entityManager.find(Department.class, dept.getDepartmentId());
        assertEquals(1, foundDept.getManagers().size());
        assertEquals(1, foundDept.getEmployees().size());

        Manager foundMgr = entityManager.find(Manager.class, mgr.getUserId());
        assertEquals(Role.MANAGER, foundMgr.getRole());
        assertEquals("Engineering", foundMgr.getDepartment().getName());
    }

}
