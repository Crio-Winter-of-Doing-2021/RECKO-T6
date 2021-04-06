package com.lonewolf.recko.service.xero.remote;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.PartnerCredential;
import com.lonewolf.recko.entity.Transaction;
import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.model.xero.transaction.TransactionResponse;
import com.lonewolf.recko.service.factory.remote.RemoteTokenContract;
import com.lonewolf.recko.service.factory.remote.RemoteTransactionContract;
import com.lonewolf.recko.service.xero.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service(BeanNameRepository.Xero_Remote_Transaction)
@SuppressWarnings({"rawtypes"})
public class RemoteTransactionService implements RemoteTransactionContract {

    private final RemoteTokenContract tokenContract;
    private final RestTemplate template = new RestTemplate();
    private final Utils utils;

    @Autowired
    public RemoteTransactionService(@Qualifier(BeanNameRepository.Xero_Remote_Token) RemoteTokenContract tokenContract,
                                    @Qualifier(BeanNameRepository.Xero_Utils) Utils utils) {
        this.tokenContract = tokenContract;
        this.utils = utils;
    }

    @Override
    public List<Transaction> getPartnerTransactions(PartnerCredential credential) {
        tokenContract.refreshToken(credential);

        String url = "https://api.xero.com/api.xro/2.0/BankTransactions";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + credential.getAccessToken());
        headers.add("Xero-Tenant-Id", credential.getApplicationId());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity requestEntity = new HttpEntity(headers);
        ResponseEntity<TransactionResponse> responseEntity = template.exchange(url, HttpMethod.GET, requestEntity, TransactionResponse.class);

        TransactionResponse response = responseEntity.getBody();
        if (response == null) {
            throw new ReckoException("transactions couldn't be fetched", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response.getPayments()
                .stream()
                .map(payment -> utils.parseXeroTransaction(payment, credential))
                .collect(Collectors.toUnmodifiableList());
    }
}
