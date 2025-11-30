package com.example.biztrack.service;

import com.example.biztrack.model.Appointment;
import com.example.biztrack.model.Client;
import com.example.biztrack.repository.AppointmentRepository;
import com.example.biztrack.repository.ClientRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final ClientRepository clientRepository;

    public AppointmentService(AppointmentRepository appointmentRepository,
                              ClientRepository clientRepository) {
        this.appointmentRepository = appointmentRepository;
        this.clientRepository = clientRepository;
    }

    /**
     * יצירת פגישה שבוצעה (HELD) לפי Email ותאריך שמגיעים מה-Webhook
     */
    public void createCompletedAppointment(String email,
                                           String externalId,
                                           String source,
                                           String appointmentTimeStr) {

        // בלי אימייל – אין איך לשייך ללקוח
        if (email == null || email.isBlank()) {
            return;
        }

        // למצוא לקוח לפי email
        Optional<Client> clientOpt = clientRepository.findByEmailIgnoreCase(email);
        if (clientOpt.isEmpty()) {
            // אפשרות: ליצור לקוח חדש אוטומטית – כרגע מדלגים
            return;
        }
        Client client = clientOpt.get();

        // אם כבר שמרנו appointment עם אותו externalId – לא לשמור שוב
        if (externalId != null && appointmentRepository.findByExternalId(externalId).isPresent()) {
            return;
        }

        // מנסים לפרסר את התאריך/זמן
        LocalDateTime happenedAt = null;
        if (appointmentTimeStr != null && !appointmentTimeStr.isBlank()) {
            try {
                // כרגע LocalDateTime.parse – נעדכן לפי הפורמט האמיתי כשה-Webhook מגוהיילבל יהיה ברור
                happenedAt = LocalDateTime.parse(appointmentTimeStr);
            } catch (Exception e) {
                // אם הפורמט שונה – נשאיר null בינתיים
            }
        }

        // יצירת Appointment נקי – כל פגישה שנכנסת לכאן = בוצעה
        Appointment appointment = new Appointment();
        appointment.setClient(client);
        appointment.setExternalId(externalId);
        appointment.setSource(source);
        appointment.setHappenedAt(happenedAt);

        appointmentRepository.save(appointment);
    }

}
