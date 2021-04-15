package com.lonewolf.recko.service.quickbooks.remote;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.CompanyCredential;
import com.lonewolf.recko.entity.Transaction;
import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.model.quickbooks.transaction.JournalEntry;
import com.lonewolf.recko.model.quickbooks.transaction.TransactionResponse;
import com.lonewolf.recko.service.factory.remote.RemoteTokenContract;
import com.lonewolf.recko.service.factory.remote.RemoteTransactionContract;
import com.lonewolf.recko.service.quickbooks.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service(BeanNameRepository.Quickbooks_Remote_Transaction)
@SuppressWarnings({"rawtypes"})
public class RemoteTransactionService implements RemoteTransactionContract {

    private final RestTemplate template;
    private final Utils utils;
    private final RemoteTokenContract tokenContract;

    @Autowired
    public RemoteTransactionService(@Qualifier(BeanNameRepository.Quickbooks_Remote_Token) RemoteTokenContract tokenContract,
                                    @Qualifier(BeanNameRepository.Custom_Rest_Template) RestTemplate template,
                                    @Qualifier(BeanNameRepository.Quickbooks_Utils) Utils utils) {
        this.tokenContract = tokenContract;
        this.template = template;
        this.utils = utils;
    }

    @Override
    public List<Transaction> getPartnerTransactions(CompanyCredential credential) {
        tokenContract.refreshToken(credential);

        String query = "select * from JournalEntry";
        String url = "https://sandbox-quickbooks.api.intuit.com/v3/company/"
                + credential.getApplicationId()
                + "/query?query="
                + query;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + credential.getAccessToken());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity requestEntity = new HttpEntity(headers);
        ResponseEntity<TransactionResponse> responseEntity = template.exchange(url, HttpMethod.GET, requestEntity, TransactionResponse.class);

        TransactionResponse response = responseEntity.getBody();
        if (response == null) {
            throw new ReckoException("transactions couldn't be changed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<Transaction> transactions = new ArrayList<>();
        for (JournalEntry journalEntry : response.getCollection().getEntries()) {
            transactions.addAll(utils.parseQuickbooksTransaction(credential, journalEntry));
        }

        return transactions;
    }
}
