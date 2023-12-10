package com.backend.server.reportedhours;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;




@Repository
public interface WorkDayRepository extends JpaRepository<WorkDay, Long> {
    
    List<WorkDay> findAllByUserId(Long userId);

    @Query(value = "SELECT * FROM reported_hours WHERE user_id = :userId ORDER BY date DESC LIMIT :limit", nativeQuery = true)
    List<WorkDay> findLastShiftsForUser(@Param("userId") Long userId, @Param("limit") int limit);


    @Query("SELECT wd FROM WorkDay wd JOIN wd.user u WHERE u.company.id = :companyId")
    List<WorkDay> findAllByCompanyId(Long companyId);

    @Query(value = "SELECT * FROM reported_hours WHERE user_id = :userId AND date = :date ORDER BY date DESC LIMIT 1", nativeQuery = true)
    Optional<WorkDay> findByUserAndDate(@Param("userId") Long userId, @Param("date") LocalDate date);

    // poista vanhentuneet workdayt cutoff päivän jälkeen, companyn perusteella, nativequeryllä
    @Query(value = "DELETE FROM reported_hours WHERE date < :date AND user_id IN (SELECT id FROM users WHERE company_id = :companyId)", nativeQuery = true)
    void deleteOldReports(@Param("date") LocalDate date, @Param("companyId") Long companyId);

    // hae kaikki companyn käyttäjien raportoidut vuorot
    @Query("SELECT wd FROM WorkDay wd JOIN wd.user u WHERE u.company.id = :companyId")
    List<WorkDay> findAllByUserIn(@Param("companyId") Long companyId);

    // hae kaikki companyn käyttäjien raportoidut vuorot, paitsi käyttäjän omat
    @Query("SELECT wd FROM WorkDay wd JOIN wd.user u WHERE u.company.id = :companyId AND u.id != :userId")
    List<WorkDay> findAllByUserExcludingUser(@Param("userId") Long userId, @Param("companyId") Long companyId);
}
