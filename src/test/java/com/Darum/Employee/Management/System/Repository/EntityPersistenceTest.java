package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Entites.Admin;
import com.Darum.Employee.Management.System.Entites.Department;
import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.Enum.Role;
import com.Darum.Employee.Management.System.Entites.Enum.Status;
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

    /**
     * Test persisting an Admin entity.
     * Verifies that the entity gets an ID after persisting
     * and that the default role is correctly set to ADMIN.
     */
    @Test
    void testAdminPersistence() {
        Admin admin = new Admin();
        admin.setEmail("admin@mail.com");
        admin.setPassword("adminpass");
        admin.setFirstName("Joseph");
        admin.setLastName("Sanusi");
        admin.setPhoneNumber("08012345678");

        // Persist and flush to the database
        entityManager.persist(admin);
        entityManager.flush();

        // Assertions
        assertNotNull(admin.getUserId(), "Admin should have a generated ID after persisting");
        assertEquals(Role.ADMIN, admin.getRole(), "Admin role should be ADMIN by default");
    }

    /**
     * Test the relationships between Department, Manager, and Employee entities.
     * Verifies that persisting the entities establishes proper bidirectional links.
     */
    @Test
    void testDepartmentAndManagerEmployeeRelationship() {
        // Create and persist Department
        Department dept = new Department();
        dept.setName("Engineering");
        dept.setDescription("Software Department");
        entityManager.persist(dept);

        // Create and persist Manager linked to Department
        Manager mgr = new Manager();
        mgr.setEmail("manager@mail.com");
        mgr.setPassword("managerpass");
        mgr.setFirstName("Jane");
        mgr.setLastName("Doe");
        mgr.setPhoneNumber("09011112222");
        mgr.setGender("FEMALE");
        mgr.setDateOfBirth(LocalDate.of(1990, 1, 1));
        mgr.setDepartment(dept);
        mgr.setRole(Role.MANAGER);
        mgr.setStatus(Status.ACTIVE); // optional
        entityManager.persist(mgr);

        // Create and persist Employee linked to Manager and Department
        Employee emp = new Employee();
        emp.setEmail("emp@mail.com");
        emp.setPassword("emppass");
        emp.setFirstName("John");
        emp.setLastName("Smith");
        emp.setPhoneNumber("08099998888");
        emp.setGender("MALE");
        emp.setDateOfBirth(LocalDate.of(1995, 6, 12));
        emp.setManager(mgr);
        emp.setDepartment(dept);
        emp.setRole(Role.EMPLOYEE);
        emp.setStatus(Status.INACTIVE);
        entityManager.persist(emp);

        // Flush changes to DB and clear persistence context
        entityManager.flush();
        entityManager.clear();

        // Fetch Department and verify relationships
        Department foundDept = entityManager.find(Department.class, dept.getDepartmentId());
        assertEquals(1, foundDept.getManagers().size(), "Department should have 1 manager");
        assertEquals(1, foundDept.getEmployees().size(), "Department should have 1 employee");

        // Fetch Manager and verify role and department
        Manager foundMgr = entityManager.find(Manager.class, mgr.getUserId());
        assertEquals(Role.MANAGER, foundMgr.getRole(), "Manager role should be MANAGER");
        assertEquals("Engineering", foundMgr.getDepartment().getName(), "Manager should belong to Engineering");

        // Fetch Employee and verify role, manager, and first name
        Employee foundEmp = entityManager.find(Employee.class, emp.getUserId());
        assertEquals(Role.EMPLOYEE, foundEmp.getRole(), "Employee role should be EMPLOYEE");
        assertEquals("John", foundEmp.getFirstName(), "Employee first name should be John");
        assertEquals("Jane", foundEmp.getManager().getFirstName(), "Employee's manager first name should be Jane");
    }
}

