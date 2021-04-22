package com.lonewolf.recko.service.quickbooks.host;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.entity.CompanyCredential;
import com.lonewolf.recko.entity.Consumer;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.model.quickbooks.consumer.AccountType;
import com.lonewolf.recko.repository.CompanyCredentialRepository;
import com.lonewolf.recko.repository.ConsumerRepository;
import com.lonewolf.recko.service.factory.host.HostConsumerContract;
import com.lonewolf.recko.service.factory.remote.RemoteConsumerContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service(BeanNameRepository.Quickbooks_Host_Consumer)
public class HostConsumerService implements HostConsumerContract {

    private static final String Partner_Name = PartnerNameRepository.QUICKBOOKS.getName();

    private final CompanyCredentialRepository credentialRepository;
    private final ConsumerRepository consumerRepository;
    private final RemoteConsumerContract remoteService;

    @Autowired
    public HostConsumerService(CompanyCredentialRepository credentialRepository,
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
    public List<Consumer> getPartnerConsumers(CompanyCredential credential) {
        List<Consumer> consumers = credential.getConsumers();

        if (consumers.isEmpty()) {
            List<Consumer> remoteConsumers = remoteService.fetchConsumers(credential);
            addConsumersDatabase(remoteConsumers);
            consumers.addAll(remoteConsumers);
        }

        return consumers.stream()
                .filter(Consumer::isActive)
                .collect(Collectors.toUnmodifiableList());
    }

    @Override
    public Consumer addConsumer(Consumer consumer) {
        CompanyCredential credential = credentialRepository
                .getCredential(
                        consumer.getCredential().getId(),
                        Partner_Name,
                        consumer.getCredential().getCompany().getId(),
                        consumer.getCredential().getEmail(),
                        consumer.getCredential().getPassword())
                .orElse(null);

        if (credential == null) {
            throw new ReckoException("invalid credentials specified", HttpStatus.UNAUTHORIZED);
        }

        Consumer newConsumer = remoteService.addConsumer(credential, consumer);
        return consumerRepository.saveAndFlush(newConsumer);
    }

    @Override
    public Consumer updateConsumer(Consumer consumer) {
        CompanyCredential credential = credentialRepository
                .getCredential(
                        consumer.getCredential().getId(),
                        Partner_Name,
                        consumer.getCredential().getCompany().getId(),
                        consumer.getCredential().getEmail(),
                        consumer.getCredential().getPassword())
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
            throw new ReckoException("consumer type cannot be changed", HttpStatus.BAD_REQUEST);
        }

        consumerToUpdate.setName(consumer.getName());
        Consumer updatedConsumer = remoteService.updateConsumer(credential, consumerToUpdate);

        if (!updatedConsumer.getConsumerId().equals(consumerToUpdate.getConsumerId())) {
            throw new ReckoException("account couldn't be updated", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        consumerToUpdate.setName(updatedConsumer.getName());
        consumerToUpdate.setType(updatedConsumer.getType());
        consumerToUpdate.setUpdateCount(updatedConsumer.getUpdateCount());

        return consumerRepository.saveAndFlush(consumerToUpdate);
    }

    @Override
    public boolean deleteConsumer(Consumer consumer) {
        CompanyCredential credential = credentialRepository
                .getCredential(
                        consumer.getCredential().getId(),
                        Partner_Name,
                        consumer.getCredential().getCompany().getId(),
                        consumer.getCredential().getEmail(),
                        consumer.getCredential().getPassword())
                .orElse(null);

        if (credential == null) {
            throw new ReckoException("invalid credentials specified", HttpStatus.UNAUTHORIZED);
        }

        List<Consumer> consumers = credential.getConsumers()
                .stream()
                .filter(acc -> acc.getConsumerId().equals(consumer.getConsumerId()))
                .collect(Collectors.toUnmodifiableList());
        if (consumers.isEmpty()) {
            throw new ReckoException("invalid account specified", HttpStatus.NOT_FOUND);
        }

        Consumer consumerToDelete = consumers.get(0);
        Consumer deletedConsumer = remoteService.deleteConsumer(credential, consumerToDelete);

        if (!consumerToDelete.getConsumerId().equals(deletedConsumer.getConsumerId())) {
            throw new ReckoException("account couldn't be deleted", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        consumerToDelete.setActive(false);
        consumerToDelete.setUpdateCount(deletedConsumer.getUpdateCount());

        consumerRepository.saveAndFlush(consumerToDelete);
        return true;
    }

    @Override
    public List<String> getAccountTypes() {
        return Arrays.stream(AccountType.values())
                .map(AccountType::getName)
                .collect(Collectors.toUnmodifiableList());
    }
}
