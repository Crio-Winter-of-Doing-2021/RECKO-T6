package com.lonewolf.recko.repository;

import com.lonewolf.recko.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CompanyRepository extends JpaRepository<Company, String> {

    boolean existsByIdIgnoreCase(String id);

    Optional<Company> findByIdIgnoreCase(String id);
}
