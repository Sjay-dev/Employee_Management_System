package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Entites.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager,Long> {

    @Query("SELECT m FROM Manager m WHERE LOWER(m.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(m.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
        public List<Manager> findManagerByName(String name);

        Optional<Manager> findManagerByDepartment_DepartmentId (Long departmentId);

}
