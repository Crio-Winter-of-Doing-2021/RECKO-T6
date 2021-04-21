package com.lonewolf.recko.schedule;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.CompanyCredential;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.repository.CompanyCredentialRepository;
import com.lonewolf.recko.service.factory.remote.RemoteTokenContract;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Configuration
public class RenewRefreshToken {

    private static final long Quickbooks_Refresh_Token_Validity = 99;
    private static final long Xero_Refresh_Token_Validity = 59;

    private final CompanyCredentialRepository credentialRepository;
    private final RemoteTokenContract xeroTokenContract;
    private final RemoteTokenContract quickbooksTokenContract;

    public RenewRefreshToken(CompanyCredentialRepository credentialRepository,
                             @Qualifier(BeanNameRepository.Xero_Remote_Token) RemoteTokenContract xeroTokenContract,
                             @Qualifier(BeanNameRepository.Quickbooks_Remote_Token) RemoteTokenContract quickbooksTokenContract) {
        this.credentialRepository = credentialRepository;
        this.xeroTokenContract = xeroTokenContract;
        this.quickbooksTokenContract = quickbooksTokenContract;
    }

    @Scheduled(cron = "0 1 0 1/1 * *")
    public void renewQuickbooksRefreshToken() {
        List<CompanyCredential> credentials = credentialRepository.findByPartnerName(PartnerNameRepository.QUICKBOOKS.getName());
        for (CompanyCredential credential : credentials) {
            if (credential.getLastAccess().until(LocalDateTime.now(), ChronoUnit.DAYS) >= Quickbooks_Refresh_Token_Validity) {
                quickbooksTokenContract.refreshToken(credential);
            }
        }
    }

    @Scheduled(cron = "0 21 0 1/1 * *")
    public void renewXeroRefreshToken() {
        List<CompanyCredential> credentials = credentialRepository.findByPartnerName(PartnerNameRepository.XERO.getName());
        for (CompanyCredential credential : credentials) {
            if (credential.getLastAccess().until(LocalDateTime.now(), ChronoUnit.DAYS) >= Xero_Refresh_Token_Validity) {
                xeroTokenContract.refreshToken(credential);
            }
        }
    }
}
