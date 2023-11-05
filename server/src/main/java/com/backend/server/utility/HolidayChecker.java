package com.backend.server.utility;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
@Service
public class HolidayChecker {

    @Value("${useHolidayApi}")
    private Boolean useHolidayApi;  // TODO: holiday checker api, default false jollon app.props arvojen kanssa mennään
    
    @Value("${holidays.fixed}")
    private String fixedHolidays;

    @Value("${holidays.variable}")
    private String variableHolidays;

    @Value("${holidayChecker.apiKey}")
    private String API_KEY;

    @Value("${holidayChecker.url}")
    private String API_URL;

    public Boolean isHoliday(LocalDate date){  // TODO: testaukset
        // Tarkista ensin onko sunnuntai
        if (date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            return true;
        }
    
        // App propertiesissa voi vaihtaa käytetäänkö API:a vai ei
        if(useHolidayApi) {
            // TODO: API koodit
            return false;
        } else {
            Set<LocalDate> holidays = new HashSet<>();
    
            // Kerätään kovakoodatut vakio pyhäpäivät propertiesistä
            for (String fixedHoliday : fixedHolidays.split(",")) {
                LocalDate holiday = LocalDate.parse(date.getYear() + "-" + fixedHoliday);
                holidays.add(holiday);
            }
    
            // Kerätään kovakoodatut muuttuvat pyhäpäivät propertiesistä
            for (String variableHoliday : variableHolidays.split(",")) {
                LocalDate holiday = LocalDate.parse(date.getYear() + "-" + variableHoliday);
                holidays.add(holiday);
            }

    
            // Tarkistus
            return holidays.contains(date);
        }
    }
    
}
