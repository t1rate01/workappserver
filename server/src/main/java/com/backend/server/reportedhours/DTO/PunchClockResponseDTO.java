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
public class PunchClockResponseDTO {

    private String firstName;
    private String lastName;
    private Boolean isAtWork;
    private LocalTime startTime;
    private LocalTime endTime;
    private LocalDate date;
    private String description;

}
