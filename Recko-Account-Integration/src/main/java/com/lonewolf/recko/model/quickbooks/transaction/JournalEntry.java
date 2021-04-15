package com.lonewolf.recko.model.quickbooks.transaction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Setter(AccessLevel.NONE)
public class JournalEntry {

    @JsonProperty("TxnDate")
    private String transactionDate;

    @JsonProperty("Line")
    private List<Payment> payments;
}
