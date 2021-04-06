package com.lonewolf.recko.repository;

import com.lonewolf.recko.entity.Partner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PartnerRepository extends JpaRepository<Partner, String> {

    @Query("FROM Partner p WHERE p.isActive=true")
    List<Partner> findActivePartners();
}
