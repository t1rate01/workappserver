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
public class NewMailDTO {
    private String email;
    private Role role;
}
