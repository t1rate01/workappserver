package com.backend.server.shifts.DTO;

import java.time.LocalDate;
import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShiftListDTO {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private int breaksTotal;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private String description;
    
}
