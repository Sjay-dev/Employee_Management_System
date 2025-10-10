package com.Darum.Employee.Management.System.Repository;


import com.Darum.Employee.Management.System.Entites.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave,Long> {

    @Query("SELECT l from Leave l WHERE l.employee.employeeId = :employeeId")
    public List<Leave> findLeavesByEmployeeId(Long employeeId);

    @Query("SELECT l from Leave l WHERE l.status = :status ")
    public List<Leave> findLeavesByStatus(String status);

    @Query ("SELECT l from Leave l WHERE l.manager.managerId = :managerId")
    public List<Leave> findLeavesByManagerId(Long managerId);


}



