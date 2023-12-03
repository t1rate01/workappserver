package com.backend.server.shifts;


import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.server.companies.Company;
import com.backend.server.reportedhours.DTO.WorkDayDTO;
import com.backend.server.security.SecurityService;
import com.backend.server.shifts.DTO.ShiftDTO;
import com.backend.server.shifts.DTO.ShiftListDTO;
import com.backend.server.users.User;
import com.backend.server.users.UserRepository;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/shifts")
public class ShiftRestController {
    private final ShiftService shiftService;
    private final SecurityService securityService;
    private final UserRepository userRepository;

    @PutMapping("/update")  // saa määränneen tiedot tokenista, requestissa oltava työntekijän tiedot, ketä koskee, katso WorkDayDTO
    public ResponseEntity<?> assignShift(@Valid @RequestBody WorkDayDTO workDayDTO, @RequestHeader("Authorization")String token){
        try {
            // tarkista käyttäjä ja rooli jotta oikeus käyttää, heittää illegalargumentexceptionin jos ei onnistu
            User supervisor = securityService.getUserFromToken(token);
            if(supervisor.getRole() == null || !securityService.isSuperVisor(supervisor.getRole())){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
            // tarkista että työntekijä on olemassa
            User worker = userRepository.findById(workDayDTO.getId()).orElse(null);
            if(worker == null){
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Worker not found");
            }

            // tarkista että työntekijä kuuluu samaan yritykseen kuin esimies
            if(worker.getCompany().getId() != supervisor.getCompany().getId()){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }

            Company confirmedCompany = supervisor.getCompany();

            // lisää vuoro
            ShiftDTO shiftDTO = shiftService.addShift(worker, workDayDTO.getDate(), 
                                                        workDayDTO.getStartTime(), 
                                                        workDayDTO.getEndTime(),
                                                        workDayDTO.getBreaksTotal(),
                                                        workDayDTO.getDescription(),
                                                        confirmedCompany
                                                        );
            

            return ResponseEntity.ok(shiftDTO);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @DeleteMapping("/delete/{shiftID}")
    public ResponseEntity<?> deleteAssignedShift(@RequestHeader("Authorization") String token, @PathVariable int shiftId){
        try {
            // varmenna token ja rooli
            User supervisor = securityService.getUserFromToken(token);
            if(!securityService.isSuperVisor(supervisor.getRole())){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
            // poista vuoro
            String response = shiftService.deleteShift((long)shiftId);
            return ResponseEntity.ok(response);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @GetMapping("/personal")
    public ResponseEntity<?> getPersonalAssignedShifts(@RequestHeader("Authorization")String token){
        try {
            // käyttäjä tokenista
            User user = securityService.getUserFromToken(token);
            // hae käyttäjälle määrätyt vuorot
            List<Shift> assignedshifts = shiftService.getFutureShiftsById(user.getId());
            List<ShiftListDTO> shiftListDTOs = new ArrayList<>();
            for (Shift assignedshift : assignedshifts){
                ShiftListDTO shiftListDTO = new ShiftListDTO();
                shiftListDTO.setId(assignedshift.getId());
                shiftListDTO.setUserId(assignedshift.getUser().getId());
                shiftListDTO.setFirstName(assignedshift.getUser().getFirstName());
                shiftListDTO.setLastName(assignedshift.getUser().getLastName());
                shiftListDTO.setStartTime(assignedshift.getStartTime());
                shiftListDTO.setEndTime(assignedshift.getEndTime());
                shiftListDTO.setCompanyId(assignedshift.getCompany().getId());
                shiftListDTO.setCompanyName(assignedshift.getCompany().getCompanyName());
                // null check breaks
                if(assignedshift.getBreaksTotal() == null){
                    shiftListDTO.setBreaksTotal(0);
                }
                else{
                    shiftListDTO.setBreaksTotal(assignedshift.getBreaksTotal());
                }
                // null check description 
                if(assignedshift.getDescription() == null){
                    shiftListDTO.setDescription("");
                }
                else{
                    shiftListDTO.setDescription(assignedshift.getDescription());
                }
                shiftListDTO.setDate(assignedshift.getDate());
                shiftListDTOs.add(shiftListDTO);
            }

            return ResponseEntity.ok(shiftListDTOs);
            
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/personal/all")
    public ResponseEntity<?> getAllPersonalAssignedShifts(@RequestHeader("Authorization")String token){
        try {
            // käyttäjä tokenista
            User user = securityService.getUserFromToken(token);
            // hae käyttäjälle määrätyt vuorot
            List<Shift> assignedshifts = shiftService.getShiftsById(user.getId());
            List<ShiftListDTO> shiftListDTOs = new ArrayList<>();
            for (Shift assignedshift : assignedshifts){
                ShiftListDTO shiftListDTO = new ShiftListDTO();
                shiftListDTO.setId(assignedshift.getId());
                shiftListDTO.setUserId(assignedshift.getUser().getId());
                shiftListDTO.setFirstName(assignedshift.getUser().getFirstName());
                shiftListDTO.setLastName(assignedshift.getUser().getLastName());
                shiftListDTO.setStartTime(assignedshift.getStartTime());
                shiftListDTO.setEndTime(assignedshift.getEndTime());
                shiftListDTO.setBreaksTotal(assignedshift.getBreaksTotal());
                shiftListDTO.setDescription(assignedshift.getDescription());
                shiftListDTO.setDate(assignedshift.getDate());
                shiftListDTOs.add(shiftListDTO);
            }

            return ResponseEntity.ok(shiftListDTOs);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/everyone") // companyn kaikkien työntekijöiden kaikki TULEVAT määrätyt vuorot
    public ResponseEntity<?> getAllFutureAssignedShifts(@RequestHeader("Authorization") String token){
        try {
            // varmenna token, ei rajata roolilla
            User user= securityService.getUserFromToken(token);
           
            // hae käyttäjän company
            Company company = user.getCompany();
            if (company == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
            // hae kaikki companyn määrätyt vuorot
            List<Shift> assignedshifts = shiftService.getAllFutureShiftsByCompanyId(company.getId());
            List<ShiftListDTO> shiftListDTOs = new ArrayList<>();
            for (Shift assignedshift : assignedshifts){
                ShiftListDTO shiftListDTO = new ShiftListDTO();
                shiftListDTO.setId(assignedshift.getId());
                shiftListDTO.setUserId(assignedshift.getUser().getId());
                shiftListDTO.setFirstName(assignedshift.getUser().getFirstName());
                shiftListDTO.setLastName(assignedshift.getUser().getLastName());
                shiftListDTO.setStartTime(assignedshift.getStartTime());
                shiftListDTO.setEndTime(assignedshift.getEndTime());
                shiftListDTO.setBreaksTotal(assignedshift.getBreaksTotal());
                shiftListDTO.setDescription(assignedshift.getDescription());
                shiftListDTO.setDate(assignedshift.getDate());
                shiftListDTO.setCompanyId(company.getId());
                shiftListDTO.setCompanyName(company.getCompanyName());
                shiftListDTOs.add(shiftListDTO);
            }

            return ResponseEntity.ok(shiftListDTOs);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @GetMapping("/everyone/all") // companyn kaikkien työntekijöiden kaikki  määrätyt vuorot
    public ResponseEntity<?> getAllAssignedShifts(@RequestHeader("Authorization") String token){
        try {
            // varmenna token, ei rajata roolilla
            User user= securityService.getUserFromToken(token);
           
            // hae käyttäjän company
            Company company = user.getCompany();
            if (company == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
            // hae kaikki companyn määrätyt vuorot
            List<Shift> assignedshifts = shiftService.getAllShiftsByCompanyId(company.getId());
            List<ShiftListDTO> shiftListDTOs = new ArrayList<>();
            for (Shift assignedshift : assignedshifts){
                ShiftListDTO shiftListDTO = new ShiftListDTO();
                shiftListDTO.setId(assignedshift.getId());
                shiftListDTO.setUserId(assignedshift.getUser().getId());
                shiftListDTO.setFirstName(assignedshift.getUser().getFirstName());
                shiftListDTO.setLastName(assignedshift.getUser().getLastName());
                shiftListDTO.setStartTime(assignedshift.getStartTime());
                shiftListDTO.setEndTime(assignedshift.getEndTime());
                shiftListDTO.setBreaksTotal(assignedshift.getBreaksTotal());
                shiftListDTO.setDescription(assignedshift.getDescription());
                shiftListDTO.setDate(assignedshift.getDate());
                shiftListDTO.setCompanyId(company.getId());
                shiftListDTO.setCompanyName(company.getCompanyName());
                shiftListDTOs.add(shiftListDTO);
            }

            return ResponseEntity.ok(shiftListDTOs);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @GetMapping("/others") // companyn kaikkien työntekijöiden kaikki TULEVAT määrätyt vuorot, paitsi käyttäjän omat
    public ResponseEntity<?> getOthersFutureAssignedShifts(@RequestHeader("Authorization") String token){
        try {
            // varmenna token, ei rajata roolilla
            User user= securityService.getUserFromToken(token);
           
            // hae käyttäjän company
            Company company = user.getCompany();
            if (company == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
            // hae kaikki companyn määrätyt vuorot
            List<Shift> assignedshifts = shiftService.getCompanyFutureShiftsExcludingUser(user);
            List<ShiftListDTO> shiftListDTOs = new ArrayList<>();
            for (Shift assignedshift : assignedshifts){
                ShiftListDTO shiftListDTO = new ShiftListDTO();
                shiftListDTO.setId(assignedshift.getId());
                shiftListDTO.setUserId(assignedshift.getUser().getId());
                shiftListDTO.setFirstName(assignedshift.getUser().getFirstName());
                shiftListDTO.setLastName(assignedshift.getUser().getLastName());
                shiftListDTO.setStartTime(assignedshift.getStartTime());
                shiftListDTO.setEndTime(assignedshift.getEndTime());
                shiftListDTO.setBreaksTotal(assignedshift.getBreaksTotal());
                shiftListDTO.setDescription(assignedshift.getDescription());
                shiftListDTO.setDate(assignedshift.getDate());
                shiftListDTO.setCompanyId(company.getId());
                shiftListDTO.setCompanyName(company.getCompanyName());
                shiftListDTOs.add(shiftListDTO);
            }

            return ResponseEntity.ok(shiftListDTOs);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }

    @GetMapping("/others/all") // companyn kaikkien työntekijöiden kaikki määrätyt vuorot, paitsi käyttäjän omat
    public ResponseEntity<?> getOthersAssignedShifts(@RequestHeader("Authorization") String token){
        try {
            // varmenna token, ei rajata roolilla
            User user= securityService.getUserFromToken(token);
           
            // hae käyttäjän company
            Company company = user.getCompany();
            if (company == null){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
            }
            // hae kaikki companyn määrätyt vuorot
            List<Shift> assignedshifts = shiftService.getAllCompanyShiftsExcludingUserNoDate(user);
            List<ShiftListDTO> shiftListDTOs = new ArrayList<>();
            for (Shift assignedshift : assignedshifts){
                ShiftListDTO shiftListDTO = new ShiftListDTO();
                shiftListDTO.setId(assignedshift.getId());
                shiftListDTO.setUserId(assignedshift.getUser().getId());
                shiftListDTO.setFirstName(assignedshift.getUser().getFirstName());
                shiftListDTO.setLastName(assignedshift.getUser().getLastName());
                shiftListDTO.setStartTime(assignedshift.getStartTime());
                shiftListDTO.setEndTime(assignedshift.getEndTime());
                shiftListDTO.setBreaksTotal(assignedshift.getBreaksTotal());
                shiftListDTO.setDescription(assignedshift.getDescription());
                shiftListDTO.setDate(assignedshift.getDate());
                shiftListDTO.setCompanyId(company.getId());
                shiftListDTO.setCompanyName(company.getCompanyName());
                shiftListDTOs.add(shiftListDTO);
            }

            return ResponseEntity.ok(shiftListDTOs);
        }
        catch(IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized");
        }
        catch(Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error");
        }
    }
}
