package com.Darum.Employee.Management.System.Entites;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class DepartmentEntityTest {

@Test
void testDepartmentFieldsAndLists() {
    Department dept = new Department();
    dept.setName("IT Department");
    dept.setDescription("Handles tech operations");
    dept.setEmployees(new ArrayList<>());
    dept.setManagers(new ArrayList<>());

    assertEquals("IT Department", dept.getName());
    assertEquals("Handles tech operations", dept.getDescription());
    assertNotNull(dept.getEmployees());
    assertNotNull(dept.getManagers());
    assertTrue(dept.getEmployees().isEmpty());
}
}

