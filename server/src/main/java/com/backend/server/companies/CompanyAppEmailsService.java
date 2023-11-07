package com.backend.server.companies;

import java.util.List;

import org.springframework.stereotype.Service;

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

    public String deleteCompanyApprovedEmails(Long id) {
        compAppEmailsRepository.deleteById(id);
        return "Company Approved Emails removed !! " + id;
    }
    
    public List<CompanyApprovedEmails> getCompanyApprovedEmails() {
        return compAppEmailsRepository.findAll();
    }
}
