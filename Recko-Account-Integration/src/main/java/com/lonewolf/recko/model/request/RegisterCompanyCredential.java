package com.lonewolf.recko.model.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Setter;

@Data
@Setter(AccessLevel.NONE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class RegisterCompanyCredential {

    private String companyId;

    private String companyPassword;

    private String remoteAccountId;

    private String remoteAccountSecret;

    private String applicationId;

    private String refreshToken;

    private String scope;

    private String email;

    private String password;
}
