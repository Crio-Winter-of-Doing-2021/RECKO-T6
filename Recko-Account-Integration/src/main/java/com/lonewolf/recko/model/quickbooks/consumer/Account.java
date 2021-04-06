package com.lonewolf.recko.model.quickbooks.consumer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lonewolf.recko.model.quickbooks.Metadata;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Setter(AccessLevel.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Account {

    @JsonProperty("Id")
    private String id;

    @JsonProperty("Name")
    private String name;

    @JsonProperty("AccountType")
    @Enumerated(EnumType.STRING)
    private AccountType type;

    @JsonProperty("Active")
    private boolean isActive;

    @JsonProperty("CurrentBalance")
    private double balance;

    @JsonProperty("MetaData")
    private Metadata metadata;

    @JsonProperty("SyncToken")
    private String syncToken;
}
