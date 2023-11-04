package com.backend.server.companies;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {


    // Tuskin tarvitsee mitään, kaikki muut luokat sidotaan companyyn
    
}
