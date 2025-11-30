package com.example.biztrack.controller;

import com.example.biztrack.model.Client;
import com.example.biztrack.model.MeetingPurchase;
import com.example.biztrack.model.MeetingPurchaseSummary;
import com.example.biztrack.repository.MeetingPurchaseRepository;
import org.springframework.web.bind.annotation.*;
import com.example.biztrack.repository.ClientRepository;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


@RestController
@RequestMapping("/api/meeting-purchases")
public class MeetingPurchaseController {

    private final MeetingPurchaseRepository meetingPurchaseRepository;
    private final ClientRepository clientRepository;

    public MeetingPurchaseController(MeetingPurchaseRepository meetingPurchaseRepository,
                                     ClientRepository clientRepository) {
        this.meetingPurchaseRepository = meetingPurchaseRepository;
        this.clientRepository = clientRepository;
    }

    // כל הקניות (לכל הלקוחות)
    @GetMapping
    public List<MeetingPurchase> getAllPurchases() {
        return meetingPurchaseRepository.findAll();
    }

    // כל הקניות של לקוח מסוים
    @GetMapping("/client/{clientId}")
    public List<MeetingPurchase> getPurchasesForClient(@PathVariable Long clientId) {
        return meetingPurchaseRepository.findByClientId(clientId);
    }

    // יצירת קניה חדשה של חבילת פגישות
    @PostMapping
    public MeetingPurchase createPurchase(@RequestBody MeetingPurchase purchaseRequest) {
        // נטען את הלקוח מה-DB לפי ה-id שמגיע בבקשה
        if (purchaseRequest.getClient() == null || purchaseRequest.getClient().getId() == null) {
            throw new IllegalArgumentException("client.id is required");
        }

        Client client = clientRepository.findById(purchaseRequest.getClient().getId())
                .orElseThrow(() -> new IllegalArgumentException("Client not found"));

        purchaseRequest.setClient(client);

        // אם totalAmount לא הגיע – נחשב אותו לפי meetingsPurchased * pricePerMeeting
        if (purchaseRequest.getTotalAmount() == null &&
                purchaseRequest.getMeetingsPurchased() != null &&
                purchaseRequest.getPricePerMeeting() != null) {

            BigDecimal total = purchaseRequest.getPricePerMeeting()
                    .multiply(BigDecimal.valueOf(purchaseRequest.getMeetingsPurchased()));
            purchaseRequest.setTotalAmount(total);
        }

        // אם לא הגיע תאריך – נשים תאריך של היום
        if (purchaseRequest.getPurchaseDate() == null) {
            purchaseRequest.setPurchaseDate(LocalDate.now());
        }

        // נעדכן ללקוח את כמות הפגישות הכוללת
        Integer currentMeetings = client.getMeetingsCount() != null ? client.getMeetingsCount() : 0;
        client.setMeetingsCount(currentMeetings + purchaseRequest.getMeetingsPurchased());
        clientRepository.save(client);

        return meetingPurchaseRepository.save(purchaseRequest);
    }

    @PostMapping("/{id}/register-meeting")
    public MeetingPurchase registerMeeting(@PathVariable Long id) {
        MeetingPurchase purchase = meetingPurchaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Purchase not found"));

        // כמה פגישות כבר בוצעו (אם null – נתחיל מ-0)
        int completed = purchase.getMeetingsCompleted() != null ? purchase.getMeetingsCompleted() : 0;
        int purchased = purchase.getMeetingsPurchased() != null ? purchase.getMeetingsPurchased() : 0;

        if (completed >= purchased) {
            throw new IllegalStateException("All meetings for this package are already completed.");
        }

        purchase.setMeetingsCompleted(completed + 1);

        // עדכון סכום ששולם עד עכשיו
        BigDecimal paid = purchase.getAmountPaid() != null ? purchase.getAmountPaid() : BigDecimal.ZERO;
        BigDecimal pricePerMeeting = purchase.getPricePerMeeting() != null ? purchase.getPricePerMeeting() : BigDecimal.ZERO;

        purchase.setAmountPaid(paid.add(pricePerMeeting));

        return meetingPurchaseRepository.save(purchase);
    }
    @GetMapping("/{id}/summary")
    public MeetingPurchaseSummary getPurchaseSummary(@PathVariable Long id) {
        MeetingPurchase purchase = meetingPurchaseRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Purchase not found"));

        int purchased = purchase.getMeetingsPurchased() != null ? purchase.getMeetingsPurchased() : 0;
        int completed = purchase.getMeetingsCompleted() != null ? purchase.getMeetingsCompleted() : 0;
        int remainingMeetings = purchased - completed;

        BigDecimal total = purchase.getTotalAmount();
        if (total == null && purchase.getPricePerMeeting() != null) {
            total = purchase.getPricePerMeeting()
                    .multiply(BigDecimal.valueOf(purchased));
        }
        if (total == null) {
            total = BigDecimal.ZERO;
        }

        BigDecimal paid = purchase.getAmountPaid() != null ? purchase.getAmountPaid() : BigDecimal.ZERO;
        BigDecimal remainingAmount = total.subtract(paid);

        Long clientId = purchase.getClient() != null ? purchase.getClient().getId() : null;
        String clientName = purchase.getClient() != null ? purchase.getClient().getName() : null;

        return new MeetingPurchaseSummary(
                purchase.getId(),
                clientId,
                clientName,
                purchased,
                completed,
                remainingMeetings,
                total,
                paid,
                remainingAmount
        );
    }

}
