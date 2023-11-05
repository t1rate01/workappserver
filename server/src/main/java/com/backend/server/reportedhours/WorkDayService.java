package com.backend.server.reportedhours;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.backend.server.security.SecurityService;
import com.backend.server.users.User;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkDayService {
    private final WorkDayRepository workDayRepository;
    private final SecurityService securityService;


    public WorkDay saveWorkDay(WorkDay workDay) {
        return workDayRepository.save(workDay);
    }

    public WorkDay getWorkDayById(Long id) {
        return workDayRepository.findById(id).orElse(null);
    }

    public String deleteWorkDay(Long id) {
        workDayRepository.deleteById(id);
        return "WorkDay removed !! " + id;
    }

    public List<WorkDay> getWorkDaysById(Long id) {
        return workDayRepository.findAllByUserId(id);
    }

    @Transactional
    public WorkDay addShift(String token, LocalDate date, LocalTime startTime, 
                            LocalTime endTime, Integer breaksTotal, String description) {
        // käyttäjä tokenista
        User user = securityService.getUserFromToken(token);

        // Tarkista onko päivälle jo olemassa entry
        Optional<WorkDay> existingWorkDay = workDayRepository.findByUserAndDate(user, date);

        WorkDay workDay;
        if(existingWorkDay.isPresent()) {
            // Jos olemassa, päivitä olemassa oleva entry
            workDay = existingWorkDay.get();
            // Ehkä tarkistus jos päivitetään vuoroa joka on aloitettu mutta ei ole lopetettu ? 
            
        } else {
            // Else tee uusi työpäiväentry
            workDay = new WorkDay();
            workDay.setUser(user);
            workDay.setDate(date);
        }

        // Loput tiedot
        workDay.setStartTime(startTime);
        workDay.setEndTime(endTime);
        workDay.setBreaksTotal(breaksTotal);
        workDay.setDescription(description);

        // Tallennus
        return workDayRepository.save(workDay);
    }

    @Transactional
    public WorkDay updateShift(String token, LocalDate date, LocalTime startTime, 
                               LocalTime endTime, Integer breaksTotal, String description) {
        // käyttäjä tokenista
        User user = securityService.getUserFromToken(token);

        // Tarkista onko päivälle jo olemassa entry
        Optional<WorkDay> existingWorkDay = workDayRepository.findByUserAndDate(user, date);

        WorkDay workDay;
        if(existingWorkDay.isPresent()) {
            // Jos olemassa, päivitä olemassa oleva entry
            workDay = existingWorkDay.get();
            // Ehkä tarkistus jos päivitetään vuoroa joka on aloitettu mutta ei ole lopetettu
            
        } else {
            // Else tee uusi työpäiväentry
            workDay = new WorkDay();
            workDay.setUser(user);
            workDay.setDate(date);
        }

        // Loput tiedot
        workDay.setStartTime(startTime);
        workDay.setEndTime(endTime);
        workDay.setBreaksTotal(breaksTotal);
        workDay.setDescription(description);

        // Tallennus
        return workDayRepository.save(workDay);
    }

    public List<WorkDay> getUserShifts(User user) {  // OVERLOAD, Hae default määrä 31
        return getUserShifts(user, 31);
    }

    public List<WorkDay> getUserShifts(User user, Integer limit) {  // HAE RAJALLINEN MÄÄRÄ VUOROJA
        return workDayRepository.findLastShiftsForUser(user.getId(), limit);
    }
    
    
}
