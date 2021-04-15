package com.lonewolf.recko.service.xero.remote;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.CompanyCredential;
import com.lonewolf.recko.entity.Transaction;
import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.model.xero.transaction.Journal;
import com.lonewolf.recko.model.xero.transaction.JournalResponse;
import com.lonewolf.recko.service.factory.remote.RemoteTokenContract;
import com.lonewolf.recko.service.factory.remote.RemoteTransactionContract;
import com.lonewolf.recko.service.xero.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service(BeanNameRepository.Xero_Remote_Transaction)
@SuppressWarnings({"rawtypes", "unchecked"})
public class RemoteTransactionService implements RemoteTransactionContract {

    private final RemoteTokenContract tokenContract;
    private final RestTemplate template;
    private final Utils utils;

    @Autowired
    public RemoteTransactionService(@Qualifier(BeanNameRepository.Xero_Remote_Token) RemoteTokenContract tokenContract,
                                    @Qualifier(BeanNameRepository.Custom_Rest_Template) RestTemplate template,
                                    @Qualifier(BeanNameRepository.Xero_Utils) Utils utils) {
        this.tokenContract = tokenContract;
        this.template = template;
        this.utils = utils;
    }

    @Override
    public List<Transaction> getPartnerTransactions(CompanyCredential credential) {
        tokenContract.refreshToken(credential);

        String url = "https://api.xero.com/api.xro/2.0/Journals";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + credential.getAccessToken());
        headers.add("Xero-Tenant-Id", credential.getApplicationId());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity requestEntity = new HttpEntity(headers);
        ResponseEntity<JournalResponse> responseEntity = template.exchange(url, HttpMethod.GET, requestEntity, JournalResponse.class);

        JournalResponse response = responseEntity.getBody();
        if (response == null) {
            throw new ReckoException("transactions couldn't be fetched", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<Transaction> partnerTransactions = new ArrayList<>();

        for (Journal journal : response.getJournals()) {
            partnerTransactions.addAll(utils.parseXeroTransaction(credential, journal.getJournalDate(), journal.getPayments()));
        }

        return partnerTransactions;
    }
}
