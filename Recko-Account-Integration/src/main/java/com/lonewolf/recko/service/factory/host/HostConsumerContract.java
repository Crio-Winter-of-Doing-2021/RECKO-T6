package com.lonewolf.recko.service.factory.host;

import com.lonewolf.recko.entity.Consumer;

import java.util.List;

public interface HostConsumerContract {

    List<Consumer> getPartnerConsumers();

    List<Consumer> getPartnerHandlerConsumers(String email);

    Consumer addConsumer(Consumer consumer);

    Consumer updateConsumer(Consumer consumer);

    boolean deleteConsumer(Consumer consumer);

    List<String> getAccountTypes();
}
