package com.lonewolf.recko.repository;

import com.lonewolf.recko.entity.Moderator;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ModeratorRepository extends JpaRepository<Moderator, String> {

    Optional<Moderator> findByModeratorNameAndPassword(String moderatorName, String password);
}