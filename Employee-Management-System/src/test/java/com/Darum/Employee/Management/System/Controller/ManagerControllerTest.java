package com.Darum.Employee.Management.System.Controller;

import com.Darum.Employee.Management.System.Entites.Department;
import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Entites.Manager;
import com.Darum.Employee.Management.System.Security.JwtToken;
import com.Darum.Employee.Management.System.Service.EmployeeService;
import com.Darum.Employee.Management.System.Service.ManagerService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class ManagerControllerTest {

    @Mock private EmployeeService employeeService;
    @Mock private ManagerService managerService;
    @Mock private JwtToken jwtToken;

    @InjectMocks private ManagerController managerController;

    private MockMvc mockMvc;

    @Test
    void testGetEmployeesInDepartment_Success() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(managerController).build();

        Department dept = new Department();
        dept.setDepartmentId(1L);

        Manager manager = new Manager();
        manager.setDepartment(dept);

        Employee emp = new Employee();
        emp.setFirstName("John");

        when(jwtToken.validateToken("token")).thenReturn(true);
        when(jwtToken.getRoleFromToken("token")).thenReturn("MANAGER");
        when(jwtToken.getEmailFromToken("token")).thenReturn("manager@test.com");
        when(managerService.getManagerByEmail("manager@test.com")).thenReturn(manager);
        when(employeeService.getAllEmployeesByDepartmentId(1L)).thenReturn(List.of(emp));

        mockMvc.perform(get("/api/manager/department/employees")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void testGetEmployeesInDepartment_Forbidden() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(managerController).build();

        when(jwtToken.validateToken("token")).thenReturn(true);
        when(jwtToken.getRoleFromToken("token")).thenReturn("EMPLOYEE"); // Not manager

        mockMvc.perform(get("/api/manager/department/employees")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isForbidden());
    }
}
