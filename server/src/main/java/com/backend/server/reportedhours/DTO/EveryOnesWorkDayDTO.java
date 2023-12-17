package com.backend.server.reportedhours.DTO;

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
public class EveryOnesWorkDayDTO {
    private Long id;
    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer breaksTotal;
    private String description;
    private Boolean isHoliday;
    
    
}
