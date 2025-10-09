package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    //Find Employee
    List<Employee> findByNameContainingIgnoreCase(String name);
}
