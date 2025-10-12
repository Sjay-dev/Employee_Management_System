package com.Darum.Employee.Management.System.Service;

import com.Darum.Employee.Management.System.Entites.Department;
import org.springframework.stereotype.Service;

import java.util.List;

public interface DepartmentService {
    public Department addDepartment(Department department);

    public Department getDepartmentById(Long departmentId);

    public List<Department> getAllDepartments();

    public List<Department> getAllDepartmentsByName(String name);

    public Department updateDepartmentDetails(Long departmentId , Department department);

    public void deleteDepartment(Long departmentId);
}
