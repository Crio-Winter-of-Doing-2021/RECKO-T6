package com.lonewolf.recko.service.factory.remote;

import com.lonewolf.recko.entity.Consumer;
import com.lonewolf.recko.entity.PartnerCredential;

import java.util.List;

public interface RemoteConsumerContract {

    List<Consumer> fetchConsumers(PartnerCredential credential);

    Consumer addConsumer(PartnerCredential credential, Consumer consumer);

    Consumer updateConsumer(PartnerCredential credential, Consumer consumer);

    Consumer deleteConsumer(PartnerCredential credential, Consumer consumer);
}