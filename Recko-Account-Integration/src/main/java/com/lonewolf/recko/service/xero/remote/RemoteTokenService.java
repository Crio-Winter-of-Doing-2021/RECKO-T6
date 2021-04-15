package com.lonewolf.recko.service.xero.remote;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.CompanyCredential;
import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.model.xero.Token;
import com.lonewolf.recko.repository.CompanyCredentialRepository;
import com.lonewolf.recko.service.factory.remote.RemoteTokenContract;
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

@Service(BeanNameRepository.Xero_Remote_Token)
public class RemoteTokenService implements RemoteTokenContract {

    private static final long Access_Token_Validity = 1800;

    private final RestTemplate template;
    private final CompanyCredentialRepository credentialRepository;

    public RemoteTokenService(@Qualifier(BeanNameRepository.Custom_Rest_Template) RestTemplate template,
                              CompanyCredentialRepository credentialRepository) {
        this.template = template;
        this.credentialRepository = credentialRepository;
    }

    @Override
    public void refreshToken(CompanyCredential credential) {
        LocalDateTime lastAccess = credential.getLastAccess();
        if (lastAccess != null && lastAccess.until(LocalDateTime.now(), ChronoUnit.SECONDS) <= Access_Token_Validity) {
            return;
        }

        String url = "https://identity.xero.com/connect/token";

        String identity = credential.getClientId() + ":" + credential.getClientSecret();
        String encodedIdentity = Base64.getEncoder().encodeToString(identity.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Basic " + encodedIdentity);
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        requestBody.add("grant_type", "refresh_token");
        requestBody.add("refresh_token", credential.getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<Token> responseEntity = template.postForEntity(url, requestEntity, Token.class);

        Token newToken = responseEntity.getBody();
        if (newToken == null) {
            throw new ReckoException("error occurred fetching accounts", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        credential.setAccessToken(newToken.getAccessToken());
        credential.setRefreshToken(newToken.getRefreshToken());
        credential.setScope(newToken.getScope());
        credential.setLastAccess(LocalDateTime.now());

        credentialRepository.saveAndFlush(credential);
    }
}
