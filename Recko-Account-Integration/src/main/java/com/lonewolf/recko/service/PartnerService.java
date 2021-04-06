package com.lonewolf.recko.service;

import com.lonewolf.recko.entity.Partner;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.repository.PartnerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PartnerService {

    private final PartnerRepository repository;

    @Autowired
    public PartnerService(PartnerRepository repository) {
        this.repository = repository;
    }

    public List<Partner> getPartners() {
        return repository.findActivePartners();
    }

    public Partner getPartner(PartnerNameRepository nameRepository) {
        Partner partner = repository.findById(nameRepository.getName()).orElse(null);

        if (partner == null) {
            throw new ReckoException("service not found", HttpStatus.NOT_FOUND);
        }

        if (!partner.isActive()) {
            throw new ReckoException("service not active", HttpStatus.BAD_REQUEST);
        }

        return partner;
    }
}
