package com.lonewolf.recko.model.xero.consumer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lonewolf.recko.model.xero.AccountType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Setter(AccessLevel.NONE)
public class Account {

    @JsonProperty("AccountID")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("Type")
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @JsonProperty("Status")
    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @JsonProperty("UpdatedDateUTC")
    private String date;
}
