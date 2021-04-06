package com.lonewolf.recko.util;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;

public class ResponseGenerator {

    private static final String Message = "message";

    private ResponseGenerator() {
    }

    public static ResponseEntity<Map<String, String>> generateResponse(String message, HttpStatus status, boolean noCap) {
        Map<String, String> body = new HashMap<>();
        body.put(Message, noCap ? message : StringExtensions.capitalize(message));
        return new ResponseEntity<>(body, status);
    }
}
