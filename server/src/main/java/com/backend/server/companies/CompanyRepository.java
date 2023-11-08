package com.backend.server.companies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {


    // etsi companyname perusteella
    Company findByCompanyName(String companyName);
    
    
}
