package com.lonewolf.recko.repository;

import com.lonewolf.recko.entity.PartnerCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PartnerCredentialRepository extends JpaRepository<PartnerCredential, Long> {

    @Query("FROM PartnerCredential r WHERE r.partner.name=?1 AND lower(r.email)=lower(?2) AND r.password=?3")
    Optional<PartnerCredential> getCredential(String partnerName, String email, String password);

    @Query("FROM PartnerCredential r WHERE r.partner.name=?1 AND lower(r.email)=?2")
    Optional<PartnerCredential> credentialExists(String partnerName, String email);

    @Query("FROM PartnerCredential r WHERE r.partner.name=?1")
    List<PartnerCredential> findByPartner(String partnerName);
}
