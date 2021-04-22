package com.lonewolf.recko.repository;

import com.lonewolf.recko.entity.CompanyCredential;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CompanyCredentialRepository extends JpaRepository<CompanyCredential, Long> {

    @Query("FROM CompanyCredential r WHERE r.id=?1 AND r.partner.name=?2 AND r.company.id=?3 AND lower(r.email)=lower(?4) AND r.password=?5")
    Optional<CompanyCredential> getCredential(long id, String partnerName, String companyId, String email, String password);

    @Query("FROM CompanyCredential r WHERE r.partner.name=?1 AND r.company.id=?2")
    List<CompanyCredential> findByPartnerInCompany(String partnerName, String companyId);

    @Query("FROM CompanyCredential r WHERE r.partner.name=?1")
    List<CompanyCredential> findByPartnerName(String partnerName);

    boolean existsByApplicationId(String applicationId);
}
