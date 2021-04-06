package com.lonewolf.recko.repository;

import com.lonewolf.recko.entity.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository extends JpaRepository<Transaction, String> {
}
