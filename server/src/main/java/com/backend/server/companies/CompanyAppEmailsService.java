package com.backend.server.companies;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.backend.server.companies.DTO.ApprovedEmailsDTO;
import com.backend.server.users.User;
import com.backend.server.users.UserRepository;
import com.backend.server.utility.Role;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyAppEmailsService {
    private final CompAppEmailsRepository compAppEmailsRepository;
    private final UserRepository userRepository;

    public CompanyApprovedEmails saveCompanyApprovedEmails(CompanyApprovedEmails companyApprovedEmails) {
        return compAppEmailsRepository.save(companyApprovedEmails);
    }

    public CompanyApprovedEmails getCompanyApprovedEmailsById(Long id) {
        return compAppEmailsRepository.findById(id).orElse(null);
    }

    public void deleteCompanyApprovedEmails(Long id) {
        compAppEmailsRepository.deleteById(id);
    }

    public void deleteCompanyApprovedEmailsByEmail(String email) {
        compAppEmailsRepository.deleteByEmail(email);
    }
    
    public List<CompanyApprovedEmails> getCompanyApprovedEmails() {
        return compAppEmailsRepository.findAll();
    }

    public List<CompanyApprovedEmails> getCompanyApprovedEmailsByCompany(Company company) {
        return compAppEmailsRepository.findByCompany(company);
    }

    public CompanyApprovedEmails getCompanyApprovedEmailsByEmail(String email) {
        Optional<CompanyApprovedEmails> companyApprovedEmails = compAppEmailsRepository.findByEmail(email);
        if (companyApprovedEmails.isPresent()) {
            return companyApprovedEmails.get();
        }
        return null;
    }

    public void addEmail(Company company, String email, Role role) {
        CompanyApprovedEmails companyApprovedEmails = new CompanyApprovedEmails();
        companyApprovedEmails.setCompany(company);
        companyApprovedEmails.setEmail(email);
        companyApprovedEmails.setRole(role);
        compAppEmailsRepository.save(companyApprovedEmails);
    }
    
    public void updateRole(Long id, Role role) {
        CompanyApprovedEmails companyApprovedEmails = compAppEmailsRepository.findById(id).orElse(null);
        companyApprovedEmails.setRole(role);
        compAppEmailsRepository.save(companyApprovedEmails);
    }

    @Transactional
    public List<ApprovedEmailsDTO> getApprovedEmailsDTO(Company company){
        List<CompanyApprovedEmails> companyApprovedEmails = compAppEmailsRepository.findByCompany(company);
        List<ApprovedEmailsDTO> approvedEmailsDTOs = new ArrayList<>();
        for (CompanyApprovedEmails companyApprovedEmail : companyApprovedEmails) {
            ApprovedEmailsDTO approvedEmailsDTO = new ApprovedEmailsDTO();
            approvedEmailsDTO.setId(companyApprovedEmail.getId());
            approvedEmailsDTO.setEmail(companyApprovedEmail.getEmail());
            approvedEmailsDTO.setRole(companyApprovedEmail.getRole());
            approvedEmailsDTO.setRegistered(isEmailRegistered(companyApprovedEmail.getEmail()));
            approvedEmailsDTOs.add(approvedEmailsDTO);
        }
        return approvedEmailsDTOs;
    }

    public Boolean isEmailRegistered(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if (user.isPresent()) {
            return true;
        }
        return false;
    }
}