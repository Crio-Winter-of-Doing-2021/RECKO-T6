package com.lonewolf.recko.service;

import com.lonewolf.recko.entity.Consumer;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.repository.ConsumerRepository;
import com.lonewolf.recko.service.factory.PartnerResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConsumerService {

    private final PartnerResolver partnerFactory;
    private final ConsumerRepository consumerRepository;

    @Autowired
    public ConsumerService(PartnerResolver partnerFactory,
                           ConsumerRepository consumerRepository) {
        this.partnerFactory = partnerFactory;
        this.consumerRepository = consumerRepository;
    }

    public List<Consumer> getConsumers() {
        List<Consumer> consumers = consumerRepository.findActiveConsumers();
        if (consumers.isEmpty()) {
            for (PartnerNameRepository nameRepository : PartnerNameRepository.values()) {
                consumers.addAll(partnerFactory.resolveConsumer(nameRepository).getPartnerConsumers());
            }
        }
        return consumers;
    }

    public List<Consumer> getPartnerConsumers(PartnerNameRepository nameRepository) {
        List<Consumer> consumers = consumerRepository.findPartnerConsumers(nameRepository.getName());
        if (consumers.isEmpty()) {
            return partnerFactory.resolveConsumer(nameRepository).getPartnerConsumers();
        }
        return consumers;
    }

    public List<Consumer> getPartnerHandlerConsumers(PartnerNameRepository nameRepository, String email) {
        List<Consumer> consumers = consumerRepository.findPartnerHandlerConsumers(nameRepository.getName(), email);
        if (consumers.isEmpty()) {
            return partnerFactory.resolveConsumer(nameRepository).getPartnerHandlerConsumers(email);
        }
        return consumers;
    }

    public Consumer addConsumer(Consumer consumer) {
        PartnerNameRepository nameRepository =
                PartnerNameRepository.parsePartner(consumer.getCredential().getPartner().getName());
        return partnerFactory.resolveConsumer(nameRepository).addConsumer(consumer);
    }

    public Consumer updateConsumer(Consumer consumer) {
        PartnerNameRepository nameRepository =
                PartnerNameRepository.parsePartner(consumer.getCredential().getPartner().getName());
        return partnerFactory.resolveConsumer(nameRepository).updateConsumer(consumer);
    }

    public boolean deleteConsumer(Consumer consumer) {
        PartnerNameRepository nameRepository =
                PartnerNameRepository.parsePartner(consumer.getCredential().getPartner().getName());
        return partnerFactory.resolveConsumer(nameRepository).deleteConsumer(consumer);
    }

    public List<String> getAccountTypes(PartnerNameRepository nameRepository) {
        return partnerFactory.resolveConsumer(nameRepository).getAccountTypes();
    }
}