package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Entites.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {


    @Query("SELECT e from Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    public List<Employee> findEmployeeByName(String name);

    public List<Employee> findEmployeeByDepartment_DepartmentId(String departmentId);

    public List<Employee> findEmployeeByManager_UserId(String managerId);

    public Optional<Employee> findEmployeeByEmail(String email);

}
