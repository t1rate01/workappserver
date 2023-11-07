package com.backend.server.security;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AdminService {
    private final AdminRepository adminRepository;

    public Admin saveAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public void deleteAdminByEmail(String email) {
        adminRepository.deleteByEmail(email);
    }

    public void deleteAdminById(Long id) {
        adminRepository.deleteById(id);
    }

    public Admin getAdminById(Long id) {
        return adminRepository.findById(id).orElse(null);
    }



}
