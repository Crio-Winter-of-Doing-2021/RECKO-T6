package com.lonewolf.recko.service.xero;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.CompanyCredential;
import com.lonewolf.recko.entity.Consumer;
import com.lonewolf.recko.entity.Transaction;
import com.lonewolf.recko.model.xero.consumer.Account;
import com.lonewolf.recko.model.xero.consumer.AccountStatus;
import com.lonewolf.recko.model.xero.transaction.Payment;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Component(BeanNameRepository.Xero_Utils)
public class Utils {

    private static final double Account_Balance = 0;

    private Utils() {
    }

    private LocalDate parseXeroActivityDate(String date) {
        int startIndex = date.indexOf('(');
        int endIndex = date.indexOf('+');
        return Instant
                .ofEpochMilli(Long.parseLong(date.substring(startIndex + 1, endIndex)))
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public Consumer parseXeroConsumer(Account account, CompanyCredential credential) {
        Consumer consumer = new Consumer(
                account.getId(),
                account.getName(),
                Account_Balance,
                parseXeroActivityDate(account.getDate()),
                account.getType().getName(),
                credential);
        consumer.setActive(account.getStatus() == AccountStatus.Active);
        return consumer;
    }

    public List<Transaction> parseXeroTransaction(CompanyCredential credential, String transactionDate, List<Payment> payments) {
        List<Transaction> transactions = new ArrayList<>();

        for (Payment payment : payments) {
            Transaction transaction = new Transaction();

            transaction.setTransactionId(payment.getPaymentId());

            transaction.setAccountId(payment.getAccountId());
            transaction.setHolderName(payment.getAccountHolder());

            transaction.setAmount(Math.abs(payment.getAmount()));
            transaction.setTransactionType(payment.getAmount() >= 0 ? "Debit" : "Credit");
            transaction.setDate(parseXeroActivityDate(transactionDate));

            transaction.setCredential(credential);

            transactions.add(transaction);
        }

        return transactions;
    }
}
