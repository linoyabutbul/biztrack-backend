package com.example.biztrack.service;

import com.example.biztrack.model.*;
import com.example.biztrack.repository.ClientRepository;
import com.example.biztrack.repository.IncomeRepository;
import com.example.biztrack.repository.MeetingPurchaseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // 👈 להוסיף import

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
public class ClientBillingService {

    private final ClientRepository clientRepository;
    private final MeetingPurchaseRepository meetingPurchaseRepository;
    private final IncomeRepository incomeRepository;
    private final MeetingStatsService meetingStatsService;

    public ClientBillingService(ClientRepository clientRepository,
                                MeetingPurchaseRepository meetingPurchaseRepository,
                                IncomeRepository incomeRepository,
                                MeetingStatsService meetingStatsService) {
        this.clientRepository = clientRepository;
        this.meetingPurchaseRepository = meetingPurchaseRepository;
        this.incomeRepository = incomeRepository;
        this.meetingStatsService = meetingStatsService;
    }

    public ClientBillingSummary getClientBillingSummary(Long clientId) {
        Client client = clientRepository.findById(clientId)
                .orElseThrow(() -> new IllegalArgumentException("Client not found with id: " + clientId));

        // כל חבילות הפגישות של הלקוח
        List<MeetingPurchase> purchases = meetingPurchaseRepository.findAll().stream()
                .filter(mp -> mp.getClient() != null && clientId.equals(mp.getClient().getId()))
                .toList();

        int totalMeetingsPurchased = purchases.stream()
                .map(mp -> mp.getMeetingsPurchased() != null ? mp.getMeetingsPurchased() : 0)
                .reduce(0, Integer::sum);

        int totalMeetingsCompleted = purchases.stream()
                .map(mp -> mp.getMeetingsCompleted() != null ? mp.getMeetingsCompleted() : 0)
                .reduce(0, Integer::sum);

        int totalMeetingsRemaining = totalMeetingsPurchased - totalMeetingsCompleted;
        if (totalMeetingsRemaining < 0) {
            totalMeetingsRemaining = 0;
        }

        // כל ההכנסות של הלקוח מסוג CLIENT_PAYMENT
        List<Income> incomes = incomeRepository.findAll().stream()
                .filter(in -> in.getClient() != null &&
                        clientId.equals(in.getClient().getId()) &&
                        "CLIENT_PAYMENT".equalsIgnoreCase(in.getIncomeType()))
                .toList();

        BigDecimal totalClientPayments = incomes.stream()
                .map(in -> in.getAmount() != null ? in.getAmount() : BigDecimal.ZERO)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal averageRevenuePerMeetingForClient = BigDecimal.ZERO;
        if (totalMeetingsCompleted > 0) {
            averageRevenuePerMeetingForClient = totalClientPayments
                    .divide(BigDecimal.valueOf(totalMeetingsCompleted), 2, RoundingMode.HALF_UP);
        }

        // סטטיסטיקה גלובלית – עלות ממוצעת ורווח ממוצע לפגישה בכל העסק
        MeetingStatsReport globalStats = meetingStatsService.calculateStats();
        BigDecimal globalAverageCostPerMeeting = globalStats.getAverageCostPerMeeting();
        BigDecimal globalAverageProfitPerMeeting = globalStats.getAverageProfitPerMeeting();

        return new ClientBillingSummary(
                client.getId(),
                client.getName(),
                client.getCity(),
                totalMeetingsPurchased,
                totalMeetingsCompleted,
                totalMeetingsRemaining,
                totalClientPayments,
                averageRevenuePerMeetingForClient,
                globalAverageCostPerMeeting,
                globalAverageProfitPerMeeting
        );
    }

    // ✅ מתודה חדשה – איחוד שני לקוחות
    @Transactional
    public void mergeClients(Long mainClientId, Long duplicateClientId) {

        if (mainClientId.equals(duplicateClientId)) {
            throw new IllegalArgumentException("mainClientId and duplicateClientId must be different");
        }

        Client main = clientRepository.findById(mainClientId)
                .orElseThrow(() -> new IllegalArgumentException("Main client not found with id: " + mainClientId));

        Client duplicate = clientRepository.findById(duplicateClientId)
                .orElseThrow(() -> new IllegalArgumentException("Duplicate client not found with id: " + duplicateClientId));

        // 1) להעביר הכנסות מהלקוח הכפול ללקוח הראשי
        List<Income> incomes = incomeRepository.findAll().stream()
                .filter(in -> in.getClient() != null &&
                        duplicateClientId.equals(in.getClient().getId()))
                .toList();

        incomes.forEach(in -> in.setClient(main));
        incomeRepository.saveAll(incomes);

        // 2) להעביר חבילות פגישות מהלקוח הכפול ללקוח הראשי
        List<MeetingPurchase> purchases = meetingPurchaseRepository.findAll().stream()
                .filter(mp -> mp.getClient() != null &&
                        duplicateClientId.equals(mp.getClient().getId()))
                .toList();

        purchases.forEach(mp -> mp.setClient(main));
        meetingPurchaseRepository.saveAll(purchases);

        // 3) למחוק את הלקוח הכפול
        clientRepository.delete(duplicate);
    }
}
