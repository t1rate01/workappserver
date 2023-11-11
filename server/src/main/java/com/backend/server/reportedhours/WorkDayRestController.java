package com.backend.server.reportedhours;



import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.reportedhours.DTO.WorkDayDTO;
import com.backend.server.reportedhours.DTO.WorkDayResponseDTO;
import com.backend.server.security.SecurityService;
import com.backend.server.users.User;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/report")
public class WorkDayRestController {
    private final WorkDayService workDayService;
    private final SecurityService securityService;

  /*   @PostMapping("/add")
    public ResponseEntity<?> addShift(@Valid @RequestBody WorkDayDTO workDayDTO,
                                      @RequestHeader("Authorization") String token) {
        try {
            // Käyttäjätarkistus, luomalla Userin, jos luonti epäonnistuu heittää IllegalArgumentExceptionin
            // herjaa keltaista "turhaan"
            User user = securityService.getUserFromToken(token);

            // Lisää vuoro
            WorkDay workDay = workDayService.addShift(token, workDayDTO.getDate(), 
                                                      workDayDTO.getStartTime(), 
                                                      workDayDTO.getEndTime(), 
                                                      workDayDTO.getBreaksTotal(), 
                                                      workDayDTO.getDescription());

            
            return ResponseEntity.ok(workDay);  // palauttaa lisätyn vuoron tiedot
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }*/


    // päivittää vuoron päivämäärän perusteella, jos päivämäärää ei ole olemassa, luo uuden vuoron.
    // tätä voisi katsoa jos käyttäisi ensisijaisesti frontista.
    @PutMapping("/update")
    public ResponseEntity<?> updateShift(@Valid @RequestBody WorkDayDTO workDayDTO,
                                         @RequestHeader("Authorization") String token) {
        try {
            // Käyttäjäntarkistus, heittää IllegalArgumentException jos ei toimi
            User user = securityService.getUserFromToken(token);
            System.out.println("WorkdayDTO: " + workDayDTO.toString() + " token: " + token + " user: " + user.toString() + " date: " + workDayDTO.getDate() + " startTime: " + workDayDTO.getStartTime() + " endTime: " + workDayDTO.getEndTime() + " breaksTotal: " + workDayDTO.getBreaksTotal() + " description: " + workDayDTO.getDescription());

            WorkDayResponseDTO workDay = workDayService.updateShift(token, workDayDTO.getDate(), 
                                                         workDayDTO.getStartTime(), 
                                                         workDayDTO.getEndTime(), 
                                                         workDayDTO.getBreaksTotal(), 
                                                         workDayDTO.getDescription());

            return ResponseEntity.ok(workDay);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    // viimeiset 31 vuoroa haku
    @GetMapping("/personal")
    public ResponseEntity<?> getUserShifts(@RequestHeader("Authorization") String token) {
    try {
        // Tarkistus
        User user = securityService.getUserFromToken(token);

        // Kutsu overloaded funkiota joka palauttaa viimeiset 31 entryä
        List<WorkDay> userShifts = workDayService.getUserShifts(user);

        List<WorkDayDTO> userShiftsDTO = userShifts.stream()   // DTO muunnos jotta välttää lazy parsing error
                        .map(workday -> {
                        WorkDayDTO dto = new WorkDayDTO();
                        dto.setId(workday.getId());
                        dto.setDate(workday.getDate());
                        dto.setStartTime(workday.getStartTime());
                        dto.setEndTime(workday.getEndTime());
                        dto.setBreaksTotal(workday.getBreaksTotal());
                        dto.setDescription(workday.getDescription());
                        dto.setIsHoliday(workday.getIsHoliday());
                        return dto;
    })
                        .collect(Collectors.toList());

        return ResponseEntity.ok(userShiftsDTO);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

    // viimeiset custom määrä vuoroja haku, määrä parametrinä
    @GetMapping("/personal/{amount}")
    public ResponseEntity<?> getUserShifts(@RequestHeader("Authorization") String token, 
                                            @Valid @PathVariable int amount) {
        try {
            // Tarkistus
            User user = securityService.getUserFromToken(token);

            // Kutsu overloaded funktiota joka palauttaa custom määrän entryjä
            List<WorkDay> userShifts = workDayService.getUserShifts(user, amount);

            List<WorkDayDTO> userShiftsDTO = userShifts.stream()   // DTO välttääkseen lazy parsing error
                            .map(workday -> {
                            WorkDayDTO dto = new WorkDayDTO();
                            dto.setId(workday.getId());
                            dto.setDate(workday.getDate());
                            dto.setStartTime(workday.getStartTime());
                            dto.setEndTime(workday.getEndTime());
                            dto.setBreaksTotal(workday.getBreaksTotal());
                            dto.setDescription(workday.getDescription());
                            dto.setIsHoliday(workday.getIsHoliday());
                            return dto;
        })
                            .collect(Collectors.toList());


            return ResponseEntity.ok(userShiftsDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    // viimeisin vuoro, ehkä turha koska voi käyttää /personal/1
    @GetMapping("/personal/latest")
    public ResponseEntity<?> getLatestShift(@RequestHeader("Authorization") String token) {
        try {
            // Tarkistus
            User user = securityService.getUserFromToken(token);

            // Kutsu funktiota joka palauttaa viimeisimmän entryn
            List<WorkDay> userShift = workDayService.getUserShifts(user, 1);

            List<WorkDayDTO> userShiftDTO = userShift.stream()   // DTO välttääkseen lazy parsing error
                            .map(workday -> {
                            WorkDayDTO dto = new WorkDayDTO();
                            dto.setId(workday.getId());
                            dto.setDate(workday.getDate());
                            dto.setStartTime(workday.getStartTime());
                            dto.setEndTime(workday.getEndTime());
                            dto.setBreaksTotal(workday.getBreaksTotal());
                            dto.setDescription(workday.getDescription());
                            dto.setIsHoliday(workday.getIsHoliday());
                            return dto;
        })
                            .collect(Collectors.toList());
            // muunna lista yhdeksi vuoroksi
            return ResponseEntity.ok(userShiftDTO.get(0));
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @DeleteMapping("/delete/{shiftID}")
    public ResponseEntity<?> deleteShift(@RequestHeader("Authorization") String token, 
                                          @Valid @PathVariable Long shiftID) {
        try {
            // Tarkistus
            User user = securityService.getUserFromToken(token);

            // etsi vuoro
            WorkDay tobedeleted = workDayService.findByID(shiftID);
            if (tobedeleted == null) {
                return ResponseEntity.badRequest().body("Shift not found");
            }

            // poista vuoro
            workDayService.deleteShiftByID(shiftID);
            
            return ResponseEntity.ok("Shift deleted");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

