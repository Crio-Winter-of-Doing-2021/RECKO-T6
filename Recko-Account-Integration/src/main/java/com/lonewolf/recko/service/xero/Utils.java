package com.lonewolf.recko.service.xero;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.Consumer;
import com.lonewolf.recko.entity.PartnerCredential;
import com.lonewolf.recko.entity.Transaction;
import com.lonewolf.recko.model.xero.consumer.Account;
import com.lonewolf.recko.model.xero.consumer.AccountStatus;
import com.lonewolf.recko.model.xero.transaction.Payment;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component(BeanNameRepository.Xero_Utils)
public class Utils {

    private static final double Account_Balance = 0;

    private Utils() {
    }

    private LocalDate parseXeroAccountDate(String date) {
        int startIndex = date.indexOf('(');
        int endIndex = date.indexOf('+');
        return Instant
                .ofEpochMilli(Long.parseLong(date.substring(startIndex + 1, endIndex)))
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    public Consumer parseXeroConsumer(Account account, PartnerCredential credential) {
        Consumer consumer = new Consumer(
                account.getId(),
                account.getName(),
                Account_Balance,
                parseXeroAccountDate(account.getDate()),
                account.getType().getName(),
                credential);
        consumer.setActive(account.getStatus() == AccountStatus.Active);
        return consumer;
    }

    private LocalDate parseXeroTransactionDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return LocalDate.parse(date.substring(0, 10), formatter);
    }

    public Transaction parseXeroTransaction(Payment payment, PartnerCredential credential) {
        return new Transaction(
                payment.getTransactionId(),
                payment.getAccount().getAccountId(),
                payment.getAccount().getHolderName(),
                payment.getReceiver().getName(),
                payment.getType(),
                payment.getAmount(),
                parseXeroTransactionDate(payment.getTransactionDate()),
                credential);
    }
}
