package com.lonewolf.recko.service.factory.host;

import com.lonewolf.recko.entity.Transaction;

import java.util.List;

public interface HostTransactionContract {

    List<Transaction> getPartnerTransactions();
}