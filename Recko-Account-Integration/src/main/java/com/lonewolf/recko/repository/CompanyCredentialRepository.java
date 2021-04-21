package com.lonewolf.recko.repository;

import com.lonewolf.recko.entity.CompanyCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyCredentialRepository extends JpaRepository<CompanyCredential, Long> {

    @Query("FROM CompanyCredential r WHERE r.partner.name=?1 AND r.company.id = ?2 AND lower(r.email)=lower(?3) AND r.password=?4")
    Optional<CompanyCredential> getCredential(String partnerName, String companyId, String email, String password);

    @Query("FROM CompanyCredential r WHERE r.partner.name=?1 AND r.company.id = ?2")
    List<CompanyCredential> findByPartner(String partnerName, String companyId);

    @Query("FROM CompanyCredential r WHERE r.company.name = ?1")
    List<CompanyCredential> findByPartnerName(String partnerName);

    boolean existsByApplicationId(String applicationId);
}
