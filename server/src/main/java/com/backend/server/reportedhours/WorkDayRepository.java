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

    // poista vanhentuneet shiftit cutoff päivän jälkeen
    @Query(value = "DELETE FROM reported_hours WHERE date < :date", nativeQuery = true)
    void deleteOldShifts(@Param("date") LocalDate date);
}
