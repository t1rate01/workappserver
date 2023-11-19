package com.backend.server.companies;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.backend.server.users.User;

@Repository
public interface CompanyRepository extends JpaRepository<Company, Long> {


    // etsi companyname perusteella
    Company findByCompanyName(String companyName);

    // hae kaikki companyn työntekijät
    @Query(value = "SELECT * FROM users WHERE company_id = :companyId", nativeQuery = true)
    List<User> getAllWorkers(@Param("companyId") Long companyId);


    
    
}
