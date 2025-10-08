package com.Darum.Employee.Management.System.Repository;


import com.Darum.Employee.Management.System.Model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LeaveRepository extends JpaRepository<Leave,Long> {

}
