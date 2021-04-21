package com.lonewolf.recko.repository;

import com.lonewolf.recko.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {

    @Query("FROM Transaction t WHERE t.credential.id = ?1 AND t.transactionId = ?2")
    Optional<Transaction> transactionExists(long credentialId, String transactionId);
}
