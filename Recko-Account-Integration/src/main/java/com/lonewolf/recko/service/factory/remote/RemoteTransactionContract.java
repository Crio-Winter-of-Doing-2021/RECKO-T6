package com.lonewolf.recko.service.factory.remote;

import com.lonewolf.recko.entity.PartnerCredential;
import com.lonewolf.recko.entity.Transaction;

import java.util.List;

public interface RemoteTransactionContract {

    List<Transaction> getPartnerTransactions(PartnerCredential credential);
}
