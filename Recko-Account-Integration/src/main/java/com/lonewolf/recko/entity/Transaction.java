package com.lonewolf.recko.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "transactions")
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class Transaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    @Setter(AccessLevel.NONE)
    private long id;

    @Column(name = "account_id")
    private String accountId;

    @Column(name = "holder_name")
    @JsonProperty("holder")
    private String holderName;

    @Column(name = "transaction_type")
    @JsonProperty("type")
    private String transactionType;

    @Column(name = "amount")
    private double amount;

    @Column(name = "transaction_date")
    private LocalDate date;

    @ManyToOne
    private CompanyCredential credential;
}
