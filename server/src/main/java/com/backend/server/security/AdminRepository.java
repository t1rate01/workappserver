package com.backend.server.security;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Long> {

    // poista emaililla perusteella
    void deleteByEmail(String email);

    // löydä emaililla
    Optional<Admin> findByEmail(String email);

    // etsi kaikki adminit
    List<Admin> findAll();
    
}
