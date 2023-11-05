package com.backend.server.utility;

import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String refreshToken;
    private Role role;
    private String companyname;
    private Map<String, Object> companySettings;

    // TODO: LOGINRESPONSEN TÄYTYY KERTOA SETTINGSIT
}
