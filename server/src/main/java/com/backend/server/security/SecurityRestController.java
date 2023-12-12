package com.backend.server.security;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Optional;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import com.backend.server.security.DTO.RegisterDTO;
import com.backend.server.security.DTO.UpdateDTO;
import com.backend.server.security.DTO.UpdatePassWordDTO;
import com.backend.server.users.User;
import com.backend.server.users.UserRepository;
import com.backend.server.utility.LoginResponse;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@CrossOrigin
@RestController
@RequestMapping("/api")  // kaikki alkaa /api + endpointin /osote
public class SecurityRestController {
    private final SecurityService securityService;
    private final UserRepository userRepository;
 
  



    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDTO registerDTO){
    try {
        User registeredUser = securityService.register(registerDTO.getEmail(), registerDTO.getPassword(), registerDTO.getFirstName(), registerDTO.getLastName(), registerDTO.getPhoneNumber());
        if (registeredUser != null) {
            return ResponseEntity.ok("User created successfully");
        } else {
            return ResponseEntity.badRequest().body("User creation failed");
        }
    } catch (IllegalArgumentException e) {
        // Catch argumentexceptionille
        return ResponseEntity.badRequest().body(e.getMessage());
    } catch (Exception e) {
        // Jos tarvitsee debugata jotain muuta niin 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
}

    @PutMapping("/user/update") // käyttäjän omien tietojen päivitys, ei salasanan
    public ResponseEntity<?> updateUser(@Valid @RequestBody UpdateDTO DTO, @RequestHeader("Authorization") String token) {
        try{
            // käyttäjä tokenista
            User user = securityService.getUserFromToken(token);
            Boolean isMaster = securityService.isMaster(user.getRole());
            

            securityService.updateUserDetails(user, DTO, isMaster, true);
            return ResponseEntity.ok("User updated successfully");
        }
        catch (IllegalArgumentException e) {
            // Catch argumentexceptionille
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Jos tarvitsee debugata jotain muuta niin 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    }

    @PutMapping("/user/update/{userId}") // käyttäjän tietojen päivitys, ei salasanan, vain master ja vain muille
    public ResponseEntity<?> updateOtherUser (@Valid @RequestBody UpdateDTO DTO, @RequestHeader("Authorization") String token, @PathVariable Long userId) {
        try {
            User master = securityService.getUserFromToken(token);
            Boolean personalUpdate = master.getId() == userId;
            if (!securityService.isMaster(master.getRole())) {
                return ResponseEntity.status(401).body("Unauthorized");
            }
            User targetUser = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
            // katso että companyt samat
            if (targetUser.getCompany().getId() != master.getCompany().getId()) {
                return ResponseEntity.status(401).body("Unauthorized");
            }
            
            securityService.updateUserDetails(targetUser, DTO, true, personalUpdate);
            return ResponseEntity.ok("User updated successfully");
        }
        catch (IllegalArgumentException e) {
            // Catch argumentexceptionille
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Jos tarvitsee debugata jotain muuta niin 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    }

    @PutMapping("/user/update/password") // käyttäjän oman salasanan päivitys. 
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePassWordDTO DTO, @RequestHeader("Authorization") String token) {
        try {
            User user = securityService.getUserFromToken(token);
            securityService.updatePassword(user, DTO.getNewPassword());
             return ResponseEntity.ok("Password updated successfully");
        }
        catch (IllegalArgumentException e) {
            // Catch argumentexceptionille
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Jos tarvitsee debugata jotain muuta niin 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    }

    @PutMapping("/user/update/password/{userId}") // käyttäjän salasanan päivitys, vain master
    public ResponseEntity<?> resetPassword(@Valid @RequestBody UpdatePassWordDTO DTO, @RequestHeader("Authorization") String token, @PathVariable Long userId) {
        try {
            User master = securityService.getUserFromToken(token);
            if (!securityService.isMaster(master.getRole())) {
                return ResponseEntity.status(401).body("Unauthorized");
            }
            Optional<User> targetUser = userRepository.findById(userId);
            if (targetUser.isEmpty()) {
                return ResponseEntity.status(401).body("Target user not found");
            }
            // katso että companyt samat
            if (targetUser.get().getCompany().getId() != master.getCompany().getId()) {
                return ResponseEntity.status(401).body("Unauthorized");
            }
            securityService.updatePassword(targetUser.get(), DTO.getNewPassword());
            return ResponseEntity.ok("Targets password updated successfully");
        }
        catch (IllegalArgumentException e) {
            // Catch argumentexceptionille
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            // Jos tarvitsee debugata jotain muuta niin 500
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
    }
    }


    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestHeader("Authorization") String basicAuth) {
        try {
            String basicStart = "Basic ";
            
            if (!basicAuth.startsWith(basicStart)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing Basic Auth format");
            }

            String base64Credentials = basicAuth.substring(basicStart.length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            String[] values = credentials.split(":", 2);

            if (values.length != 2) {
                return ResponseEntity.badRequest().body("Error in Basic Auth format");
            }

            String email = values[0];
            String password = values[1];

            LoginResponse response = securityService.login(email, password);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Kutsuttu functio käyttää throw new IllegalArgumentExceptionia, josta viesti
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/login/short")
    public ResponseEntity<?> loginShort(@Valid @RequestHeader("Authorization") String basicAuth) {
        try {
            String basicStart = "Basic ";

            if (!basicAuth.startsWith(basicStart)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Missing Basic Auth format");
            }

            String base64Credentials = basicAuth.substring(basicStart.length()).trim();
            byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
            String credentials = new String(credDecoded, StandardCharsets.UTF_8);
            String[] values = credentials.split(":", 2);

            if (values.length != 2) {
                return ResponseEntity.badRequest().body("Error in Basic Auth format");
            }

            String email = values[0];
            String password = values[1];

            LoginResponse response = securityService.login(email, password);
            response.setRefreshToken("");
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Kutsuttu functio käyttää throw new IllegalArgumentExceptionia, josta viesti
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }


    @PostMapping("/refresh")  // frontti koodattava reagoimaan accesstokenin vanhenemiseen niin että yrittää tähän endpointiin refreshtokenilla ja saada uuden tokenin
    public ResponseEntity<?> refresh(@Valid @RequestHeader("Authorization") String refreshToken) {
        if (refreshToken.startsWith("Bearer ")) {  // poistetaan Bearer alku tokenhakuja varten
            refreshToken = refreshToken.substring(7);
        }
        try {
            String newAccessToken = securityService.refreshAccessToken(refreshToken);
            User user = securityService.getUserFromToken(newAccessToken);
            // muodosta vastaus JSON
            LoginResponse response = new LoginResponse();
            response.setToken(newAccessToken);
            response.setRole(user.getRole());
            response.setCompanyname(user.getCompany().getCompanyName());
            response.setCompanySettings(user.getCompany().getSettings());
            response.setFirstName(user.getFirstName());
            response.setLastName(user.getLastName());

            // lähetä json
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            // Kutsuttu functio käyttää throw new IllegalArgumentExceptionia
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout(@Valid @RequestHeader("Authorization") String accessToken) {
        try {
            User user = securityService.getUserFromToken(accessToken);
            securityService.logout(user);
            return ResponseEntity.ok("Logged out successfully");
        } catch (IllegalArgumentException e) {
            // Kutsuttu functio käyttää throw new IllegalArgumentExceptionia
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        }
    }
}
