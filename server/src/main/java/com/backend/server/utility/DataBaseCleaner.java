package com.backend.server.utility;

import org.springframework.stereotype.Component;

import com.backend.server.companies.Company;
import com.backend.server.companies.CompanyService;
import com.backend.server.reportedhours.WorkDayService;
import com.backend.server.shifts.ShiftService;


import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Map;

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

    @Value("${workShiftKeepMax}")
    private int workShiftKeepMax;

    @Value("${workDayKeepMax}")
    private int workDayKeepMax;

    private final ShiftService shiftService;
    private final WorkDayService workDayService;
    private final CompanyService companyService;


    // käydään jokainen company läpi, katsotaan onko companyllä oma setting tiedonpoistolle, jos ei niin käytetään defaulttia
    @Scheduled(cron = "0 0 3 * * ?") // Ajetaan joka päivä klo 03:00
    public void cleanOldWorkDays() {
        List<Company> companies = companyService.getCompanies();
    
        for (Company company : companies) {
            int keepReportsFor = keepWorkDaysFor;  // alustetaan default arvolla
            Map<String, Object> settings = company.getSettings();
    
            if(settings != null && settings.containsKey("keepWorkDaysFor")) { // jos settingseissä keepWorkDaysFor
                Object value = settings.get("keepWorkDaysFor"); // arvon haku
                if(value instanceof Number) {   // tarkistetaan että on numero, runtime turvaamiseksi
                    keepReportsFor = ((Number) value).intValue();  // asetetaan arvo
                    // maximitarkistus
                    if(keepReportsFor > workDayKeepMax) {
                        keepReportsFor = workDayKeepMax;
                    }
                }
            }
            workDayService.deleteOldWorkDays(keepReportsFor, company);
            // console log
            System.out.println("Old workdays deleted for company: " + company.getCompanyName());
        }
    }
    

    @Scheduled(cron = "0 0 2 * * ?") // Ajetaan joka päivä klo 02:00
    public void cleanOldShifts() {
        List<Company> companies = companyService.getCompanies();
        
        for (Company company : companies) {
            int keepShiftsFor = keepWorkShiftsFor;  // alustetaan default arvolla
            Map<String, Object> settings = company.getSettings();

            if(settings != null && settings.containsKey("keepWorkShiftsFor")) { // jos settingseissä keepWorkShiftsFor
                Object value = settings.get("keepWorkShiftsFor"); // arvon haku
                if(value instanceof Number) {   // tarkistetaan että on numero, runtime turvaamiseksi
                    keepShiftsFor = ((Number) value).intValue();  // asetetaan arvo
                    // maximitarkistus
                    if(keepShiftsFor > workShiftKeepMax) {
                        keepShiftsFor = workShiftKeepMax;
                    }
                }
            }
            shiftService.deleteOldShifts(keepShiftsFor, company);
            // console log 
            System.out.println("Old shifts deleted for company: " + company.getCompanyName());
        }
    }
    
}
