package com.lonewolf.recko.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lonewolf.recko.model.exception.ReckoException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum PartnerNameRepository {

    XERO("xero"), QUICKBOOKS("quickbooks");

    @JsonValue
    private final String name;

    public static PartnerNameRepository parsePartner(String partnerName) {
        for (PartnerNameRepository nameRepository : values()) {
            if (partnerName.equalsIgnoreCase(nameRepository.getName())) {
                return nameRepository;
            }
        }

        throw new ReckoException("invalid service specified", HttpStatus.BAD_REQUEST);
    }
}
