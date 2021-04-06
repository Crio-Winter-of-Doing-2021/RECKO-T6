package com.lonewolf.recko.service.xero.host;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.Consumer;
import com.lonewolf.recko.entity.PartnerCredential;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.model.xero.consumer.AccountType;
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

@Service(BeanNameRepository.Xero_Host_Consumer)
public class HostConsumerService implements HostConsumerContract {

    private static final String Partner_Name = PartnerNameRepository.XERO.getName();

    private final PartnerCredentialRepository credentialRepository;
    private final ConsumerRepository consumerRepository;
    private final RemoteConsumerContract remoteService;

    @Autowired
    public HostConsumerService(PartnerCredentialRepository credentialRepository,
                               ConsumerRepository consumerRepository,
                               @Qualifier(BeanNameRepository.Xero_Remote_Consumer) RemoteConsumerContract remoteService) {
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
            throw new ReckoException("invalid service credentials specified", HttpStatus.UNAUTHORIZED);
        }

        List<Consumer> consumers = credential.getConsumers();
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
                .getCredential(Partner_Name, consumer.getCredential().getEmail(), consumer.getCredential().getPassword())
                .orElse(null);

        if (credential == null) {
            throw new ReckoException("invalid service credentials specified", HttpStatus.UNAUTHORIZED);
        }

        Consumer newConsumer = remoteService.addConsumer(credential, consumer);
        newConsumer.setUpdateCount("0");
        return consumerRepository.saveAndFlush(newConsumer);
    }

    @Override
    public Consumer updateConsumer(Consumer consumer) {
        PartnerCredential credential = credentialRepository.getCredential(
                Partner_Name, consumer.getCredential().getEmail(), consumer.getCredential().getPassword())
                .orElse(null);
        if (credential == null) {
            throw new ReckoException("invalid service credentials specified", HttpStatus.UNAUTHORIZED);
        }

        List<Consumer> consumers = credential.getConsumers()
                .stream()
                .filter(con -> con.getConsumerId().equals(consumer.getConsumerId()))
                .collect(Collectors.toUnmodifiableList());
        if (consumers.isEmpty()) {
            throw new ReckoException("invalid consumer specified", HttpStatus.NOT_FOUND);
        }

        Consumer consumerToUpdate = consumers.get(0);

        if (consumerToUpdate.getType().equalsIgnoreCase(AccountType.Bank.getName())) {
            throw new ReckoException("bank accounts cannot be updated", HttpStatus.BAD_REQUEST);
        }

        if (consumer.getType() != null && !consumerToUpdate.getType().equalsIgnoreCase(consumer.getType())) {
            throw new ReckoException("account type cannot be changed", HttpStatus.BAD_REQUEST);
        }

        consumerToUpdate.setName(consumer.getName());
        Consumer updatedConsumer = remoteService.updateConsumer(credential, consumerToUpdate);
        updatedConsumer.setUpdateCount(String.valueOf(Integer.parseInt(consumerToUpdate.getUpdateCount()) + 1));

        return consumerRepository.saveAndFlush(updatedConsumer);
    }

    @Override
    public boolean deleteConsumer(Consumer consumer) {
        PartnerCredential credential = credentialRepository.getCredential(
                Partner_Name, consumer.getCredential().getEmail(), consumer.getCredential().getPassword())
                .orElse(null);
        if (credential == null) {
            throw new ReckoException("invalid service credentials specified", HttpStatus.UNAUTHORIZED);
        }

        List<Consumer> consumers = credential.getConsumers()
                .stream()
                .filter(con -> con.getConsumerId().equals(consumer.getConsumerId()))
                .collect(Collectors.toUnmodifiableList());
        if (consumers.isEmpty()) {
            throw new ReckoException("invalid consumer specified", HttpStatus.NOT_FOUND);
        }

        Consumer consumerToDelete = consumers.get(0);

        if (consumerToDelete.getType().equalsIgnoreCase(AccountType.Bank.getName())) {
            throw new ReckoException("bank accounts cannot be deleted", HttpStatus.BAD_REQUEST);
        }

        Consumer deletedConsumer = remoteService.deleteConsumer(credential, consumerToDelete);
        deletedConsumer.setUpdateCount(String.valueOf(Integer.parseInt(consumerToDelete.getUpdateCount()) + 1));

        consumerRepository.saveAndFlush(deletedConsumer);
        return true;
    }

    @Override
    public List<String> getAccountTypes() {
        return Arrays.stream(AccountType.values()).map(AccountType::getName).collect(Collectors.toUnmodifiableList());
    }
}
