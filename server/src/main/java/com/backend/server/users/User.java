package com.backend.server.users;

import java.util.Set;

import com.backend.server.companies.Company;
import com.backend.server.security.RefreshToken;
import com.backend.server.utility.Auditable;
import com.backend.server.utility.Role;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    private String firstName;

    private String lastName;

    @Column(nullable = true)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)  // see utility/Role.java
    @Column(nullable = false)      // roolit on worker, supervisor ja master, eri accesstasoja varten
    private Role role;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable = false)
    private Company company;

    @OneToMany(mappedBy = "user")
    private Set<RefreshToken> refreshTokens;

 // TODO: SUHTEET SHIFTS JA TYÖAIKARAPORTIT
    
}
