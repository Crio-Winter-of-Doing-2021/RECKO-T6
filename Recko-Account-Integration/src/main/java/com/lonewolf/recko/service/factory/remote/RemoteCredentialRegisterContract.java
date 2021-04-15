package com.lonewolf.recko.service.factory.remote;

import com.lonewolf.recko.entity.CompanyCredential;
import com.lonewolf.recko.model.request.RegisterCompanyCredential;

public interface RemoteCredentialRegisterContract {

    CompanyCredential registerCompanyCredential(RegisterCompanyCredential companyCredential);
}
