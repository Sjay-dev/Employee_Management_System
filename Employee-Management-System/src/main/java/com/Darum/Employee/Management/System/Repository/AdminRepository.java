package com.Darum.Employee.Management.System.Repository;


import com.Darum.Employee.Management.System.Entites.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface AdminRepository extends JpaRepository<Admin, Long> {

    public Admin findAdminByEmail(String email);

    @Query("SELECT a FROM Admin a WHERE LOWER(a.firstName) LIKE LOWER(CONCAT('%', :name, '%')) " +
            "OR LOWER(a.lastName) LIKE LOWER(CONCAT('%', :name, '%'))")
     public List<Admin> getAdminByName(String name);
}
