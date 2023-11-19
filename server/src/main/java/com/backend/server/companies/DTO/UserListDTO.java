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
public class UserListDTO {
    
        private Long id;
        private String email;
        private String firstName;
        private String lastName;
        private String phoneNumber;
        private Role role;
        private String companyName;
        private Long companyId;

    
}
