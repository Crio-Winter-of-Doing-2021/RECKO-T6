package com.lonewolf.recko.service.factory.remote;

import com.lonewolf.recko.entity.CompanyCredential;

public interface RemoteTokenContract {

    void refreshToken(CompanyCredential credential);
}
