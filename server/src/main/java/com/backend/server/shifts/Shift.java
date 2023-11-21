package com.backend.server.shifts;

import java.time.LocalDate;
import java.time.LocalTime;

import com.backend.server.companies.Company;
import com.backend.server.users.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "shifts")
public class Shift {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable=false, referencedColumnName = "id")  // KÄYTTÄJÄ JOLLE VUORO ON MÄÄRÄTTY
    private User user;

    @Column(nullable=false, unique = true)
    private LocalDate date;

    @Column(nullable=false)
    private LocalTime startTime;

    private LocalTime endTime;

    @Column(nullable=true, name = "breaks_total")  // määrätyt tauot vuoron aikana yhteensä, jos on
    private Integer breaksTotal;

    private String description;

    @ManyToOne
    @JoinColumn(name = "company_id", nullable=false, referencedColumnName = "id")  // KÄYTTÄJÄN TYÖNANTAJA
    private Company company;

    
    
}
