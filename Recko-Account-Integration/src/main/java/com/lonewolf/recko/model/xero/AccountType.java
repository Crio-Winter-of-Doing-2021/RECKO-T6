package com.lonewolf.recko.model.xero;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lonewolf.recko.model.exception.ReckoException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AccountType {

    Bank("BANK"),
    Revenue("REVENUE"),
    Sales("SALES"),
    Direct_Costs("DIRECTCOSTS"),
    Expense("EXPENSE"),
    Current("CURRENT"),
    Inventory("INVENTORY"),
    Fixed("FIXED"),
    Current_Liability("CURRLIAB"),
    Term_Liability("TERMLIAB"),
    Equity("EQUITY");

    @JsonValue
    private final String name;

    public static AccountType parseType(String source) {
        for (AccountType type : values()) {
            if (type.getName().equalsIgnoreCase(source)) {
                return type;
            }
        }
        throw new ReckoException("invalid account type specified", HttpStatus.BAD_REQUEST);
    }
}
