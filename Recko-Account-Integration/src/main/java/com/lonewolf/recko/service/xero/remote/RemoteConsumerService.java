package com.lonewolf.recko.service.xero.remote;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.Consumer;
import com.lonewolf.recko.entity.PartnerCredential;
import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.model.xero.Tenant;
import com.lonewolf.recko.model.xero.consumer.Account;
import com.lonewolf.recko.model.xero.consumer.AccountCollection;
import com.lonewolf.recko.model.xero.consumer.AccountStatus;
import com.lonewolf.recko.model.xero.consumer.AccountType;
import com.lonewolf.recko.service.factory.remote.RemoteConsumerContract;
import com.lonewolf.recko.service.factory.remote.RemoteTokenContract;
import com.lonewolf.recko.service.xero.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;
import java.util.stream.Collectors;

@Service(BeanNameRepository.Xero_Remote_Consumer)
@SuppressWarnings({"rawtypes", "unchecked"})
public class RemoteConsumerService implements RemoteConsumerContract {

    private final RestTemplate template;
    private final RemoteTokenContract tokenContract;
    private final Utils utils;

    @Autowired
    public RemoteConsumerService(@Qualifier(BeanNameRepository.Xero_Remote_Token) RemoteTokenContract tokenContract,
                                 @Qualifier(BeanNameRepository.Custom_Rest_Template) RestTemplate template,
                                 @Qualifier(BeanNameRepository.Xero_Utils) Utils utils) {
        this.tokenContract = tokenContract;
        this.template = template;
        this.utils = utils;
    }

    private String remoteFetchTenant(PartnerCredential credential, String accessToken) {
        tokenContract.refreshToken(credential);

        String url = "https://api.xero.com/connections";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity requestEntity = new HttpEntity(headers);

        ResponseEntity<Tenant[]> tenantEntity = template.exchange(url, HttpMethod.GET, requestEntity, Tenant[].class);

        Tenant[] newTenants = tenantEntity.getBody();
        if (newTenants == null) {
            throw new ReckoException("tenant couldn't be fetched", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return newTenants[0].getTenantId();
    }

    @Override
    public List<Consumer> fetchConsumers(PartnerCredential credential) {
        tokenContract.refreshToken(credential);

        String url = "https://api.xero.com/api.xro/2.0/Accounts";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + credential.getAccessToken());
        headers.add("Xero-Tenant-Id", credential.getApplicationId());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        HttpEntity requestEntity = new HttpEntity(headers);

        ResponseEntity<AccountCollection> responseEntity =
                template.exchange(url, HttpMethod.GET, requestEntity, AccountCollection.class);

        AccountCollection response = responseEntity.getBody();
        if (response == null) {
            throw new ReckoException("error fetching accounts from remote service", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return response.getAccounts().stream()
                .map(account -> utils.parseXeroConsumer(account, credential))
                .peek(account -> account.setUpdateCount("0"))
                .collect(Collectors.toUnmodifiableList());
    }


    @Override
    public Consumer addConsumer(PartnerCredential credential, Consumer consumer) {
        tokenContract.refreshToken(credential);

        String url = "https://api.xero.com/api.xro/2.0/Accounts";

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + credential.getAccessToken());
        headers.add("Xero-Tenant-Id", credential.getApplicationId());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> accountBody = new HashMap<>();
        accountBody.put("Name", consumer.getName());
        accountBody.put("Type", AccountType.parseType(consumer.getType()).getName());
        accountBody.put("Code", UUID.randomUUID().toString().substring(0, 10));

        if (AccountType.parseType(consumer.getType()) == AccountType.Bank) {
            long bankAccountNumber = (long) (Math.random() * Math.pow(10, 8));
            accountBody.put("BankAccountNumber", Long.toString(bankAccountNumber));
        }

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity(accountBody, headers);
        ResponseEntity<AccountCollection> responseEntity = template.exchange(url, HttpMethod.PUT, requestEntity, AccountCollection.class);

        AccountCollection collection = responseEntity.getBody();
        if (collection == null || collection.getAccounts() == null || collection.getAccounts().isEmpty()) {
            throw new ReckoException("account couldn't be added", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Account newAccount = collection.getAccounts().get(0);

        return utils.parseXeroConsumer(newAccount, credential);
    }

    @Override
    public Consumer updateConsumer(PartnerCredential credential, Consumer consumer) {
        tokenContract.refreshToken(credential);

        String url = "https://api.xero.com/api.xro/2.0/Accounts/" + consumer.getConsumerId();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + credential.getAccessToken());
        headers.add("Xero-Tenant-Id", credential.getApplicationId());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("AccountID", consumer.getConsumerId());
        requestBody.put("Name", consumer.getName());

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<AccountCollection> responseEntity = template.exchange(url, HttpMethod.POST, requestEntity, AccountCollection.class);

        AccountCollection collection = responseEntity.getBody();
        if (collection == null || collection.getAccounts() == null || collection.getAccounts().isEmpty()) {
            throw new ReckoException("account couldn't be updated", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Account account = collection.getAccounts().get(0);
        return utils.parseXeroConsumer(account, credential);
    }

    @Override
    public Consumer deleteConsumer(PartnerCredential credential, Consumer consumer) {
        tokenContract.refreshToken(credential);

        String url = "https://api.xero.com/api.xro/2.0/Accounts/" + consumer.getConsumerId();

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + credential.getAccessToken());
        headers.add("Xero-Tenant-Id", credential.getApplicationId());
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("AccountID", consumer.getConsumerId());
        requestBody.put("Status", AccountStatus.Archived.getName());

        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<AccountCollection> responseEntity = template.exchange(url, HttpMethod.POST, requestEntity, AccountCollection.class);

        AccountCollection collection = responseEntity.getBody();
        if (collection == null || collection.getAccounts() == null || collection.getAccounts().isEmpty()) {
            throw new ReckoException("account couldn't be deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        Account account = collection.getAccounts().get(0);
        return utils.parseXeroConsumer(account, credential);
    }
}
