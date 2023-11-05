package com.backend.server.users;

import java.util.List;

import org.springframework.stereotype.Service;

import com.backend.server.reportedhours.WorkDay;
import com.backend.server.reportedhours.WorkDayRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WorkDayService {
    private final WorkDayRepository workDayRepository;


    public WorkDay saveWorkDay(WorkDay workDay) {
        return workDayRepository.save(workDay);
    }

    public WorkDay getWorkDayById(Long id) {
        return workDayRepository.findById(id).orElse(null);
    }

    public String deleteWorkDay(Long id) {
        workDayRepository.deleteById(id);
        return "WorkDay removed !! " + id;
    }

    public List<WorkDay> getWorkDaysById(Long id) {
        return workDayRepository.findAllByUserId(id);
    }



    
}
