package com.lonewolf.recko.repository;

import com.lonewolf.recko.entity.Consumer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ConsumerRepository extends JpaRepository<Consumer, String> {

    @Query("FROM Consumer r WHERE r.isActive=true ORDER BY r.date DESC")
    List<Consumer> findActiveConsumers();

    @Query("FROM Consumer r WHERE r.credential.partner.name=?1 AND r.isActive=true ORDER BY r.date DESC")
    List<Consumer> findPartnerConsumers(String partnerName);

    @Query("FROM Consumer r WHERE r.credential.partner.name=?1 AND r.credential.email=?2 AND r.isActive=true ORDER BY r.date DESC")
    List<Consumer> findPartnerHandlerConsumers(String partnerName, String email);
}
