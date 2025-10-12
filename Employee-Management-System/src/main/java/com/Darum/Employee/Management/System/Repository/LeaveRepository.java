package com.Darum.Employee.Management.System.Repository;


import com.Darum.Employee.Management.System.Entites.Leave;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface LeaveRepository extends JpaRepository<Leave,Long> {

     public Optional<Leave> findLeavesByEmployee_UserId (Long employeeId);

     public Optional<Leave> findLeaveByManager_UserId (Long managerId);

     public Optional<Leave> findLeaveByStartDate (LocalDate startDate);

     public Optional<Leave> findLeaveByEndDate (LocalDate endDate);




}



