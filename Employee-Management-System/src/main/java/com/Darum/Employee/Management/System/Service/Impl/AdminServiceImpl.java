package com.Darum.Employee.Management.System.Service.Impl;

import com.Darum.Employee.Management.System.Entites.*;
import com.Darum.Employee.Management.System.Event.Enum.Event;
import com.Darum.Employee.Management.System.Event.KafkaEvent;
import com.Darum.Employee.Management.System.Repository.*;
import com.Darum.Employee.Management.System.Service.AdminService;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;



@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;

    // ------------------------- ADMIN CRUD METHODS -------------------------

    /**
     * Add a new admin.
     */
    @Override
    public Admin addAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    /**
     * Get an admin by their ID.
     */
    @Override
    public Admin getAdminById(Long adminId) {
        return adminRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Admin not found with ID: " + adminId
                ));
    }

    /**
     * Get an admin by their email.
     */
    @Override
    public Admin getAdminByEmail(String email) {
        return adminRepository.findAdminByEmail(email)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Admin not found with email: " + email
                ));
    }

    /**
     * Get admins by name (could return multiple admins).
     */
    @Override
    public List<Admin> getAdminByName(String name) {
        List<Admin> admins = adminRepository.findAdminByName(name);
        if (admins.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "No admins found with name: " + name);
        }
        return admins;
    }

    /**
     * Get all admins.
     */
    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    /**
     * Update an existing admin's details.
     */
    @Override
    public Admin updateAdmin(Long adminId, Admin adminDetails) {
        Admin existingAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Admin not found with ID: " + adminId
                ));

        if (adminDetails.getFirstName() != null) existingAdmin.setFirstName(adminDetails.getFirstName());
        if (adminDetails.getLastName() != null) existingAdmin.setLastName(adminDetails.getLastName());
        if (adminDetails.getEmail() != null) existingAdmin.setEmail(adminDetails.getEmail());
        if (adminDetails.getPassword() != null) existingAdmin.setPassword(adminDetails.getPassword());
        if (adminDetails.getRole() != null) existingAdmin.setRole(adminDetails.getRole());

        return adminRepository.save(existingAdmin);
    }

    /**
     * Delete an admin by ID.
     */
    @Override
    public void deleteAdmin(Long adminId) {
        if (!adminRepository.existsById(adminId)) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                    "Admin not found with ID: " + adminId);
        }
        adminRepository.deleteById(adminId);
    }
}



