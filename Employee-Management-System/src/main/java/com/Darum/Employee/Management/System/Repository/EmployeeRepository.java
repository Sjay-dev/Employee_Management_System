package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Model.Employee;
import com.Darum.Employee.Management.System.Model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    public Employee findEmployeeByFullNameAndPassword(String fullName, String password);

    public Employee findEmployeeByName(String fullName);

    public Employee findEmployeeByEmail(String email);

    public Optional<Employee> findEmployeeByEmailOpt(String email);

}
