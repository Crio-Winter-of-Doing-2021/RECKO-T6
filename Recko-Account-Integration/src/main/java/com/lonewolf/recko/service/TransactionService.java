package com.lonewolf.recko.service;

import com.lonewolf.recko.entity.CompanyCredential;
import com.lonewolf.recko.entity.Transaction;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.repository.CompanyCredentialRepository;
import com.lonewolf.recko.service.factory.PartnerResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class TransactionService {

    private final PartnerResolver partnerResolver;
    private final CompanyCredentialRepository credentialRepository;

    @Autowired
    public TransactionService(PartnerResolver partnerResolver, CompanyCredentialRepository credentialRepository) {
        this.partnerResolver = partnerResolver;
        this.credentialRepository = credentialRepository;
    }

    public List<Transaction> getTransactions(String companyId) {
        List<Transaction> transactions = new ArrayList<>();
        for (PartnerNameRepository nameRepository : PartnerNameRepository.values()) {
            transactions.addAll(getPartnerTransactions(nameRepository, companyId));
        }
        return transactions;
    }

    public List<Transaction> getPartnerTransactions(PartnerNameRepository nameRepository, String companyId) {
        List<CompanyCredential> credentials = credentialRepository.findByPartner(nameRepository.getName(), companyId);
        if (credentials.isEmpty()) {
            return Collections.emptyList();
        }

        List<Transaction> transactions = new ArrayList<>();
        for (CompanyCredential credential : credentials) {
            transactions.addAll(partnerResolver.resolveTransaction(nameRepository).getPartnerTransactions(credential));
        }

        return transactions;
    }
}
