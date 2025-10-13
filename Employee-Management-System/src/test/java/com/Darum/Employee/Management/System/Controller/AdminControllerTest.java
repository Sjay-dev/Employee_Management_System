package com.Darum.Employee.Management.System.Controller;

import com.Darum.Employee.Management.System.Entites.Admin;
import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Security.JwtToken;
import com.Darum.Employee.Management.System.Service.AdminService;
import com.Darum.Employee.Management.System.Service.DepartmentService;
import com.Darum.Employee.Management.System.Service.EmployeeService;
import com.Darum.Employee.Management.System.Service.ManagerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.lenient;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock private EmployeeService employeeService;
    @Mock private ManagerService managerService;
    @Mock private AdminService adminService;
    @Mock private DepartmentService departmentService;
    @Mock private JwtToken jwtToken;

    @InjectMocks private AdminController adminController;

    private MockMvc mockMvc;
    private final ObjectMapper objectMapper = new ObjectMapper();

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    // ------------------- ADMIN PROFILE -------------------

    @Test
    void testUpdateMyProfileSuccess() throws Exception {
        // Setup existing admin
        Admin admin = new Admin();
        admin.setEmail("admin@test.com");

        Admin updateAdmin = new Admin();
        updateAdmin.setFirstName("Updated");

        lenient().when(jwtToken.validateToken("token")).thenReturn(true);
        lenient().when(jwtToken.getEmailFromToken("token")).thenReturn("admin@test.com");
        lenient().when(adminService.getAdminByEmail("admin@test.com")).thenReturn(admin);
        lenient().when(adminService.updateAdmin(any(), any())).thenReturn(updateAdmin);

        // Perform PUT request to update profile
        mockMvc.perform(put("/api/admin/me")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateAdmin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    // ------------------- ADMIN CRUD -------------------

    @Test
    void testGetAllAdminsSuccess() throws Exception {
        Admin admin = new Admin();
        admin.setEmail("admin@test.com");

        lenient().when(jwtToken.validateToken("token")).thenReturn(true);
        lenient().when(jwtToken.getRoleFromToken("token")).thenReturn("ADMIN");
        lenient().when(adminService.getAllAdmins()).thenReturn(List.of(admin));

        // Perform GET request to fetch all admins
        mockMvc.perform(get("/api/admin/all")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].email").value("admin@test.com"));
    }

    @Test
    void testGetAdminsByNameSuccess() throws Exception {
        Admin admin = new Admin();
        admin.setFirstName("John");

        lenient().when(adminService.getAdminByName("John")).thenReturn(List.of(admin));

        // Perform GET request with name query param
        mockMvc.perform(get("/api/admin/search").param("name", "John"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("John"));
    }

    @Test
    void testDeleteAdminSuccess() throws Exception {
        lenient().when(jwtToken.validateToken("token")).thenReturn(true);
        lenient().when(jwtToken.getRoleFromToken("token")).thenReturn("ADMIN");

        // Perform DELETE request
        mockMvc.perform(delete("/api/admin/1")
                        .header("Authorization", "Bearer token")
                        .param("adminId", "1"))
                .andExpect(status().isNoContent());
    }

    // ------------------- EMPLOYEE CRUD -------------------

    @Test
    void testCreateEmployeeSuccess() throws Exception {
        Employee employee = new Employee();
        employee.setEmail("employee@test.com");

        lenient().when(jwtToken.validateToken("token")).thenReturn(true);
        lenient().when(jwtToken.getRoleFromToken("token")).thenReturn("ADMIN");
        lenient().when(employeeService.addEmployee(any())).thenReturn(employee);

        mockMvc.perform(post("/api/admin/employees")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("employee@test.com"));
    }

    @Test
    void testUpdateEmployeeSuccess() throws Exception {
        Employee employee = new Employee();
        employee.setFirstName("Updated");

        lenient().when(jwtToken.validateToken("token")).thenReturn(true);
        lenient().when(jwtToken.getRoleFromToken("token")).thenReturn("ADMIN");
        lenient().when(employeeService.updateEmployee(eq(1L), any())).thenReturn(employee);

        mockMvc.perform(put("/api/admin/employees/1")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Updated"));
    }

    @Test
    void testDeleteEmployeeSuccess() throws Exception {
        lenient().when(jwtToken.validateToken("token")).thenReturn(true);
        lenient().when(jwtToken.getRoleFromToken("token")).thenReturn("ADMIN");

        mockMvc.perform(delete("/api/admin/employees/1")
                        .header("Authorization", "Bearer token")
                        .param("employeeId", "1"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testGetEmployeeSuccess() throws Exception {
        Employee employee = new Employee();
        employee.setEmail("employee@test.com");

        lenient().when(jwtToken.validateToken("token")).thenReturn(true);
        lenient().when(jwtToken.getRoleFromToken("token")).thenReturn("ADMIN");
        lenient().when(employeeService.getEmployeeById(1L)).thenReturn(employee);

        mockMvc.perform(get("/api/admin/employees/1")
                        .header("Authorization", "Bearer token")
                        .param("employeeId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("employee@test.com"));
    }
}

