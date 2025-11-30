package com.example.biztrack.repository;

import com.example.biztrack.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingPurchaseRepository extends JpaRepository<MeetingPurchase, Long> {

    // כל הרכישות של לקוח מסוים
    List<MeetingPurchase> findByClientId(Long clientId);
}
