package com.backend.server.companies;


import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.server.users.User;
import com.backend.server.users.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;


@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;
    private final UserRepository userRepository;

    
    public Company saveCompany(Company company) {
        return companyRepository.save(company);
    }

    public Company updateCompany(Company company) {
        Company existingCompany = companyRepository.findById(company.getId()).orElse(null);
        existingCompany.setCompanyName(company.getCompanyName());
        existingCompany.setSettings(company.getSettings());
        return companyRepository.save(existingCompany);
    }

    public Company getCompanyById(Long id) {
        return companyRepository.findById(id).orElse(null);
    }

    public List<Company> getCompanies() {
        return companyRepository.findAll();
    }

    public String deleteCompany(Long id) {
        companyRepository.deleteById(id);
        return "Company removed !! " + id;
    }

    public Company getCompanyByCompanyName(String companyName) {
        return companyRepository.findByCompanyName(companyName);
    }

    @Transactional
    public List<User> getAllWorkers(Company company) {
        return userRepository.findByCompany(company);
    }

    // Lisää jos tarvii, esim update
}
