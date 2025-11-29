package com.example.biztrack.controller;

import com.example.biztrack.model.DashboardSummary;
import com.example.biztrack.model.MeetingPurchase;
import com.example.biztrack.model.MeetingStatsReport;
import com.example.biztrack.repository.ClientRepository;
import com.example.biztrack.repository.MeetingPurchaseRepository;
import com.example.biztrack.service.MeetingStatsService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

@RestController
public class DashboardController {

    private final MeetingStatsService meetingStatsService;
    private final ClientRepository clientRepository;
    private final MeetingPurchaseRepository meetingPurchaseRepository;

    public DashboardController(MeetingStatsService meetingStatsService,
                               ClientRepository clientRepository,
                               MeetingPurchaseRepository meetingPurchaseRepository) {
        this.meetingStatsService = meetingStatsService;
        this.clientRepository = clientRepository;
        this.meetingPurchaseRepository = meetingPurchaseRepository;
    }

    @GetMapping("/api/dashboard")
    public DashboardSummary getDashboard(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate
    ) {

        LocalDate start = startDate != null && !startDate.isBlank() ? LocalDate.parse(startDate) : null;
        LocalDate end = endDate != null && !endDate.isBlank() ? LocalDate.parse(endDate) : null;

        // 1) להשתמש ב-MeetingStatsService כדי לחשב את עלות/רווח לפגישה
        MeetingStatsReport stats;
        if (start == null && end == null) {
            stats = meetingStatsService.calculateStats();
        } else {
            stats = meetingStatsService.calculateStats(start, end);
        }

        // 2) כמות לקוחות
        long totalClients = clientRepository.count();

        // 3) כמות חבילות פגישות
        List<MeetingPurchase> allPurchases = meetingPurchaseRepository.findAll();
        long totalMeetingPackages = allPurchases.size();

        // 4) כמות פגישות שבוצעו ( יש לנו ב-stats, אבל אפשר לוודא)
        long totalMeetingsCompleted = stats.getTotalMeetingsCompleted();

        return new DashboardSummary(
                start,
                end,
                totalClients,
                totalMeetingPackages,
                totalMeetingsCompleted,
                stats.getTotalRelevantExpenses(),
                stats.getTotalClientPayments(),
                stats.getAverageCostPerMeeting(),
                stats.getAverageRevenuePerMeeting(),
                stats.getAverageProfitPerMeeting()
        );
    }
}
