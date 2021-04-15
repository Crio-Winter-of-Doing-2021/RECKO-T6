package com.lonewolf.recko.controller;

import com.lonewolf.recko.entity.Partner;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.service.PartnerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/partners")
@CrossOrigin
public class PartnerController {

    private final PartnerService service;

    @Autowired
    public PartnerController(PartnerService service) {
        this.service = service;
    }

    @GetMapping
    public List<Partner> controlGetPartners() {
        return service.getPartners();
    }

    @GetMapping("/{partner}")
    public Partner controlGetPartner(@PathVariable("partner") PartnerNameRepository nameRepository) {
        return service.getPartner(nameRepository);
    }
}
