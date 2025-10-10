package com.Darum.Employee.Management.System.Service.Impl;

import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.Leave;
import com.Darum.Employee.Management.System.Entites.Manager;
import com.Darum.Employee.Management.System.Repository.EmployeeRepository;
import com.Darum.Employee.Management.System.Repository.LeaveRepository;
import com.Darum.Employee.Management.System.Repository.ManagerRepository;
import com.Darum.Employee.Management.System.Service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private EmployeeRepository employeeRepository;
    @Autowired
    private LeaveRepository leaveRepository;
    @Autowired
    private ManagerRepository managerRepository;

    @Override
    public Leave leaveApplicationByEmployee(Leave leave, Long employeeId) {

        Employee employee = employeeRepository.findById(employeeId).orElse(null);

        if (employee != null) {
            leave.setEmployee(employee);
            leave.setStatus("Pending");
            return leaveRepository.save(leave);
        }
        return null;
    }

    @Override
    public List<Leave> leavesByEmployee(Long employeeId) {
        return leaveRepository.findLeavesByEmployeeId(employeeId);
    }

    @Override
    public List<Leave> pendingLeavesRequests() {
        return leaveRepository.findLeavesByStatus("Pending");
    }

    @Override
    public Leave leaveApplicationByManager(Leave leave, Long managerId) {
        Manager manager = managerRepository.findById(managerId).orElse(null);

        if (manager != null) {
            leave.setManager(manager);
            leave.setStatus("Pending");
            return leaveRepository.save(leave);
        }
        return null;
    }

    @Override
    public List<Leave> leavesByManager(Long managerId) {
        return leaveRepository.findLeavesByManagerId(managerId);
    }

    @Override
    public String updateLeaveStatus(long leaveId, String status) {

        Leave leave = leaveRepository.findById(leaveId).orElse(null);
        if (leave != null) {
            leave.setStatus(status);
            leaveRepository.save(leave);
            return "Leave status has been updated";
        }
           else{
               return "Leave application not found";
        }
    }
}
