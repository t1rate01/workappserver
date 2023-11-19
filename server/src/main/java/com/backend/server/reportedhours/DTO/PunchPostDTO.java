package com.backend.server.reportedhours.DTO;

import java.time.LocalTime;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PunchPostDTO {
    private LocalTime time;
    private String email;
}
