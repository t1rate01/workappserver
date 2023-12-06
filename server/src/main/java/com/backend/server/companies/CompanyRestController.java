package com.backend.server.companies;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.backend.server.companies.DTO.ApprovedEmailsDTO;
import com.backend.server.companies.DTO.NewMailDTO;
import com.backend.server.companies.DTO.SettingsDTO;
import com.backend.server.companies.DTO.UserListDTO;
import com.backend.server.security.SecurityService;
import com.backend.server.users.User;
import com.backend.server.users.UserRepository;
import com.backend.server.utility.Role;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api/company")
public class CompanyRestController {
    private final CompanyService companyService;
    private final SecurityService securityService;
    private final UserRepository userRepository;
    private final CompanyAppEmailsService approvedEmails;
    private final CompAppEmailsRepository compAppEmailsRepository;
    
@GetMapping("/workers")
public ResponseEntity<?> getCompanysWorkers(@RequestHeader("Authorization") String token) {
        try {
            // käyttäjätarkistus ja roolitarkistus
            User user = securityService.getUserFromToken(token);
            if(!securityService.isSuperVisor(user.getRole())){
                return ResponseEntity.status(401).body("Unauthorized");
            }
            // hae käyttäjän company
            Company company = user.getCompany();
            // hae companyyn kuuluvat työntekijät
            List<User> workers = companyService.getAllWorkers(company);
            List<UserListDTO> userListDTO = new ArrayList<>();
            for (User worker : workers){
                UserListDTO dto = new UserListDTO();
                dto.setId(worker.getId());
                dto.setFirstName(worker.getFirstName());
                dto.setLastName(worker.getLastName());
                dto.setEmail(worker.getEmail());
                dto.setPhoneNumber(worker.getPhoneNumber());
                dto.setRole(worker.getRole());
                dto.setCompanyName(worker.getCompany().getCompanyName());
                dto.setCompanyId(worker.getCompany().getId());
                userListDTO.add(dto);
            }
            return ResponseEntity.ok(userListDTO);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }catch (Exception e) {
            return ResponseEntity.status(401).body(e.getMessage());
        }
}

// Yrityksen esihyväksytyt sähköpostit // TODO: TEST
@GetMapping("/workers/email")
public ResponseEntity<?> getApprovedEmails(@RequestHeader("Authorization") String token) {
    try {
        // käyttäjätarkistus ja roolitarkistus
        User user = securityService.getUserFromToken(token);
        if(!securityService.isSuperVisor(user.getRole())){
            return ResponseEntity.status(401).body("Unauthorized");
        }
        // hae käyttäjän company
        Company company = user.getCompany();
        // hae companyyn kuuluvat työntekijät DTOlle
        List<ApprovedEmailsDTO> emailsDTOs = approvedEmails.getApprovedEmailsDTO(company);
        
        return ResponseEntity.ok(emailsDTOs);

    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }catch (Exception e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
}

// hae yrityksen asetukset
@GetMapping("/settings")
public ResponseEntity<?> getCompanySettings(@RequestHeader("Authorization") String token) {
    try {
        // käyttäjätarkistus
        User user = securityService.getUserFromToken(token);
        
        // hae käyttäjän company
        Company company = user.getCompany();
        // hae companyyn kuuluvat työntekijät DTOlle
        Map<String, Object> settings = company.getSettings();
        if (settings == null){
            settings = new HashMap<>();
        }
        return ResponseEntity.ok(settings);

    } catch (IllegalArgumentException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }catch (Exception e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
}
    

@PostMapping("/workers/add")
public ResponseEntity<?> addWorkerEmail(@RequestHeader("Authorization")String token, @RequestBody NewMailDTO newMailDTO){
    try {
        // käyttäjätarkistus ja roolitarkistus
        User user = securityService.getUserFromToken(token);
        if(!securityService.isSuperVisor(user.getRole())){
            return ResponseEntity.status(401).body("Unauthorized");
        }
        // hae käyttäjän company
        Company company = user.getCompany();
        // lisää sähköposti companyn hyväksyttyihin sähköposteihin

        // tarkista ettei sähköpostia ole jo hyväksytty
        if (compAppEmailsRepository.findByEmail(newMailDTO.getEmail()).isPresent()){
            return ResponseEntity.status(401).body("Email already approved");
        }

        // tarkista, ettei kohteen rooli ole korkeampi kuin käyttäjän rooli
        if (newMailDTO.getRole().ordinal() > user.getRole().ordinal()){ // worker = 0, supervisor = 1, master = 2
            return ResponseEntity.status(401).body("Cannot add higher role than your own");
        }
        
        approvedEmails.addEmail(company, newMailDTO.getEmail(), newMailDTO.getRole());
        return ResponseEntity.ok("Email added");
    }
    catch (IllegalArgumentException e) {
        return ResponseEntity.status(401).body("Unauthorized");
    }
    catch (Exception e) {
        return ResponseEntity.status(401).body("Unauthorized");
    }

}

@DeleteMapping("/workers/{userId}")
public ResponseEntity<?> deleteWorkerByID(@RequestHeader("Authorization")String token, @PathVariable Long userId){
    try {
        // käyttäjätarkistus ja roolitarkistus
        User user = securityService.getUserFromToken(token);
        if(!securityService.isMaster(user.getRole())){
            return ResponseEntity.status(401).body("Unauthorized");
        }
        // hae käyttäjän jonka id annettu email, ja poista käyttäjä ja preapproved email
        User worker = userRepository.findById((long)userId).orElse(null);
        if(worker == null){
            return ResponseEntity.status(404).body("User not found");
        }

        Long approvedEmailID = approvedEmails.getCompanyApprovedEmailsById(worker.getId()).getId();
        approvedEmails.deleteCompanyApprovedEmails(approvedEmailID);
        userRepository.deleteById(worker.getId());
        return ResponseEntity.ok("User deleted");
    }
    catch (IllegalArgumentException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
    catch (Exception e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
}

@DeleteMapping("/workers/email/{email}")  // Poistaa työntekijän sähköpostin hyväksytyistä sähköposteista JA KATSOO ONKO SÄHKÖPOSTILLA KÄYTTÄJÄÄ, JOKA MYÖS POISTETAAN
public ResponseEntity<?> deleteApprovedEmail(@RequestHeader("Authorization")String token, @PathVariable String email){
    try {
        // käyttäjätarkistus ja roolitarkistus, sallitaan vain masterille
        User user = securityService.getUserFromToken(token);
        if(!securityService.isMaster(user.getRole())){
            return ResponseEntity.status(401).body("Unauthorized");
        }
        // katso onko sähköpostilla käyttäjää
        User worker = userRepository.findByEmail(email).orElse(null);
        if(worker != null){
            // jos on, poista käyttäjä
            userRepository.deleteById(worker.getId());
        }
        // poista sähköposti hyväksytyistä sähköposteista
        approvedEmails.deleteCompanyApprovedEmailsByEmail(email);
        return ResponseEntity.ok("Email deleted");
    }
    catch (IllegalArgumentException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
    catch (Exception e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
}

// Päivitä työntekijän rooli, supervisor ja master
@PutMapping("/workers/{userId}")
public ResponseEntity<?> updateRole(@RequestHeader("Authorization")String token, @PathVariable Long userId, @RequestBody NewMailDTO DTO){
    try {
          // katso että DTO:ssa on rooli
        if (DTO.getRole() == null){
            return ResponseEntity.status(401).body("Role not found, send role in JSON format");
        }
        // käyttäjätarkistus ja roolitarkistus
        User user = securityService.getUserFromToken(token);
        if(!securityService.isSuperVisor(user.getRole())){
            return ResponseEntity.status(401).body("Unauthorized");
        }
        // jos roolia päivitetään, katso että uusi rooli ei ole korkeampi kuin lisääjän rooli
        if (DTO.getRole().ordinal() > user.getRole().ordinal()){ // worker = 0, supervisor = 1, master = 2
            return ResponseEntity.status(401).body("Cannot add higher role than your own");
        }
        // hae käyttäjän jonka id annettu email, päivitä käyttäjän ja preapprovedin rooli
        User worker = userRepository.findById(userId).orElse(null);
        if(worker == null){
            return ResponseEntity.status(404).body("User not found");
        }
        Long approvedEmailID = approvedEmails.getCompanyApprovedEmailsById(worker.getId()).getId();
        approvedEmails.updateRole(approvedEmailID, DTO.getRole());
        securityService.updateRole(userId, DTO.getRole());
        return ResponseEntity.ok("Role updated");
    }
    catch (IllegalArgumentException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
    catch (Exception e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
}

// Muokkaa esihyväksyttyä sähköpostia, supervisor ja master. Vain jos sähköpostilla ei ole vielä rekisteröity
@PutMapping("/workers/email/{oldEmail}")
public ResponseEntity<?> updateApprovedEmail(@RequestHeader("Authorization") String token, @PathVariable String oldEmail, @RequestBody NewMailDTO DTO){
    try {
        // katso että DTO:ssa on email
        if (DTO.getEmail() == null){
            return ResponseEntity.status(401).body("Email not found, send email in JSON format");
        }
        // käyttäjätarkistus ja roolitarkistus
        User user = securityService.getUserFromToken(token);
        if(!securityService.isSuperVisor(user.getRole())){
            return ResponseEntity.status(401).body("Unauthorized");
        }
        // jos roolia päivitetään, katso että uusi rooli ei ole korkeampi kuin lisääjän rooli
        if (DTO.getRole() != null && DTO.getRole().ordinal() > user.getRole().ordinal()){ // worker = 0, supervisor = 1, master = 2
            return ResponseEntity.status(401).body("Cannot add higher role than your own");
        }
        // katso että oldEmail on olemassa
        Optional<CompanyApprovedEmails> oldEmailOptional = compAppEmailsRepository.findByEmail(oldEmail);
        if (!oldEmailOptional.isPresent()){
            return ResponseEntity.status(404).body("Email not found");
        }
        // katso että uusi email ei ole jo rekisteröity
        Optional<CompanyApprovedEmails> newEmailOptional = compAppEmailsRepository.findByEmail(DTO.getEmail());
        if (newEmailOptional.isPresent()){
            return ResponseEntity.status(401).body("User has already registered with email, edit through user settings.");
        }
        // katso että uusi email on vapaa
        Optional<CompanyApprovedEmails> newEmailOptional2 = compAppEmailsRepository.findByEmail(DTO.getEmail());
        if (newEmailOptional2.isPresent()){
            return ResponseEntity.status(401).body("Email already in approved list");
        }
        // päivitä email
        CompanyApprovedEmails oldEmailObject = oldEmailOptional.get();
        oldEmailObject.setEmail(DTO.getEmail());
        // päivitä rooli, jos DTO:ssa on rooli
        if (DTO.getRole() != null){
            oldEmailObject.setRole(DTO.getRole());
        }
        // tallenna
        compAppEmailsRepository.save(oldEmailObject);
        return ResponseEntity.ok("Email updated");
        
    }
    catch (IllegalArgumentException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
    catch (Exception e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
}

// Muokkaa companyn settingsejä, vain master
@PutMapping("/settings")
public ResponseEntity<?> updateCompanySettings(@RequestHeader("Authorization")String token, @RequestBody SettingsDTO settingsDTO){
    try {
        // käyttäjätarkistus ja roolitarkistus
        User user = securityService.getUserFromToken(token);
        if(!securityService.isMaster(user.getRole())){
            return ResponseEntity.status(401).body("Unauthorized");
        }
        // hae käyttäjän company
        Company company = user.getCompany();
        // katso nykyiset asetukset
        Map<String, Object> currentSettings = company.getSettings();
        if (currentSettings == null){
            currentSettings = new HashMap<>();
        }
        // päivitä asetukset
        currentSettings.putAll(settingsDTO.getSettings());
        company.setSettings(currentSettings);
        // katso onko DTOssa companyName, jos on niin, tarkista onko se erilainen kuin nykyinen
        if (settingsDTO.getCompanyName() != null && !settingsDTO.getCompanyName().equals(company.getCompanyName())){
            // jos on, päivitä companylle uusi nimi
            company.setCompanyName(settingsDTO.getCompanyName());
        }
        // tallenna company
        companyService.updateCompany(company);

        return ResponseEntity.ok("Settings updated");
    }
    catch (IllegalArgumentException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
    catch (Exception e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
}

}
