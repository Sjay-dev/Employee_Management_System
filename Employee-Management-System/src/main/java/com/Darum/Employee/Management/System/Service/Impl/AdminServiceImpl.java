package com.Darum.Employee.Management.System.Service.Impl;

import com.Darum.Employee.Management.System.Entites.*;
import com.Darum.Employee.Management.System.Event.Enum.Event;
import com.Darum.Employee.Management.System.Event.KafkaEvent;
import com.Darum.Employee.Management.System.Repository.*;
import com.Darum.Employee.Management.System.Service.AdminService;
import jakarta.transaction.Transactional;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;



@Service
@Transactional
@RequiredArgsConstructor
@Slf4j
public class AdminServiceImpl implements AdminService {

    private final AdminRepository adminRepository;
    private final UserRepository userRepository;




    @Override
    public Admin addAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Override
    public Admin getAdminById(Long adminId) {
        return adminRepository.findById(adminId).orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    @Override
    public Admin getAdminByEmail(String email) {
        return adminRepository.findAdminByEmail(email).orElseThrow(() -> new RuntimeException("Admin not found"));
    }

    @Override
    public List<Admin> getAdminByName(String name) {
        return adminRepository.findAdminByName(name);
    }

    @Override
    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public Admin updateAdmin(Long adminId, Admin adminDetails) {
        Admin existingAdmin = adminRepository.findById(adminId)
                .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + adminId));

        if (adminDetails.getFirstName() != null)
            existingAdmin.setFirstName(adminDetails.getFirstName());

        if (adminDetails.getLastName() != null)
            existingAdmin.setLastName(adminDetails.getLastName());

        if (adminDetails.getEmail() != null)
            existingAdmin.setEmail(adminDetails.getEmail());

        if (adminDetails.getPassword() != null)
            existingAdmin.setPassword(adminDetails.getPassword());

        if (adminDetails.getRole() != null)
            existingAdmin.setRole(adminDetails.getRole());

        return adminRepository.save(existingAdmin);
    }


    @Override
    public void deleteAdmin(Long adminId) {
        if (!adminRepository.existsById(adminId)) {
            throw new RuntimeException("Admin not found");
        }
        adminRepository.deleteById(adminId);
    }



}

