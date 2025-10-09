package com.Darum.Employee.Management.System.Service;

import com.Darum.Employee.Management.System.Model.Employee;
import com.Darum.Employee.Management.System.Model.Leave;

import java.util.List;

public interface LeaveService {

    public Leave leaveApplicationByEmployee(Leave leave, Long employeeId);

    public List<Leave> leavesByEmployee(Long employeeId);
    public List<Leave> pendingLeavesRequests();

    public Leave leaveApplicationByManager(Leave leave, Long managerId);
    public List<Leave> leavesByManager(Long managerId);
    public String updateLeaveStatus(long leaveId, String status);
}
