package com.backend.server.utility;

import org.springframework.stereotype.Component;

import com.backend.server.reportedhours.WorkDayService;
import com.backend.server.shifts.ShiftService;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;


// POISTAA VANHENTUNEET TIEDOT TIETOKANNASTA, AJASTETTU
@RequiredArgsConstructor
@Component
public class DataBaseCleaner {

    @Value("${keepWorkShiftsFor}")
    private int keepWorkShiftsFor;

    @Value("${keepWorkDaysFor}")
    private int keepWorkDaysFor;

    private final ShiftService shiftService;
    private final WorkDayService workDayService;

    @Scheduled(cron = "0 0 1 * * ?") // Ajetaan joka päivä klo 01:00
    public void cleanOldShifts() {
        shiftService.deleteOldShifts(keepWorkShiftsFor);
    }

    @Scheduled(cron = "0 0 2 * * ?") // Ajetaan joka päivä klo 02:00
    public void cleanOldWorkDays() {
        workDayService.deleteOldWorkDays(keepWorkDaysFor);
    }
    
}
