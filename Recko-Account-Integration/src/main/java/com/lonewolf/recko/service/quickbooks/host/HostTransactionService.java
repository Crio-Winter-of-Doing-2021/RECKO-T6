package com.lonewolf.recko.service.quickbooks.host;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.CompanyCredential;
import com.lonewolf.recko.entity.Transaction;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.repository.TransactionRepository;
import com.lonewolf.recko.service.factory.host.HostTransactionContract;
import com.lonewolf.recko.service.factory.remote.RemoteTransactionContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;

@Service(BeanNameRepository.Quickbooks_Host_Transaction)
public class HostTransactionService implements HostTransactionContract {

    private static final String Partner_Name = PartnerNameRepository.QUICKBOOKS.getName();

    private final TransactionRepository transactionRepository;
    private final RemoteTransactionContract transactionContract;

    @Autowired
    public HostTransactionService(@Qualifier(BeanNameRepository.Quickbooks_Remote_Transaction) RemoteTransactionContract transactionContract,
                                  TransactionRepository transactionRepository) {
        this.transactionContract = transactionContract;
        this.transactionRepository = transactionRepository;
    }

    private void addTransactionsDatabase(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            transactionRepository.saveAndFlush(transaction);
        }
    }

    @Override
    public List<Transaction> getPartnerTransactions(CompanyCredential credential) {
        List<Transaction> transactions = credential.getTransactions();

        if (transactions.isEmpty()) {
            List<Transaction> remoteTransactions = transactionContract.getPartnerTransactions(credential);
            addTransactionsDatabase(remoteTransactions);
            transactions.addAll(remoteTransactions);
        }

        return transactions;
    }
}
