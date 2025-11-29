package com.example.biztrack.controller;

import com.example.biztrack.model.MeetingStatsReport;
import com.example.biztrack.service.MeetingStatsService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequestMapping("/api/reports") // 👈 כל הדוחות תחת /api/reports
public class ReportController {

    private final MeetingStatsService meetingStatsService;

    public ReportController(MeetingStatsService meetingStatsService) {
        this.meetingStatsService = meetingStatsService;
    }

    // 1. דוח כללי (על כל הזמן או עם טווח תאריכים)
    @GetMapping("/meeting-stats")
    public MeetingStatsReport getMeetingStats(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;

        if (start == null && end == null) {
            return meetingStatsService.calculateStats();
        }
        return meetingStatsService.calculateStats(start, end);
    }

    // 2. דוח לפי לקוח
    @GetMapping("/meeting-stats/client/{clientId}")
    public MeetingStatsReport getClientMeetingStats(
            @PathVariable Long clientId,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;
        return meetingStatsService.calculateStatsForClient(clientId, start, end);
    }

    // 3. דוח לפי עיר
    @GetMapping("/meeting-stats/city")
    public MeetingStatsReport getCityMeetingStats(
            @RequestParam String city,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {
        LocalDate start = startDate != null ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null ? LocalDate.parse(endDate) : null;
        return meetingStatsService.calculateStatsForCity(city, start, end);
    }
}
