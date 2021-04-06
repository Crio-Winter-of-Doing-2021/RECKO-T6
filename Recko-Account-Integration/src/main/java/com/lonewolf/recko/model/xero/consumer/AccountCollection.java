package com.lonewolf.recko.model.xero.consumer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
@Data
@Setter(AccessLevel.NONE)
public class AccountCollection {

    @JsonProperty("Accounts")
    private final List<Account> accounts = new ArrayList<>();
}
