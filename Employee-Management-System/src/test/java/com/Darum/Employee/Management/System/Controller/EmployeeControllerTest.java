package com.Darum.Employee.Management.System.Controller;
import com.Darum.Employee.Management.System.Entites.Employee;
import com.Darum.Employee.Management.System.Security.JwtToken;
import com.Darum.Employee.Management.System.Service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class EmployeeControllerTest {

    @Mock private EmployeeService employeeService;
    @Mock private JwtToken jwtToken;

    @InjectMocks private EmployeeController employeeController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        // Initialize MockMvc for testing controller endpoints
        mockMvc = MockMvcBuilders.standaloneSetup(employeeController).build();
    }

    // ------------------------ SUCCESSFUL PROFILE FETCH ------------------------
    @Test
    void testGetMyProfileSuccess() throws Exception {
        Employee emp = new Employee();
        emp.setEmail("emp@test.com");

        when(jwtToken.validateToken("token")).thenReturn(true);
        when(jwtToken.getEmailFromToken("token")).thenReturn("emp@test.com");
        when(jwtToken.getRoleFromToken("token")).thenReturn("EMPLOYEE");
        when(employeeService.getEmployeeByEmail("emp@test.com")).thenReturn(emp);

        mockMvc.perform(get("/api/employees/me")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("emp@test.com"));
    }

    // ------------------------ INVALID TOKEN ------------------------
    @Test
    void testGetMyProfileInvalidToken() throws Exception {
        when(jwtToken.validateToken("bad-token")).thenReturn(false);

        mockMvc.perform(get("/api/employees/me")
                        .header("Authorization", "Bearer bad-token"))
                .andExpect(status().isUnauthorized());
    }



    // ------------------------ INVALID ROLE ------------------------
    @Test
    void testGetMyProfileInvalidRole() throws Exception {
        when(jwtToken.validateToken("token")).thenReturn(true);
        when(jwtToken.getEmailFromToken("token")).thenReturn("emp@test.com");
        when(jwtToken.getRoleFromToken("token")).thenReturn("MANAGER"); // Wrong role

        mockMvc.perform(get("/api/employees/me")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isForbidden());
    }

    // ------------------------ EMPLOYEE NOT FOUND ------------------------
    @Test
    void testGetMyProfileEmployeeNotFound() throws Exception {
        when(jwtToken.validateToken("token")).thenReturn(true);
        when(jwtToken.getEmailFromToken("token")).thenReturn("unknown@test.com");
        when(jwtToken.getRoleFromToken("token")).thenReturn("EMPLOYEE");
        when(employeeService.getEmployeeByEmail("unknown@test.com"))
                .thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND));

        mockMvc.perform(get("/api/employees/me")
                        .header("Authorization", "Bearer token"))
                .andExpect(status().isNotFound());
    }
}

