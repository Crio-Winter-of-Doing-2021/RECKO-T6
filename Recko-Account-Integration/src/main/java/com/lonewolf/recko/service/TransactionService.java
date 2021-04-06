package com.lonewolf.recko.service;

import com.lonewolf.recko.entity.Transaction;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.service.factory.PartnerResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private final PartnerResolver resolver;

    @Autowired
    public TransactionService(PartnerResolver resolver) {
        this.resolver = resolver;
    }

    public List<Transaction> getTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        for (PartnerNameRepository nameRepository : PartnerNameRepository.values()) {
            transactions.addAll(getPartnerTransactions(nameRepository));
        }
        return transactions;
    }

    public List<Transaction> getPartnerTransactions(PartnerNameRepository nameRepository) {
        return resolver.resolveTransaction(nameRepository).getPartnerTransactions();
    }
}
