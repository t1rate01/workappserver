package com.backend.server.reportedhours;



import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.reportedhours.DTO.WorkDayDTO;
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

    @PostMapping("/add")
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
    }

    @PutMapping("/update")
    public ResponseEntity<?> updateShift(@Valid @RequestBody WorkDayDTO workDayDTO,
                                         @RequestHeader("Authorization") String token) {
        try {
            // Käyttäjäntarkistus, heittää IllegalArgumentException jos ei toimi
            User user = securityService.getUserFromToken(token);

            WorkDay workDay = workDayService.updateShift(token, workDayDTO.getDate(), 
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

    @GetMapping("/personal")
    public ResponseEntity<?> getUserShifts(@RequestHeader("Authorization") String token) {
    try {
        // Tarkistus
        User user = securityService.getUserFromToken(token);

        // Kutsu overloaded funkiota joka palauttaa viimeiset 31 entryä
        List<WorkDay> userShifts = workDayService.getUserShifts(user);

        return ResponseEntity.ok(userShifts);
    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
    } catch (Exception e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}

}

