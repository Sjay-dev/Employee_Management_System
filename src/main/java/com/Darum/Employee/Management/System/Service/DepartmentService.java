package com.Darum.Employee.Management.System.Service;

import com.Darum.Employee.Management.System.Entites.Department;
import com.Darum.Employee.Management.System.Entites.Manager;

import java.util.List;

public interface DepartmentService {

    // Department CRUD
    public Department addDepartment(Department department);

    public Department getDepartmentById(Long departmentId);

    public List<Department> getAllDepartments();

    public List<Department> getAllDepartmentsByName(String name);

    public Department updateDepartmentDetails(Long departmentId, Department department);

    public void deleteDepartment(Long departmentId);

    // Manager operations
    public Department addExistingManagerToDepartment(Long departmentId, Long managerId);

    public Department removeManagerFromDepartment(Long departmentId, Long managerId);

    // Employee operations
    public Department addExistingEmployeeToDepartment(Long departmentId, Long employeeId);

    public Department removeEmployeeFromDepartment(Long departmentId, Long employeeId);
}

