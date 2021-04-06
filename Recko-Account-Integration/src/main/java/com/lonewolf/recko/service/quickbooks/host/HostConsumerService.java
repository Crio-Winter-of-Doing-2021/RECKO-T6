package com.lonewolf.recko.service.quickbooks.host;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.Consumer;
import com.lonewolf.recko.entity.PartnerCredential;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.model.quickbooks.consumer.AccountType;
import com.lonewolf.recko.repository.ConsumerRepository;
import com.lonewolf.recko.repository.PartnerCredentialRepository;
import com.lonewolf.recko.service.factory.host.HostConsumerContract;
import com.lonewolf.recko.service.factory.remote.RemoteConsumerContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service(BeanNameRepository.Quickbooks_Host_Consumer)
public class HostConsumerService implements HostConsumerContract {

    private static final String Partner_Name = PartnerNameRepository.QUICKBOOKS.getName();

    private final PartnerCredentialRepository credentialRepository;
    private final ConsumerRepository consumerRepository;
    private final RemoteConsumerContract remoteService;

    @Autowired
    public HostConsumerService(PartnerCredentialRepository credentialRepository,
                               ConsumerRepository consumerRepository,
                               @Qualifier(BeanNameRepository.Quickbooks_Remote_Consumer) RemoteConsumerContract remoteService) {
        this.credentialRepository = credentialRepository;
        this.consumerRepository = consumerRepository;
        this.remoteService = remoteService;
    }

    private void addConsumersDatabase(List<Consumer> consumers) {
        for (Consumer consumer : consumers) {
            consumerRepository.saveAndFlush(consumer);
        }
    }

    @Override
    public List<Consumer> getPartnerConsumers() {
        List<PartnerCredential> credentials = credentialRepository.findByPartner(Partner_Name);
        if (credentials.isEmpty()) {
            return Collections.emptyList();
        }

        List<Consumer> consumers = new ArrayList<>();

        for (PartnerCredential credential : credentials) {
            List<Consumer> partnerConsumers = credential.getConsumers();
            if (partnerConsumers.isEmpty()) {
                List<Consumer> remoteConsumers = remoteService.fetchConsumers(credential);
                addConsumersDatabase(remoteConsumers);
                partnerConsumers.addAll(remoteConsumers);
            }
            consumers.addAll(partnerConsumers);
        }
        return consumers;
    }

    @Override
    public List<Consumer> getPartnerHandlerConsumers(String email) {
        PartnerCredential credential = credentialRepository.credentialExists(Partner_Name, email).orElse(null);
        if (credential == null) {
            throw new ReckoException("invalid credentials specified", HttpStatus.UNAUTHORIZED);
        }

        List<Consumer> consumers = consumerRepository.findPartnerHandlerConsumers(Partner_Name, email);
        if (consumers.isEmpty()) {
            List<Consumer> remoteConsumers = remoteService.fetchConsumers(credential);
            addConsumersDatabase(remoteConsumers);
            consumers.addAll(remoteConsumers);
        }
        return consumers;
    }

    @Override
    public Consumer addConsumer(Consumer consumer) {
        PartnerCredential credential = credentialRepository
                .credentialExists(Partner_Name, consumer.getCredential().getEmail())
                .orElse(null);

        if (credential == null) {
            throw new ReckoException("invalid credentials specified", HttpStatus.UNAUTHORIZED);
        }

        Consumer newConsumer = remoteService.addConsumer(credential, consumer);
        return consumerRepository.saveAndFlush(newConsumer);
    }

    @Override
    public Consumer updateConsumer(Consumer consumer) {
        PartnerCredential credential = credentialRepository
                .credentialExists(Partner_Name, consumer.getCredential().getEmail())
                .orElse(null);

        if (credential == null) {
            throw new ReckoException("invalid credentials specified", HttpStatus.UNAUTHORIZED);
        }

        List<Consumer> consumers = credential.getConsumers()
                .stream()
                .filter(acc -> acc.getConsumerId().equals(consumer.getConsumerId()))
                .collect(Collectors.toUnmodifiableList());

        if (consumers.isEmpty()) {
            throw new ReckoException("invalid consumer specified", HttpStatus.NOT_FOUND);
        }

        Consumer consumerToUpdate = consumers.get(0);
        if (consumer.getType() != null && !consumerToUpdate.getType().equalsIgnoreCase(consumer.getType())) {
            throw new ReckoException("account type cannot be changed", HttpStatus.BAD_REQUEST);
        }

        consumerToUpdate.setName(consumer.getName());
        Consumer updatedConsumer = remoteService.updateConsumer(credential, consumerToUpdate);

        return consumerRepository.saveAndFlush(updatedConsumer);
    }

    @Override
    public boolean deleteConsumer(Consumer consumer) {
        PartnerCredential credential = credentialRepository
                .credentialExists(Partner_Name, consumer.getCredential().getEmail())
                .orElse(null);

        if (credential == null) {
            throw new ReckoException("invalid credentials specified", HttpStatus.UNAUTHORIZED);
        }

        List<Consumer> consumers = credential.getConsumers()
                .stream()
                .filter(acc -> acc.getConsumerId().equals(consumer.getConsumerId()))
                .collect(Collectors.toUnmodifiableList());
        if (consumers.isEmpty()) {
            throw new ReckoException("invalid consumer specified", HttpStatus.NOT_FOUND);
        }

        Consumer consumerToDelete = consumers.get(0);

        Consumer deletedConsumer = remoteService.deleteConsumer(credential, consumerToDelete);
        consumerRepository.saveAndFlush(deletedConsumer);
        return true;
    }

    @Override
    public List<String> getAccountTypes() {
        return Arrays.stream(AccountType.values())
                .map(AccountType::getName)
                .collect(Collectors.toUnmodifiableList());
    }
}
