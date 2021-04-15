package com.lonewolf.recko.service.factory.remote;

import com.lonewolf.recko.entity.CompanyCredential;
import com.lonewolf.recko.entity.Consumer;

import java.util.List;

public interface RemoteConsumerContract {

    List<Consumer> fetchConsumers(CompanyCredential credential);

    Consumer addConsumer(CompanyCredential credential, Consumer consumer);

    Consumer updateConsumer(CompanyCredential credential, Consumer consumer);

    Consumer deleteConsumer(CompanyCredential credential, Consumer consumer);
}