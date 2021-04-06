package com.lonewolf.recko.model.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public class ReckoException extends RuntimeException {

    private final HttpStatus errorStatus;

    public ReckoException(String message, HttpStatus errorStatus) {
        super(message);
        this.errorStatus = errorStatus;
    }
}
