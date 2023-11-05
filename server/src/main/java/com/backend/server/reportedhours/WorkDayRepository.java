package com.backend.server.reportedhours;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, Long> {
    
    List<WorkDay> findAllByUserId(Long id);

    @Query("SELECT wd FROM WorkDay wd JOIN wd.user u WHERE u.company.id = :companyId")
    List<WorkDay> findAllByCompanyId(Long companyId);
}
