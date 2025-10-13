package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Entites.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    //Search(name)
    @Query("SELECT e from Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(e.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
    public List<Employee> findEmployeeByName(String name);


    public Optional<Employee> findEmployeeByEmail(String email);

    public List<Employee> findByDepartment_DepartmentId(Long departmentId);

    public List<Employee> findByManager_UserId(Long managerId);



}
