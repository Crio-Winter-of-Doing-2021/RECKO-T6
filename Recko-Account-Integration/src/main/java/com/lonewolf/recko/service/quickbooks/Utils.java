package com.lonewolf.recko.service.quickbooks;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.Consumer;
import com.lonewolf.recko.entity.PartnerCredential;
import com.lonewolf.recko.entity.Transaction;
import com.lonewolf.recko.model.quickbooks.consumer.Account;
import com.lonewolf.recko.model.quickbooks.transaction.Payment;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Component(BeanNameRepository.Quickbooks_Utils)
public class Utils {

    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    public Consumer parseQuickbooksAccount(Account account, PartnerCredential credential) {
        LocalDate createDate = LocalDate.parse(account.getMetadata().getCreateDate().substring(0, 10), formatter);

        Consumer consumer = new Consumer(
                account.getId(),
                account.getName(),
                account.getBalance(),
                createDate,
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

    public Transaction parseQuickbooksTransaction(Payment payment, PartnerCredential credential) {
        String depositHolder = null;
        if (payment.getDepositAccount() != null) {
            List<Consumer> consumers = credential.getConsumers()
                    .stream()
                    .filter(consumer -> consumer.getConsumerId().equalsIgnoreCase(payment.getDepositAccount().getId()))
                    .collect(Collectors.toUnmodifiableList());

            depositHolder = consumers.isEmpty() ? null : consumers.get(0).getName();
        }

        return new Transaction(
                payment.getId(),
                payment.getAccount().getValue(),
                payment.getAccount().getPayer(),
                depositHolder,
                "Payment",
                payment.getAmount(),
                parseQuickbooksTransactionDate(payment.getMetadata().getCreateDate()),
                credential);
    }
}
