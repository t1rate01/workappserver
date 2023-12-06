package com.backend.server.companies.DTO;

import com.backend.server.utility.Role;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ApprovedEmailsDTO {

    // DTO companyn approved emails vastaukseen
    private Long id;
    private String email;
    private Role role;

    // onko esihyväksytyllä sähköpostilla rekisteröity
    private Boolean registered;



    
}
