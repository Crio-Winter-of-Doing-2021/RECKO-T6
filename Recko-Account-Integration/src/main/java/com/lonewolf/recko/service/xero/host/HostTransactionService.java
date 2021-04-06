package com.lonewolf.recko.service.xero.host;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.PartnerCredential;
import com.lonewolf.recko.entity.Transaction;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.repository.PartnerCredentialRepository;
import com.lonewolf.recko.repository.TransactionRepository;
import com.lonewolf.recko.service.factory.host.HostTransactionContract;
import com.lonewolf.recko.service.factory.remote.RemoteTransactionContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service(BeanNameRepository.Xero_Host_Transaction)
public class HostTransactionService implements HostTransactionContract {

    private static final String Partner_Name = PartnerNameRepository.XERO.getName();

    private final RemoteTransactionContract transactionContract;
    private final PartnerCredentialRepository credentialRepository;
    private final TransactionRepository transactionRepository;

    @Autowired
    public HostTransactionService(@Qualifier(BeanNameRepository.Xero_Remote_Transaction) RemoteTransactionContract transactionContract,
                                  PartnerCredentialRepository credentialRepository,
                                  TransactionRepository transactionRepository) {
        this.transactionContract = transactionContract;
        this.credentialRepository = credentialRepository;
        this.transactionRepository = transactionRepository;
    }

    private void addTransactionsDatabase(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            transactionRepository.saveAndFlush(transaction);
        }
    }

    @Override
    public List<Transaction> getPartnerTransactions() {
        List<PartnerCredential> credentials = credentialRepository.findByPartner(Partner_Name);

        List<Transaction> transactions = new ArrayList<>();

        for (PartnerCredential credential : credentials) {
            List<Transaction> credTransactions = credential.getTransactions();

            if (credTransactions.isEmpty()) {
                List<Transaction> remoteTransactions = transactionContract.getPartnerTransactions(credential);
                addTransactionsDatabase(remoteTransactions);
                credTransactions.addAll(remoteTransactions);
            }

            transactions.addAll(credTransactions);
        }

        return transactions;
    }
}
