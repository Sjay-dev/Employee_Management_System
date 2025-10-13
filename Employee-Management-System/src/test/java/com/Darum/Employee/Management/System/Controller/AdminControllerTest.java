package com.Darum.Employee.Management.System.Controller;

import com.Darum.Employee.Management.System.Entites.Admin;
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

import static org.mockito.ArgumentMatchers.any;
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

    @Test
    void testGetMyProfileSuccess() throws Exception {
        Admin admin = new Admin();
        admin.setEmail("admin@test.com");

        lenient().when(jwtToken.validateToken("valid-token")).thenReturn(true);
        lenient().when(jwtToken.getEmailFromToken("valid-token")).thenReturn("admin@test.com");
        lenient().when(adminService.getAdminByEmail("admin@test.com")).thenReturn(admin);

        mockMvc.perform(get("/api/admin/me")
                        .header("Authorization", "Bearer valid-token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("admin@test.com"));
    }

    @Test
    void testCreateAdminForbidden() throws Exception {
        Admin newAdmin = new Admin();
        newAdmin.setEmail("new@admin.com");

        lenient().when(jwtToken.validateToken("token")).thenReturn(true);
        lenient().when(jwtToken.getRoleFromToken("token")).thenReturn("MANAGER"); // Not ADMIN

        mockMvc.perform(post("/api/admin/create")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAdmin)))
                .andExpect(status().isForbidden());
    }

    @Test
    void testCreateAdminSuccess() throws Exception {
        Admin newAdmin = new Admin();
        newAdmin.setEmail("new@admin.com");

        lenient().when(jwtToken.validateToken("token")).thenReturn(true);
        lenient().when(jwtToken.getRoleFromToken("token")).thenReturn("ADMIN");
        lenient().when(adminService.addAdmin(any())).thenReturn(newAdmin);

        mockMvc.perform(post("/api/admin/create")
                        .header("Authorization", "Bearer token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newAdmin)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("new@admin.com"));
    }
}
