package com.backend.server.reportedhours.DTO;


import java.time.LocalDate;
import java.time.LocalTime;

import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class WorkDayDTO {

    private Long userId;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Integer breaksTotal;
    private Boolean isHoliday = false;

    @Size(max=255, message="Description can't be longer than 255 characters")
    private String description;
}
