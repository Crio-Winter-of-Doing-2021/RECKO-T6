package com.lonewolf.recko.service;

import com.lonewolf.recko.entity.CompanyCredential;
import com.lonewolf.recko.entity.Consumer;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.repository.CompanyCredentialRepository;
import com.lonewolf.recko.service.factory.PartnerResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
public class ConsumerService {

    private final PartnerResolver partnerResolver;
    private final CompanyCredentialRepository credentialRepository;

    @Autowired
    public ConsumerService(PartnerResolver partnerResolver,
                           CompanyCredentialRepository credentialRepository) {
        this.partnerResolver = partnerResolver;
        this.credentialRepository = credentialRepository;
    }

    public List<Consumer> getConsumers(String companyId) {
        List<Consumer> consumers = new ArrayList<>();
        for (PartnerNameRepository nameRepository : PartnerNameRepository.values()) {
            consumers.addAll(getPartnerConsumers(nameRepository, companyId));
        }
        return consumers;
    }

    public List<Consumer> getPartnerConsumers(PartnerNameRepository nameRepository, String companyId) {
        List<CompanyCredential> credentials = credentialRepository.findByPartnerInCompany(nameRepository.getName(), companyId);
        if (credentials.isEmpty()) {
            return Collections.emptyList();
        }

        List<Consumer> consumers = new ArrayList<>();

        for (CompanyCredential credential : credentials) {
            consumers.addAll(partnerResolver.resolveConsumer(nameRepository).getPartnerConsumers(credential));
        }

        return consumers;
    }

    public Consumer addConsumer(Consumer consumer) {
        PartnerNameRepository nameRepository =
                PartnerNameRepository.parsePartner(consumer.getCredential().getPartner().getName());
        return partnerResolver.resolveConsumer(nameRepository).addConsumer(consumer);
    }

    public Consumer updateConsumer(Consumer consumer) {
        PartnerNameRepository nameRepository =
                PartnerNameRepository.parsePartner(consumer.getCredential().getPartner().getName());
        return partnerResolver.resolveConsumer(nameRepository).updateConsumer(consumer);
    }

    public boolean deleteConsumer(Consumer consumer) {
        PartnerNameRepository nameRepository =
                PartnerNameRepository.parsePartner(consumer.getCredential().getPartner().getName());
        return partnerResolver.resolveConsumer(nameRepository).deleteConsumer(consumer);
    }

    public List<String> getAccountTypes(PartnerNameRepository nameRepository) {
        return partnerResolver.resolveConsumer(nameRepository).getAccountTypes();
    }
}