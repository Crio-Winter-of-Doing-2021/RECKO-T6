package com.lonewolf.recko.repository;

import com.lonewolf.recko.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {

    Optional<Admin> findByAdminNameIgnoreCase(String adminName);

    Optional<Admin> findByAdminNameAndPassword(String adminName, String password);

    Optional<Admin> findByAdminNameAndEmailIgnoreCase(String adminName, String email);

    @Query("FROM Admin a WHERE a.company.id = ?1 AND lower(a.adminName) = lower(?2)")
    Optional<Admin> findByCompany(String companyId, String adminName);
}
