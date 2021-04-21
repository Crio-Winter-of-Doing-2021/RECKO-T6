package com.lonewolf.recko.model.request;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.lonewolf.recko.model.CompanyHandlerRole;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Data
@Setter(AccessLevel.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterCompanyHandler {

    private String companyId;

    private String companyPassword;

    private String handlerName;

    private String handlerEmail;

    private String handlerPassword;

    @JsonProperty("role")
    @Enumerated(EnumType.STRING)
    @JsonIgnore
    private CompanyHandlerRole handlerRole;
}
