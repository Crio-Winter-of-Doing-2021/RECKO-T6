package com.lonewolf.recko.model;

import com.fasterxml.jackson.annotation.JsonValue;
import com.lonewolf.recko.model.exception.ReckoException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@AllArgsConstructor
public enum CompanyHandlerRole {
    Admin("Admin"), Moderator("moderator");

    @JsonValue
    private final String name;

    public static CompanyHandlerRole parseHandlerRole(String source) {
        for (CompanyHandlerRole role : values()) {
            if (role.getName().equalsIgnoreCase(source)) {
                return role;
            }
        }
        throw new ReckoException("invalid role specified", HttpStatus.BAD_REQUEST);
    }
}
