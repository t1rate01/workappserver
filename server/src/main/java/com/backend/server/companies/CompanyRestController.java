package com.backend.server.companies;

import java.util.ArrayList;
import java.util.List;

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

@PostMapping("/workers/add")
public ResponseEntity<?> addWorkerEmail(@RequestHeader("Authorization")String token, @RequestBody String email, @RequestBody Role role){
    try {
        // käyttäjätarkistus ja roolitarkistus
        User user = securityService.getUserFromToken(token);
        if(!securityService.isSuperVisor(user.getRole())){
            return ResponseEntity.status(401).body("Unauthorized");
        }
        // hae käyttäjän company
        Company company = user.getCompany();
        // lisää sähköposti companyn hyväksyttyihin sähköposteihin
        approvedEmails.addEmail(company, email, role);
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
public ResponseEntity<?> deleteWorkerByID(@RequestHeader("Authorization")String token, @PathVariable int userId){
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

@PutMapping("/workers/{userId}")
public ResponseEntity<?> updateRole(@RequestHeader("Authorization")String token, @PathVariable Long userId, @RequestBody Role role){
    try {
        // käyttäjätarkistus ja roolitarkistus
        User user = securityService.getUserFromToken(token);
        if(!securityService.isMaster(user.getRole())){
            return ResponseEntity.status(401).body("Unauthorized");
        }
        // hae käyttäjän jonka id annettu email, päivitä käyttäjän ja preapprovedin rooli
        User worker = userRepository.findById(userId).orElse(null);
        if(worker == null){
            return ResponseEntity.status(404).body("User not found");
        }
        Long approvedEmailID = approvedEmails.getCompanyApprovedEmailsById(worker.getId()).getId();
        approvedEmails.updateRole(approvedEmailID, role);
        securityService.updateRole(userId, role);
        return ResponseEntity.ok("Role updated");
    }
    catch (IllegalArgumentException e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
    catch (Exception e) {
        return ResponseEntity.status(401).body(e.getMessage());
    }
}

}
