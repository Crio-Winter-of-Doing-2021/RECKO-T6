package com.lonewolf.recko.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Setter(AccessLevel.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

    @Id
    @Column(name = "transaction_id")
    private String id;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "payer")
    private String holderName;

    @Column(name = "receiver")
    private String receiver;

    @Column(name = "transaction_type")
    private String type;

    @Column(name = "amount")
    private double amount;

    @Column(name = "transaction_date")
    private LocalDate date;

    @ManyToOne
    private PartnerCredential credential;
}
