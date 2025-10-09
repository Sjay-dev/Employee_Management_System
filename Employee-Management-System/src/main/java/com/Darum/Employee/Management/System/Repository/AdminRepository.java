package com.Darum.Employee.Management.System.Repository;


import com.Darum.Employee.Management.System.Model.Admin;
import org.springframework.data.jpa.repository.JpaRepository;


public interface AdminRepository extends JpaRepository<Admin, Long> {

    public Admin findByFullNameAndPassword(String fullName, String password);
}
