package com.lonewolf.recko.service.xero.remote;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.Company;
import com.lonewolf.recko.entity.CompanyCredential;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.model.request.RegisterCompanyCredential;
import com.lonewolf.recko.model.xero.Tenant;
import com.lonewolf.recko.model.xero.Token;
import com.lonewolf.recko.repository.CompanyCredentialRepository;
import com.lonewolf.recko.repository.CompanyRepository;
import com.lonewolf.recko.service.PartnerService;
import com.lonewolf.recko.service.factory.remote.RemoteCredentialRegisterContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;

@Component(BeanNameRepository.Xero_Credential_Register)
@SuppressWarnings({"rawtypes", "unchecked"})
public class RemoteCredentialRegisterService implements RemoteCredentialRegisterContract {

    private final CompanyRepository companyRepository;
    private final CompanyCredentialRepository credentialRepository;
    private final RestTemplate template;
    private final PartnerService partnerService;

    @Autowired
    public RemoteCredentialRegisterService(CompanyRepository companyRepository,
                                           CompanyCredentialRepository credentialRepository,
                                           @Qualifier(BeanNameRepository.Custom_Rest_Template) RestTemplate template,
                                           PartnerService partnerService) {
        this.companyRepository = companyRepository;
        this.credentialRepository = credentialRepository;
        this.template = template;
        this.partnerService = partnerService;
    }

    @Override
    public CompanyCredential registerCompanyCredential(RegisterCompanyCredential companyCredential) {
        Company company = companyRepository.findByIdIgnoreCase(companyCredential.getCompanyId()).orElse(null);

        if (company == null) {
            throw new ReckoException("invalid company name specified", HttpStatus.BAD_REQUEST);
        }

        if (!company.getPassword().equals(companyCredential.getCompanyPassword())) {
            throw new ReckoException("invalid password specified", HttpStatus.UNAUTHORIZED);
        }

        if (credentialRepository.existsByApplicationId(companyCredential.getApplicationId())) {
            throw new ReckoException("credential already registered", HttpStatus.BAD_REQUEST);
        }

        String url = "https://identity.xero.com/connect/token";

        String identity = companyCredential.getRemoteAccountId() + ":" + companyCredential.getRemoteAccountSecret();
        String encodedIdentity = Base64.getEncoder().encodeToString(identity.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedIdentity);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("refresh_token", companyCredential.getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Token> responseEntity = template.postForEntity(url, requestEntity, Token.class);

        Token newToken = responseEntity.getBody();
        if (newToken == null) {
            throw new ReckoException("error occurred fetching accounts", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        CompanyCredential credential = new CompanyCredential();

        credential.setAccessToken(newToken.getAccessToken());
        credential.setRefreshToken(newToken.getRefreshToken());

        credential.setApplicationId(remoteFetchTenant(newToken.getAccessToken()));
        credential.setScope(companyCredential.getScope().trim());

        credential.setClientId(companyCredential.getRemoteAccountId().trim());
        credential.setClientSecret(companyCredential.getRemoteAccountSecret().trim());

        credential.setEmail(companyCredential.getEmail().trim());
        credential.setPassword(companyCredential.getPassword().trim());

        credential.setCompany(company);
        credential.setPartner(partnerService.getPartner(PartnerNameRepository.XERO));

        credential.setLastAccess(LocalDateTime.now());

        return credentialRepository.saveAndFlush(credential);
    }

    private String remoteFetchTenant(String accessToken) {
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
}
