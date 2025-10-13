package com.Darum.Employee.Management.System.ServiceImpl;

import com.Darum.Employee.Management.System.Entites.Admin;
import com.Darum.Employee.Management.System.Repository.AdminRepository;
import com.Darum.Employee.Management.System.Repository.UserRepository;
import com.Darum.Employee.Management.System.Service.Impl.AdminServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceImplTest {

    @Mock
    private AdminRepository adminRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AdminServiceImpl adminService;

    /**
     * Test adding a new admin.
     * Verifies that the repository save method is called and the returned admin is correct.
     */
    @Test
    void testAddAdmin() {
        Admin admin = new Admin();
        admin.setFirstName("Joseph");

        when(adminRepository.save(admin)).thenReturn(admin);

        Admin saved = adminService.addAdmin(admin);

        assertEquals("Joseph", saved.getFirstName());
        verify(adminRepository, times(1)).save(admin);
    }

    /**
     * Test fetching an admin by ID when the admin exists.
     */
    @Test
    void testGetAdminById_Found() {
        Admin admin = new Admin();
        admin.setUserId(1L);

        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        Admin found = adminService.getAdminById(1L);

        assertEquals(1L, found.getUserId());
    }

    /**
     * Test fetching an admin by ID when the admin does not exist.
     * Should throw a RuntimeException (or ResponseStatusException in the actual service).
     */
    @Test
    void testGetAdminById_NotFound() {
        when(adminRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(RuntimeException.class, () -> adminService.getAdminById(1L));
    }

    /**
     * Test updating an existing admin's details.
     * Verifies that only updated fields are changed and save is called once.
     */
    @Test
    void testUpdateAdmin() {
        Admin existing = new Admin();
        existing.setUserId(1L);
        existing.setFirstName("OldName");

        Admin updates = new Admin();
        updates.setFirstName("NewName");

        when(adminRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(adminRepository.save(any())).thenReturn(existing);

        Admin updated = adminService.updateAdmin(1L, updates);

        assertEquals("NewName", updated.getFirstName());
        verify(adminRepository, times(1)).save(existing);
    }

    /**
     * Test deleting an existing admin.
     * Verifies that deleteById is called once when admin exists.
     */
    @Test
    void testDeleteAdmin() {
        when(adminRepository.existsById(1L)).thenReturn(true);
        adminService.deleteAdmin(1L);
        verify(adminRepository, times(1)).deleteById(1L);
    }

    /**
     * Test deleting a non-existent admin.
     * Should throw a RuntimeException (or ResponseStatusException in the actual service).
     */
    @Test
    void testDeleteAdmin_NotFound() {
        when(adminRepository.existsById(1L)).thenReturn(false);
        assertThrows(RuntimeException.class, () -> adminService.deleteAdmin(1L));
    }
}

