package com.backend.server.reportedhours;

import java.time.LocalDate;
import java.time.LocalTime;

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
@Table(name = "reported_hours")
public class WorkDay {

    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable=false, referencedColumnName = "id")
    private User user;

    @Column(nullable=false)
    private LocalDate date;

    @Column(nullable=false)
    private LocalTime startTime;

    @Column(nullable=true)  // ENDTIME on nullable, jotta voidaan tallentaa myös keskeneräisiä työpäiviä
    private LocalTime endTime;
    
    @Column(nullable=true, name = "breaks_total")
    private Integer breaksTotal;

    @Column(nullable=false, name = "is_holiday", columnDefinition = "boolean default false")
    private Boolean isHoliday;

    @Column(nullable=true)   // koko rajataan DTO:ssa
    private String description;


}
