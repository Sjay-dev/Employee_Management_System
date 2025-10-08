package com.Darum.Employee.Management.System.Repository;

import com.Darum.Employee.Management.System.Model.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ManagerRepository extends JpaRepository<Manager,Long> {
}
