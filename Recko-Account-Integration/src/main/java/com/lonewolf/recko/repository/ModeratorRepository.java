package com.lonewolf.recko.repository;

import com.lonewolf.recko.entity.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ModeratorRepository extends JpaRepository<Moderator, String> {

    Optional<Moderator> findByModeratorNameIgnoreCase(String moderatorName);

    Optional<Moderator> findByModeratorNameAndPassword(String moderatorName, String password);

    Optional<Moderator> findByModeratorNameAndEmailIgnoreCase(String moderatorName, String email);

    @Query("FROM Moderator m WHERE m.company.id = ?1 AND lower(m.moderatorName) = lower(?2)")
    Optional<Moderator> findByCompany(String companyId, String moderatorName);
}