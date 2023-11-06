package com.backend.server.security.DTO;


import com.backend.server.utility.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class RegisterDTO {  // Käytetään data transfer object validointeja varten

    @NotBlank(message="Must put in email")
    @Email(message="Must be a valid email")
    private String email;

    @NotBlank(message="Must put in password")
    @Size(min=8, message="Password must be at least 8 characters long")
    private String password;

    // nämä ei ole pakollisia
    private String firstName;
    private String lastName;
    private String phoneNumber;

    // role tulee preapproved emailista, mutta sen voi muuttaa.
    private Role role;

    // company tulee toisella functiolla ennen tallennusta Usereihin
    
}
