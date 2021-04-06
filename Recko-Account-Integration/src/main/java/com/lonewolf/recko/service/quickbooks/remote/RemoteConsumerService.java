package com.lonewolf.recko.service.quickbooks.remote;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.Consumer;
import com.lonewolf.recko.entity.PartnerCredential;
import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.model.quickbooks.consumer.Account;
import com.lonewolf.recko.model.quickbooks.consumer.AccountResponse;
import com.lonewolf.recko.model.quickbooks.consumer.AccountType;
import com.lonewolf.recko.model.quickbooks.consumer.SingleAccountResponse;
import com.lonewolf.recko.service.factory.remote.RemoteConsumerContract;
import com.lonewolf.recko.service.factory.remote.RemoteTokenContract;
import com.lonewolf.recko.service.quickbooks.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service(BeanNameRepository.Quickbooks_Remote_Consumer)
@SuppressWarnings({"rawtypes"})
public class RemoteConsumerService implements RemoteConsumerContract {

    private final RestTemplate template;
    private final RemoteTokenContract tokenContract;
    private final Utils utils;

    @Autowired
    public RemoteConsumerService(@Qualifier(BeanNameRepository.Custom_Rest_Template) RestTemplate template,
                                 @Qualifier(BeanNameRepository.Quickbooks_Remote_Token) RemoteTokenContract tokenContract,
                                 @Qualifier(BeanNameRepository.Quickbooks_Utils) Utils utils) {
        this.template = template;
        this.tokenContract = tokenContract;
        this.utils = utils;
    }

    @Override
    public List<Consumer> fetchConsumers(PartnerCredential credential) {
        tokenContract.refreshToken(credential);

        String query = "select * from Account";
        String url = "https://sandbox-quickbooks.api.intuit.com/v3/company/"
                + credential.getApplicationId()
                + "/query?query=" + query;

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + credential.getAccessToken());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity requestEntity = new HttpEntity(headers);

        ResponseEntity<AccountResponse> responseEntity =
                template.exchange(url, HttpMethod.GET, requestEntity, AccountResponse.class);

        AccountResponse response = responseEntity.getBody();

        if (response == null) {
            throw new ReckoException("accounts couldn't be fetched", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        List<Account> accounts = response.getAccountCollection().getAccounts();
        if (accounts == null) {
            throw new ReckoException("accounts couldn't be fetched", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return accounts.stream()
                .map(acc -> utils.parseQuickbooksAccount(acc, credential))
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Consumer addConsumer(PartnerCredential credential, Consumer consumer) {
        tokenContract.refreshToken(credential);

        String url = "https://sandbox-quickbooks.api.intuit.com/v3/company/"
                + credential.getApplicationId() + "/account?minorversion=57";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + credential.getAccessToken());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("Name", consumer.getName());
        requestBody.put("AccountType", AccountType.parseType(consumer.getType()).getName());

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<SingleAccountResponse> responseEntity = template.postForEntity(url, requestEntity, SingleAccountResponse.class);
        SingleAccountResponse response = responseEntity.getBody();

        if (response == null) {
            throw new ReckoException("account couldn't be added", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Account account = response.getAccount();
        return utils.parseQuickbooksAccount(account, credential);
    }

    @Override
    public Consumer updateConsumer(PartnerCredential credential, Consumer consumer) {
        tokenContract.refreshToken(credential);

        String url = "https://sandbox-quickbooks.api.intuit.com/v3/company/"
                + credential.getApplicationId() + "/account?minorversion=57";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + credential.getAccessToken());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("Id", consumer.getConsumerId());
        requestBody.put("Name", consumer.getName());
        requestBody.put("SyncToken", consumer.getUpdateCount());
        requestBody.put("AccountType", AccountType.parseType(consumer.getType()).getName());

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<SingleAccountResponse> responseEntity =
                template.postForEntity(url, requestEntity, SingleAccountResponse.class);

        SingleAccountResponse response = responseEntity.getBody();
        if (response == null) {
            throw new ReckoException("account couldn't be updated", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Account updatedAccount = response.getAccount();
        return utils.parseQuickbooksAccount(updatedAccount, credential);
    }

    @Override
    public Consumer deleteConsumer(PartnerCredential credential, Consumer consumer) {
        tokenContract.refreshToken(credential);

        String url = "https://sandbox-quickbooks.api.intuit.com/v3/company/"
                + credential.getApplicationId() + "/account?minorversion=57";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + credential.getAccessToken());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("Id", consumer.getConsumerId());
        requestBody.put("Name", consumer.getName());
        requestBody.put("SyncToken", consumer.getUpdateCount());
        requestBody.put("AccountType", AccountType.parseType(consumer.getType()).getName());
        requestBody.put("Active", false);

        HttpEntity<Map<String, Object>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<SingleAccountResponse> responseEntity =
                template.postForEntity(url, requestEntity, SingleAccountResponse.class);

        SingleAccountResponse response = responseEntity.getBody();
        if (response == null) {
            throw new ReckoException("account couldn't be deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Account deletedAccount = response.getAccount();
        return utils.parseQuickbooksAccount(deletedAccount, credential);
    }
}
