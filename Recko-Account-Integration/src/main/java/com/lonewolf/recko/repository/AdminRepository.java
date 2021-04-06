package com.lonewolf.recko.repository;

import com.lonewolf.recko.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AdminRepository extends JpaRepository<Admin, String> {

    Optional<Admin> findByAdminNameAndPassword(String moderatorName, String password);
}
