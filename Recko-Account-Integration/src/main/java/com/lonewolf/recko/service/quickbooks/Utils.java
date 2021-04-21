package com.lonewolf.recko.service.quickbooks;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.CompanyCredential;
import com.lonewolf.recko.entity.Consumer;
import com.lonewolf.recko.entity.Transaction;
import com.lonewolf.recko.model.quickbooks.consumer.Account;
import com.lonewolf.recko.model.quickbooks.transaction.JournalEntry;
import com.lonewolf.recko.model.quickbooks.transaction.Payment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Component(BeanNameRepository.Quickbooks_Utils)
public class Utils {

    public Consumer parseQuickbooksAccount(Account account, CompanyCredential credential) {
        Consumer consumer = new Consumer(
                account.getId(),
                account.getName(),
                account.getBalance(),
                parseQuickbooksTransactionDate(account.getMetadata().getCreateDate()),
                account.getType().getName(),
                credential
        );
        consumer.setActive(account.isActive());
        consumer.setUpdateCount(account.getSyncToken());

        return consumer;
    }

    private LocalDate parseQuickbooksTransactionDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date.substring(0, 10), formatter);
    }

    public List<Transaction> parseQuickbooksTransaction(CompanyCredential credential, JournalEntry journalEntry) {
        LocalDate transactionDate = parseQuickbooksTransactionDate(journalEntry.getTransactionDate());

        List<Transaction> transactions = new ArrayList<>();
        for (Payment payment : journalEntry.getPayments()) {
            Transaction transaction = new Transaction();

            transaction.setTransactionId(payment.getPaymentId());

            transaction.setAccountId(payment.getPaymentDetail().getAccount().getId());
            transaction.setHolderName(payment.getPaymentDetail().getAccount().getHolder());

            transaction.setAmount(payment.getAmount());
            transaction.setDate(transactionDate);
            transaction.setTransactionType(payment.getPaymentDetail().getType());

            transaction.setCredential(credential);

            transactions.add(transaction);
        }

        return transactions;
    }
}
