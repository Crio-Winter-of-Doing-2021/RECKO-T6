package com.lonewolf.recko.controller;

import com.lonewolf.recko.entity.Transaction;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.service.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/transactions")
@CrossOrigin
public class TransactionController {

    private final TransactionService service;

    @Autowired
    public TransactionController(TransactionService service) {
        this.service = service;
    }

    @GetMapping("/{id}")
    public List<Transaction> controlGetTransactions(@PathVariable("id") String companyId) {
        return service.getTransactions(companyId);
    }

    @GetMapping("/{id}/{partner}")
    public List<Transaction> controlGetPartnerTransactions(@PathVariable("partner") PartnerNameRepository nameRepository,
                                                           @PathVariable("id") String companyId) {
        return service.getPartnerTransactions(nameRepository, companyId);
    }
}
