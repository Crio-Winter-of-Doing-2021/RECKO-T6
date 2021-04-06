package com.lonewolf.recko.model.xero.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Setter(AccessLevel.NONE)
public class Payment {

    @JsonProperty("BankTransactionID")
    private String transactionId;

    @JsonProperty("BankAccount")
    private TransactionAccount account;

    @JsonProperty("Contact")
    private TransactionContact receiver;

    @JsonProperty("Type")
    private String type;

    @JsonProperty("Total")
    private double amount;

    @JsonProperty("DateString")
    private String transactionDate;
}
