package com.backend.server.shifts;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.backend.server.companies.Company;
import com.backend.server.shifts.DTO.ShiftDTO;
import com.backend.server.users.User;
import com.backend.server.users.UserRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ShiftService {
        private final ShiftRepository shiftRepository;
        private final UserRepository userRepository;

        public Shift saveShift(Shift shift) {
                return shiftRepository.save(shift);
        }

        public Shift getShiftById(Long id) {
                return shiftRepository.findById(id).orElse(null);
        }

        public String deleteShift(Long id) {
                shiftRepository.deleteById(id);
                return "Shift removed !! " + id;
        }

        public List<Shift> getShiftsById(Long id) {
                return shiftRepository.findAllByUserId(id);
        }

        @Transactional
        public List<Shift> getFutureShiftsById(Long id) {
                LocalDate date = LocalDate.now();
                Long companyId = userRepository.findById(id).get().getCompany().getId();
                return shiftRepository.findFutureShiftsByUserId(id, date, companyId);
        }

        @Transactional
        public ShiftDTO addShift(User worker, LocalDate date, LocalTime startTime, LocalTime endTime, Integer breaksTotal, String description, Company company){
                User user = worker;
                
                // Tarkista onko päivälle jo olemassa entry 
                Optional<Shift> existingShift = shiftRepository.findByUserAndDate(user.getId(), date, company.getId());
                
                Shift shift;
                if(existingShift.isPresent()){
                        // Jos olemassa, päivitä olemassa oleva entry
                        shift = existingShift.get();
                } else {
                        // Jos ei ole olemassa, luo uusi entry
                        shift = new Shift();
                        shift.setUser(user);
                        shift.setDate(date);
                }
                shift.setStartTime(startTime);
                if(endTime != null){  // koska annetaan mahdollisuus määrätä vuoro ilman lopetusaikaa
                        shift.setEndTime(endTime);
                }
                if(breaksTotal != null){ // koska annetaan mahdollisuus olla määrämättä taukojen määrää
                        shift.setBreaksTotal(breaksTotal);
                }
                if(description != null){ // koska annetaan mahdollisuus olla määrämättä kuvausta
                        shift.setDescription(description);
                }
                shift.setCompany(company);
                
                //tallennus 
                shiftRepository.save(shift);
                // palautus
                ShiftDTO shiftDTO = new ShiftDTO();
                shiftDTO.setDate(shift.getDate());
                shiftDTO.setStartTime(shift.getStartTime());
                shiftDTO.setEndTime(shift.getEndTime());
                return shiftDTO;
        }

        @Transactional
        public List<Shift> getAllShiftsByCompanyId(Long id){
                return shiftRepository.findAllByCompanyId(id);
        }

        @Transactional
        public List<Shift> getAllFutureShiftsByCompanyId(Long id){
                LocalDate date = LocalDate.now();
                return shiftRepository.findAllFutureShiftsByCompanyId(id, date);
        }

        public void deleteShiftsByShiftId(Long id){
                shiftRepository.deleteById(id);
        }

        public Shift findByID (Long id){
                return shiftRepository.findById(id).orElse(null);
        }

        @Transactional
        public void deleteOldShifts(int days){
                LocalDate cutOff = LocalDate.now().minusDays(days);
                shiftRepository.deleteOldShifts(cutOff);
        }
    
}
