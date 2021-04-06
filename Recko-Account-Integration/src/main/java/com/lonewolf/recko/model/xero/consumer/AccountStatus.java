package com.lonewolf.recko.model.xero.consumer;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lonewolf.recko.model.exception.ReckoException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AccountStatus {
    Active("ACTIVE"), Archived("ARCHIVED"), Deleted("DELETED");

    @JsonValue
    private final String name;

    public AccountStatus parseStatus(String source) {
        for (AccountStatus status : values()) {
            if (status.getName().equalsIgnoreCase(source)) {
                return status;
            }
        }

        throw new ReckoException("invalid account status specified", HttpStatus.BAD_REQUEST);
    }
}
