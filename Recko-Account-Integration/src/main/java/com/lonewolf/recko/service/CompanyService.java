package com.lonewolf.recko.service;

import com.lonewolf.recko.entity.Admin;
import com.lonewolf.recko.entity.Company;
import com.lonewolf.recko.entity.CompanyCredential;
import com.lonewolf.recko.entity.Moderator;
import com.lonewolf.recko.model.CompanyHandlerRole;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.model.request.RegisterCompanyCredential;
import com.lonewolf.recko.model.request.RegisterCompanyHandler;
import com.lonewolf.recko.repository.AdminRepository;
import com.lonewolf.recko.repository.CompanyRepository;
import com.lonewolf.recko.repository.ModeratorRepository;
import com.lonewolf.recko.service.factory.PartnerResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CompanyService {

    private final CompanyRepository companyRepository;
    private final AdminRepository adminRepository;
    private final ModeratorRepository moderatorRepository;
    private final PartnerResolver partnerResolver;

    @Autowired
    public CompanyService(CompanyRepository companyRepository,
                          AdminRepository adminRepository,
                          ModeratorRepository moderatorRepository,
                          PartnerResolver partnerResolver) {
        this.companyRepository = companyRepository;
        this.adminRepository = adminRepository;
        this.moderatorRepository = moderatorRepository;
        this.partnerResolver = partnerResolver;
    }

    public Company registerNewCompany(Company company) {
        if (companyRepository.existsByIdIgnoreCase(company.getId())) {
            throw new ReckoException("company with same name already exits", HttpStatus.BAD_REQUEST);
        }

        Company newCompany = new Company();
        newCompany.setId(company.getId());
        newCompany.setName(company.getName());
        newCompany.setPassword(company.getPassword());

        return companyRepository.saveAndFlush(newCompany);
    }

    public Admin registerCompanyAdmin(RegisterCompanyHandler handler) {
        Company company = companyRepository.findByIdIgnoreCase(handler.getCompanyId()).orElse(null);
        if (company == null) {
            throw new ReckoException("invalid company name specified", HttpStatus.BAD_REQUEST);
        }

        if (!company.getPassword().equals(handler.getCompanyPassword())) {
            throw new ReckoException("wrong password specified", HttpStatus.UNAUTHORIZED);
        }

        Admin existingAdmin = adminRepository.findByCompany(company.getId(), handler.getHandlerName()).orElse(null);
        if (existingAdmin != null) {
            throw new ReckoException("admin with same name already exists", HttpStatus.BAD_REQUEST);
        }

        Admin newAdmin = new Admin();

        newAdmin.setAdminName(handler.getHandlerName().toLowerCase());
        newAdmin.setEmail(handler.getHandlerEmail().toLowerCase());
        newAdmin.setPassword(handler.getHandlerPassword());
        newAdmin.setCompany(company);

        return adminRepository.saveAndFlush(newAdmin);
    }

    public Moderator registerCompanyModerator(RegisterCompanyHandler handler) {
        Company company = companyRepository.findByIdIgnoreCase(handler.getCompanyId()).orElse(null);
        if (company == null) {
            throw new ReckoException("invalid company name specified", HttpStatus.BAD_REQUEST);
        }

        if (!company.getPassword().equals(handler.getCompanyPassword())) {
            throw new ReckoException("wrong password specified", HttpStatus.UNAUTHORIZED);
        }

        Moderator existingModerator = moderatorRepository.findByCompany(company.getId(), handler.getHandlerName()).orElse(null);
        if (existingModerator != null) {
            throw new ReckoException("moderator with same name already exists", HttpStatus.BAD_REQUEST);
        }

        Moderator newModerator = new Moderator();

        newModerator.setModeratorName(handler.getHandlerName().toLowerCase());
        newModerator.setEmail(handler.getHandlerEmail().toLowerCase());
        newModerator.setPassword(handler.getHandlerPassword());
        newModerator.setCompany(company);

        return moderatorRepository.saveAndFlush(newModerator);
    }

    public List<String> getCompanyHandleRoles() {
        return Arrays.stream(CompanyHandlerRole.values())
                .map(CompanyHandlerRole::getName)
                .collect(Collectors.toUnmodifiableList());
    }

    public CompanyCredential registerCompanyCredential(PartnerNameRepository nameRepository,
                                                       RegisterCompanyCredential companyCredential) {
        return partnerResolver.resolveCredentialRegisterer(nameRepository).registerCompanyCredential(companyCredential);
    }
}
