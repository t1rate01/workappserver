package com.backend.server.companies;



import java.util.Map;
import java.util.Set;

import com.backend.server.users.User;
import com.backend.server.utility.Auditable;
import com.backend.server.utility.JsonConverter;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;



@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "companies")
public class Company extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "company_name", nullable = false, unique = true)
    private String companyName;

    @Column(name = "settings_json", nullable = true)
    @Convert(converter = JsonConverter.class)  // see /utility/JsonConverter.java
    private Map<String, Object> settings;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true)  // db logiikkaa, jotta company ja sen työntekijät ja heidän tiedot liikkuu oikeille henkilöille
    private Set<User> users;

    @OneToMany(mappedBy = "company", cascade = CascadeType.ALL, orphanRemoval = true) // sähköpostilista, johon käyttäjä täytyy lisätä ensin, jotta annetaan rekisteröityä ja sidotaan heti oikeaan companyyn
    private Set<CompanyApprovedEmails> emailLists;
    
   



}
