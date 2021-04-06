package com.lonewolf.recko.service.factory.remote;

import com.lonewolf.recko.entity.PartnerCredential;

public interface RemoteTokenContract {

    void reauthorize(PartnerCredential credential);

    void refreshToken(PartnerCredential credential);
}
