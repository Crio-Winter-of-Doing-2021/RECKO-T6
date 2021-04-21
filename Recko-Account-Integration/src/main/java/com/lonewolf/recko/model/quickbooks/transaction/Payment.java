package com.lonewolf.recko.model.quickbooks.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Setter(AccessLevel.NONE)
public class Payment {

    @JsonProperty("Id")
    private String paymentId;

    @JsonProperty("Amount")
    private double amount;

    @JsonProperty("JournalEntryLineDetail")
    private PaymentDetail paymentDetail;
}
