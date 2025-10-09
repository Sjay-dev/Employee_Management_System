package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ManagerRepository extends JpaRepository<Manager,Long> {

    public Manager findManagerByFullNameAndPassword(String fullName, String password);


    public Manager findManagerByFullName(String fullName);

    public Manager findManagerByEmail(String email);

    public Optional<Manager> findManagerByEmailOpt(String email);

}
