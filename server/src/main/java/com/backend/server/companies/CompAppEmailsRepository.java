package com.backend.server.companies;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompAppEmailsRepository extends JpaRepository<CompanyApprovedEmails, Long> {
    

    // hae sähköpostilla 
    Optional<CompanyApprovedEmails> findByEmail(String email);

    // poista sähköpostilla
    void deleteByEmail(String email);

  
}
