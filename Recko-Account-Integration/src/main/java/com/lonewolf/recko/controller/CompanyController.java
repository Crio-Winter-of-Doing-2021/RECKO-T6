package com.lonewolf.recko.controller;

import com.lonewolf.recko.entity.Admin;
import com.lonewolf.recko.entity.Company;
import com.lonewolf.recko.entity.CompanyCredential;
import com.lonewolf.recko.entity.Moderator;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.model.request.RegisterCompanyCredential;
import com.lonewolf.recko.model.request.RegisterCompanyHandler;
import com.lonewolf.recko.service.CompanyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/companies")
@CrossOrigin
public class CompanyController {

    private final CompanyService companyService;

    @Autowired
    public CompanyController(CompanyService companyService) {
        this.companyService = companyService;
    }

    @PostMapping("/register/company")
    public Company controlRegisterNewCompany(@RequestBody Company company) {
        return companyService.registerNewCompany(company);
    }

    @PostMapping("/register/admin")
    public Admin controlRegisterCompanyAdmin(@RequestBody RegisterCompanyHandler companyHandler) {
        return companyService.registerCompanyAdmin(companyHandler);
    }

    @PostMapping("/register/moderator")
    public Moderator controlRegisterCompanyModerator(@RequestBody RegisterCompanyHandler companyHandler) {
        return companyService.registerCompanyModerator(companyHandler);
    }

    @GetMapping("/roles")
    public List<String> controlCompanyHandlerRoles() {
        return companyService.getCompanyHandleRoles();
    }

    @PostMapping("/register/credential/{partner}")
    public CompanyCredential controlRegisterCompanyCredential(@PathVariable("partner") PartnerNameRepository nameRepository,
                                                              @RequestBody RegisterCompanyCredential companyCredential) {
        return companyService.registerCompanyCredential(nameRepository, companyCredential);
    }
}
