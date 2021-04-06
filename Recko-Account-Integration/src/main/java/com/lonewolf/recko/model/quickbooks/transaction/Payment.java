package com.lonewolf.recko.model.quickbooks.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lonewolf.recko.model.quickbooks.Metadata;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Setter(AccessLevel.NONE)
public class Payment {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("CustomerRef")
    private TransactionAccount account;

    @JsonProperty("DepositToAccountRef")
    private TransactionDepositAccount depositAccount;

    @JsonProperty("TotalAmt")
    private double amount;

    @JsonProperty("MetaData")
    private Metadata metadata;
}
