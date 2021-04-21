package com.lonewolf.recko.schedule;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.CompanyCredential;
import com.lonewolf.recko.entity.Transaction;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.repository.CompanyCredentialRepository;
import com.lonewolf.recko.repository.TransactionRepository;
import com.lonewolf.recko.service.factory.remote.RemoteTransactionContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.List;

@Configuration
public class TransactionFeedRefresh {

    private final CompanyCredentialRepository credentialRepository;
    private final RemoteTransactionContract xeroTransactionContract;
    private final RemoteTransactionContract quickbooksTransactionContract;
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionFeedRefresh(CompanyCredentialRepository credentialRepository,
                                  TransactionRepository transactionRepository,
                                  @Qualifier(BeanNameRepository.Xero_Remote_Transaction) RemoteTransactionContract xeroTransactionContract,
                                  @Qualifier(BeanNameRepository.Quickbooks_Remote_Transaction) RemoteTransactionContract quickbooksTransactionContract) {
        this.credentialRepository = credentialRepository;
        this.transactionRepository = transactionRepository;
        this.xeroTransactionContract = xeroTransactionContract;
        this.quickbooksTransactionContract = quickbooksTransactionContract;
    }

    @Scheduled(cron = "0 1 0 1/1 * *")
    public void xeroTransactionFeedRefresh() {
        List<CompanyCredential> credentials = credentialRepository.findByPartnerName(PartnerNameRepository.XERO.getName());

        for (CompanyCredential credential : credentials) {
            List<Transaction> transactions = xeroTransactionContract.getPartnerTransactions(credential);
            for (Transaction transaction : transactions) {
                if (transactionRepository.transactionExists(transaction.getCredential().getId(), transaction.getTransactionId())
                        .orElse(null) == null) {
                    transactionRepository.saveAndFlush(transaction);
                }
            }
        }
    }

    @Scheduled(cron = "0 21 0 1/1 * *")
    public void quickbooksTransactionFeedRefresh() {
        List<CompanyCredential> credentials = credentialRepository.findByPartnerName(PartnerNameRepository.XERO.getName());

        for (CompanyCredential credential : credentials) {
            List<Transaction> transactions = quickbooksTransactionContract.getPartnerTransactions(credential);
            for (Transaction transaction : transactions) {
                if (transactionRepository.transactionExists(transaction.getCredential().getId(), transaction.getTransactionId())
                        .orElse(null) == null) {
                    transactionRepository.saveAndFlush(transaction);
                }
            }
        }
    }
}
