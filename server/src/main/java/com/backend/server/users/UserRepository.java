package com.backend.server.users;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import com.backend.server.companies.Company;


public interface UserRepository extends JpaRepository<User, Long> {

    // hae sähköpostilla
    Optional <User> findByEmail(String email);

    List<User> findByCompany(Company company);

    // hae kaikki company id perusteella
    List<User> findAllByCompanyId(Long id);

} 