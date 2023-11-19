package com.backend.server.companies;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.server.utility.Role;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CompanyAppEmailsService {
    private final CompAppEmailsRepository compAppEmailsRepository;

    public CompanyApprovedEmails saveCompanyApprovedEmails(CompanyApprovedEmails companyApprovedEmails) {
        return compAppEmailsRepository.save(companyApprovedEmails);
    }

    public CompanyApprovedEmails getCompanyApprovedEmailsById(Long id) {
        return compAppEmailsRepository.findById(id).orElse(null);
    }

    public void deleteCompanyApprovedEmails(Long id) {
        compAppEmailsRepository.deleteById(id);
    }
    
    public List<CompanyApprovedEmails> getCompanyApprovedEmails() {
        return compAppEmailsRepository.findAll();
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
}