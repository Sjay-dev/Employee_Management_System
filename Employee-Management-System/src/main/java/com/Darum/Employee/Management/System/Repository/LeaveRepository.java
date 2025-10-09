package com.Darum.Employee.Management.System.Repository;


import com.Darum.Employee.Management.System.Model.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LeaveRepository extends JpaRepository<Leave,Long> {

    public List<Leave> findLeavesByEmployeeId(Long employeeId);
    public List<Leave> findLeavesByStatus(String status);

    public List<Leave> findLeavesByManagerId(Long managerId);


}
