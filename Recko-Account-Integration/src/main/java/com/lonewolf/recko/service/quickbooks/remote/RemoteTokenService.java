package com.lonewolf.recko.service.quickbooks.remote;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.CompanyCredential;
import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.model.quickbooks.Token;
import com.lonewolf.recko.repository.CompanyCredentialRepository;
import com.lonewolf.recko.service.factory.remote.RemoteTokenContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Base64;
import java.util.Collections;

@Service(BeanNameRepository.Quickbooks_Remote_Token)
public class RemoteTokenService implements RemoteTokenContract {

    private static final long Access_Token_validity = 3600;
    private static final long Refresh_Token_Validity = 99;

    private final RestTemplate template;
    private final CompanyCredentialRepository credentialRepository;

    @Autowired
    public RemoteTokenService(@Qualifier(BeanNameRepository.Custom_Rest_Template) RestTemplate template,
                              CompanyCredentialRepository credentialRepository) {
        this.template = template;
        this.credentialRepository = credentialRepository;
    }

    @Override
    public void refreshToken(CompanyCredential credential) {
        LocalDateTime lastAccess = credential.getLastAccess();
        if (lastAccess != null && lastAccess.until(LocalDateTime.now(), ChronoUnit.SECONDS) <= Access_Token_validity) {
            return;
        }

        String url = "https://oauth.platform.intuit.com/oauth2/v1/tokens/bearer";
        String identity = credential.getClientId() + ":" + credential.getClientSecret();
        String encodedIdentity = Base64.getEncoder().encodeToString(identity.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedIdentity);
        headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("refresh_token", credential.getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);
        ResponseEntity<Token> responseEntity = template.postForEntity(url, requestEntity, Token.class);

        Token newToken = responseEntity.getBody();
        if (newToken == null) {
            throw new ReckoException("token couldn't be refreshed", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        credential.setAccessToken(newToken.getAccessToken());
        credential.setRefreshToken(newToken.getRefreshToken());
        credential.setLastAccess(LocalDateTime.now());

        credentialRepository.saveAndFlush(credential);
    }
}
