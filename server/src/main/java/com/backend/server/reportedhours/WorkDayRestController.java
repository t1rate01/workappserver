package com.backend.server.reportedhours;



import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;
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

import com.backend.server.reportedhours.DTO.EveryOnesWorkDayDTO;
import com.backend.server.reportedhours.DTO.PunchClockResponseDTO;
import com.backend.server.reportedhours.DTO.PunchPostDTO;
import com.backend.server.reportedhours.DTO.WorkDayDTO;
import com.backend.server.reportedhours.DTO.WorkDayResponseDTO;
import com.backend.server.security.SecurityService;
import com.backend.server.users.User;
import com.backend.server.users.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/report")
public class WorkDayRestController {
    private final WorkDayService workDayService;
    private final SecurityService securityService;
    private final UserRepository userRepository;

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

            WorkDayResponseDTO workDay = workDayService.updateShift(user, workDayDTO.getDate(), 
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
                        // null check
                        if(workday.getBreaksTotal() != null){
                            dto.setBreaksTotal(workday.getBreaksTotal());
                        }
                        else {
                            dto.setBreaksTotal(0);
                        }
                        // null check
                        if(workday.getDescription() != null){
                            dto.setDescription(workday.getDescription());
                        }
                        else {
                            dto.setDescription("");
                        }
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
                            // null check breaks
                            if(workday.getBreaksTotal() != null){
                                dto.setBreaksTotal(workday.getBreaksTotal());
                            }
                            else {
                                dto.setBreaksTotal(0);
                            }
                            // null check description
                            if(workday.getDescription() != null){
                                dto.setDescription(workday.getDescription());
                            }
                            else {
                                dto.setDescription("");
                            }
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
                            // null check breaks and description
                            if(workday.getBreaksTotal() != null){
                                dto.setBreaksTotal(workday.getBreaksTotal());
                            }
                            else {
                                dto.setBreaksTotal(0);
                            }
                            if(workday.getDescription() != null){
                                dto.setDescription(workday.getDescription());
                            }
                            else {
                                dto.setDescription("");
                            }
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

    @GetMapping("/punchclock/{email}")
    public ResponseEntity<?> getAtWork(@PathVariable String email){
        try {
            // etsi käyttäjä sähköpostilla
            Optional<User> user = userRepository.findByEmail(email);
            if (user.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found");
            }

              // TODO: etsi käyttäjän SHIFT, joka on tälle päivälle

            // katso serverin päivämäärä, ja katso onko vuoroa tällä päivämäärällä
            LocalDate today = LocalDate.now();
            // hae viimeisin työvuoro
            List<WorkDay> userShifts = workDayService.getUserShifts(user.get(), 1);

            LocalDate shiftDate;
            LocalTime shiftStartTime;
            LocalTime shiftEndTime;
            

            if (userShifts.isEmpty()) {
                shiftDate = LocalDate.of(2000, 1, 1);  // jos vuoroa ei löydy, palauta joku vanha päivä
            } else {
                shiftDate = userShifts.get(0).getDate();
            }

            // jos päivämäärä on sama kuin tänään, palauta true

            // valmistele DTO
            PunchClockResponseDTO DTO = new PunchClockResponseDTO();
            DTO.setFirstName(user.get().getFirstName());
            DTO.setLastName(user.get().getLastName());
            DTO.setDate(today);
            // TODO: lisää startTime ja endTime kun shift valmis
            // TODO: lisää description päivän shiftistä
            

            if(shiftDate.equals(today) && userShifts.get(0).getEndTime() == null) {
                DTO.setIsAtWork(true);
            } else {
                DTO.setIsAtWork(false);
            }   
            return ResponseEntity.ok(DTO); 
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/punchin")  // muokattu käyttämään email myös requestbodyssä
    public ResponseEntity<?> punchIn(@RequestBody PunchPostDTO punchPostDTO){
        try {
            // etsi käyttäjä
            Optional<User> user = userRepository.findByEmail(punchPostDTO.getEmail());
            if (user.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found");
            }

          

            User confirmedUser = user.get();
            // lisää tälle päivälle vuoro ja sille aloitusajaksi annettu aika
            WorkDay workDay = workDayService.punchIn(confirmedUser, punchPostDTO.getTime());
            PunchClockResponseDTO DTO = new PunchClockResponseDTO();
            DTO.setStartTime(punchPostDTO.getTime());
            return ResponseEntity.ok(DTO.getStartTime());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/punchout") // muokattu käyttämään email myös requestbodyssä
    public ResponseEntity<?> punchOut(@RequestBody PunchPostDTO punchPostDTO){
        try {
            // etsi käyttäjä
            Optional<User> user = userRepository.findByEmail(punchPostDTO.getEmail());
            if (user.isEmpty()) {
                return ResponseEntity.badRequest().body("User not found");
            }

            User confirmedUser = user.get();
            // päätä päivän vuoro ja päivitä siihen lopetusajaksi annettu aika
            WorkDay workDay = workDayService.punchOut(confirmedUser, punchPostDTO.getTime());

            PunchClockResponseDTO DTO = new PunchClockResponseDTO();
            DTO.setEndTime(punchPostDTO.getTime());
            return ResponseEntity.ok(DTO.getEndTime());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/company") // palauttaa kaikki yrityksen työntekijöiden vuorot, jos rooli riittää
    public ResponseEntity<?> everyOnesHours(@RequestHeader ("Authorization") String token){
        try {
            // tarkista käyttäjä, palauttaa illegalargumentexceptionin jos ei toimi
            User user = securityService.getUserFromToken(token);
            //tarkista rooli (vähintään supervisor)
            if(!securityService.isSuperVisor(user.getRole())){
                return ResponseEntity.status(401).body("Unauthorized");
            }
            // hae käyttäjän yritys
            Long companyID = user.getCompany().getId();
            // hae kaikki yrityksen vuorot 
            List<WorkDay> companyShifts = workDayService.getCompanyWorkDays(companyID);

            List<EveryOnesWorkDayDTO> companyShiftsDTO = companyShifts.stream()   // DTO välttääkseen lazy parsing error
                            .map(workday -> {
                            EveryOnesWorkDayDTO dto = new EveryOnesWorkDayDTO();
                            dto.setId(workday.getId());
                            dto.setFirstName(workday.getUser().getFirstName());
                            dto.setLastName(workday.getUser().getLastName());
                            dto.setDate(workday.getDate());
                            dto.setStartTime(workday.getStartTime());
                            dto.setEndTime(workday.getEndTime());
                            // null check breaks 
                            if(workday.getBreaksTotal() != null){
                                dto.setBreaksTotal(workday.getBreaksTotal());
                            }
                            else {
                                dto.setBreaksTotal(0);
                            }
                            // null check description
                            if(workday.getDescription() != null){
                                dto.setDescription(workday.getDescription());
                            }
                            else {
                                dto.setDescription("");
                            }
                            dto.setIsHoliday(workday.getIsHoliday());
                            return dto;
        })
                            .collect(Collectors.toList());

            return ResponseEntity.ok(companyShiftsDTO);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }

    }

    @GetMapping("/others") // palauttaa kaikki companyn vuorot, paitsi omat
    public ResponseEntity<?> othersHours(@RequestHeader ("Authorization") String token){
        try {
            // käyttäjä ja rooli
            User user = securityService.getUserFromToken(token);
            if(!securityService.isSuperVisor(user.getRole())){
                return ResponseEntity.status(401).body("Unauthorized");
            }
         
            List<WorkDay> companyShifts = workDayService.getCompanyWorkDaysExcludingUser(user);
        
            

            List<EveryOnesWorkDayDTO> othersShiftsDTO = companyShifts.stream()   // DTO välttääkseen lazy parsing error
                            .map(workday -> {
                            EveryOnesWorkDayDTO dto = new EveryOnesWorkDayDTO();
                            dto.setId(workday.getId());
                            dto.setFirstName(workday.getUser().getFirstName());
                            dto.setLastName(workday.getUser().getLastName());
                            dto.setDate(workday.getDate());
                            dto.setStartTime(workday.getStartTime());
                            dto.setEndTime(workday.getEndTime());
                            // null check breaks
                            if(workday.getBreaksTotal() != null){
                                dto.setBreaksTotal(workday.getBreaksTotal());
                            }
                            else {
                                dto.setBreaksTotal(0);
                            }
                            // null check description
                            if(workday.getDescription() != null){
                                dto.setDescription(workday.getDescription());
                            }
                            else {
                                dto.setDescription("");
                            }
                            dto.setIsHoliday(workday.getIsHoliday());
                            return dto;
        }
                            )
                            .collect(Collectors.toList());

            return ResponseEntity.ok(othersShiftsDTO);
        }
        catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}

