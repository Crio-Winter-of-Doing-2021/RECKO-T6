package com.lonewolf.recko.model.quickbooks.consumer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Setter;

@Data
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
public class SingleAccountResponse {

    @JsonProperty("Account")
    private Account account;
}
