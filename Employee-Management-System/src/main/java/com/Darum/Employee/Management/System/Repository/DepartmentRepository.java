package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Entites.Department;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends JpaRepository<Department, Long> {

    @Query("SELECT d FROM Department d WHERE LOWER(d.name) LIKE LOWER(CONCAT('%', :name, '%')) ")
    public List<Department> findDepartmentByByName(String name);
}
