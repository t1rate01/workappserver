package com.backend.server.shifts;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface ShiftRepository extends JpaRepository<Shift, Long>{

    List<Shift> findAllByUserId(Long userId);

    @Query(value = "SELECT * FROM shifts WHERE user_id = :userId ORDER BY date DESC LIMIT :limit", nativeQuery = true)
    List<Shift> findLastShiftsForUser(Long userId, int limit);

    @Query("SELECT s FROM Shift s JOIN s.user u WHERE u.company.id = :companyId")
    List<Shift> findAllByCompanyId(Long companyId);

    // kaikki companyn tulevat vuorot
    @Query(value = "SELECT * FROM shifts WHERE user_id IN (SELECT id FROM users WHERE company_id = :companyId) AND date >= :date ORDER BY date ASC", nativeQuery = true)
    List<Shift> findAllFutureShiftsByCompanyId(@Param("companyId") Long companyId, @Param("date") LocalDate date);

    @Query(value = "SELECT * FROM shifts WHERE user_id = :userId AND date = :date AND user_id IN (SELECT id FROM users WHERE company_id = :companyId) ORDER BY date DESC LIMIT 1", nativeQuery = true)
    Optional<Shift> findByUserAndDate(@Param("userId") Long userId, @Param("date") LocalDate date, @Param("companyId") Long companyId);

    // kaikki käyttäjän tänään ja tulevat vuorot
    @Query(value = "SELECT * FROM shifts WHERE user_id = :userId AND date >= :date AND user_id IN (SELECT id FROM users WHERE company_id = :companyId) ORDER BY date ASC", nativeQuery = true)
    List<Shift> findFutureShiftsByUserId(@Param("userId") Long userId, @Param("date") LocalDate date, @Param("companyId") Long companyId);

    // poista vanhentuneet shiftit cutoff päivän jälkeen, companyId perusteella, nativequeryllä
    @Query(value = "DELETE FROM shifts WHERE date < :date AND user_id IN (SELECT id FROM users WHERE company_id = :companyId)", nativeQuery = true)
    void deleteOldShifts(@Param("date") LocalDate date, @Param("companyId") Long companyId);

    // hae kaikki companyn käyttäjien tulevat vuorot, paitsi omat
    @Query("SELECT s FROM Shift s JOIN s.user u WHERE u.company.id = :companyId AND u.id != :userId AND s.date >= :date")
    List<Shift> findAllByUserExcludingUser(@Param("userId") Long userId, @Param("companyId") Long companyId, @Param("date") LocalDate date);

    // hae kaikki companyn käyttäjien menneet ja tulevat vuorot, paitsi omat
    @Query("SELECT s FROM Shift s JOIN s.user u WHERE u.company.id = :companyId AND u.id != :userId")
    List<Shift> findAllByUserExcludingUserNoDate(@Param("userId") Long userId, @Param("companyId") Long companyId);
    

    
    
}
