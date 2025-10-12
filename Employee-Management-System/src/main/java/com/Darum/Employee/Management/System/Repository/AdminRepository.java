package com.Darum.Employee.Management.System.Repository;


import com.Darum.Employee.Management.System.Entites.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;


public interface AdminRepository extends JpaRepository<Admin, Long> {

    @Query("SELECT a FROM Admin a WHERE LOWER(a.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
     public List<Admin> findAdminByName(String name);

    public Optional<Admin> findAdminByEmail(String email);

}
