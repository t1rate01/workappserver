package com.backend.server.security.DTO;

import com.backend.server.utility.Role;

import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDTO {

    @Email(message="Must be a valid email")
    private String email;


    // nämä ei ole pakollisia
    private String firstName;
    private String lastName;
    private String phoneNumber;
    private Role role;
    
}
