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
public class ShiftDTO {

    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;

    
}
