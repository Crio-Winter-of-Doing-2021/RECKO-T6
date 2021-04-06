package com.lonewolf.recko.model.quickbooks.consumer;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lonewolf.recko.model.exception.ReckoException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum AccountType {

    Credit("Accounts Payable"),
    Debit("Accounts Receivable"),
    Expense("Expense"),
    Other_Current_Liability("Other Current Liability"),
    Income("Income"),
    Bank("Bank"),
    Cost_Of_Goods_Sold("Cost of Goods Sold"),
    Other_Expense("Other Expense"),
    Other_Income("Other Income"),
    Other_Current_Asset("Other Current Asset"),
    Credit_Card("Credit Card"),
    Equity("Equity"),
    Fixed_Asset("Fixed Asset"),
    Long_Term_Liability("Long Term Liability");


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
