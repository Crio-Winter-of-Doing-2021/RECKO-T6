package com.lonewolf.recko.service.factory;

import com.lonewolf.recko.config.BeanNameRepository;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.service.PartnerService;
import com.lonewolf.recko.service.factory.host.HostConsumerContract;
import com.lonewolf.recko.service.factory.host.HostTransactionContract;
import com.lonewolf.recko.service.factory.remote.RemoteCredentialRegisterContract;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;

@Repository
public class PartnerResolver {

//    Consumer Service Beans

    @Autowired
    @Qualifier(BeanNameRepository.Xero_Host_Consumer)
    private HostConsumerContract xeroService;

    @Autowired
    @Qualifier(BeanNameRepository.Quickbooks_Host_Consumer)
    private HostConsumerContract quickbooksService;


//    Transaction Service Beans

    @Autowired
    @Qualifier(BeanNameRepository.Xero_Host_Transaction)
    private HostTransactionContract xeroTransactionService;

    @Autowired
    @Qualifier(BeanNameRepository.Quickbooks_Host_Transaction)
    private HostTransactionContract quickbooksTransactionService;

//    Credential Registration Services

    @Autowired
    @Qualifier(BeanNameRepository.Xero_Credential_Register)
    private RemoteCredentialRegisterContract xeroRegisterContract;

    @Autowired
    @Qualifier(BeanNameRepository.Quickbooks_Credential_Register)
    private RemoteCredentialRegisterContract quickbooksRegisterContract;

    //    Partner Service
    @Autowired
    private PartnerService partnerService;

    public HostConsumerContract resolveConsumer(PartnerNameRepository nameRepository) {
        if (partnerService.getPartner(nameRepository) == null) {
            throw new ReckoException("service not found", HttpStatus.BAD_REQUEST);
        }

        switch (nameRepository) {
            case XERO:
                return xeroService;
            case QUICKBOOKS:
                return quickbooksService;
            default:
                throw new ReckoException("recko doesn't have no such partner", HttpStatus.BAD_REQUEST);
        }
    }

    public HostTransactionContract resolveTransaction(PartnerNameRepository nameRepository) {
        if (partnerService.getPartner(nameRepository) == null) {
            throw new ReckoException("service not found", HttpStatus.BAD_REQUEST);
        }

        switch (nameRepository) {
            case XERO:
                return xeroTransactionService;
            case QUICKBOOKS:
                return quickbooksTransactionService;
            default:
                throw new ReckoException("recko doesn't have such partner", HttpStatus.BAD_REQUEST);
        }
    }

    public RemoteCredentialRegisterContract resolveCredentialRegisterer(PartnerNameRepository nameRepository) {
        if (partnerService.getPartner(nameRepository) == null) {
            throw new ReckoException("service not found", HttpStatus.BAD_REQUEST);
        }

        switch (nameRepository) {
            case XERO:
                return xeroRegisterContract;
            case QUICKBOOKS:
                return quickbooksRegisterContract;
            default:
                throw new ReckoException("recko doesn't have such partner", HttpStatus.BAD_REQUEST);
        }
    }
}