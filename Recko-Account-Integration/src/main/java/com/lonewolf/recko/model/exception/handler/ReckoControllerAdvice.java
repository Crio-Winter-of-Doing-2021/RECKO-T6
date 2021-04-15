package com.lonewolf.recko.model.exception.handler;


import com.lonewolf.recko.model.exception.ReckoException;
import com.lonewolf.recko.util.StringExtensions;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class ReckoControllerAdvice {

    private static final String Message = "message";

    @ExceptionHandler({ReckoException.class})
    public ResponseEntity<Map<String, String>> handleReckoException(ReckoException exception) {
        Map<String, String> responseBody = new HashMap<>();
        responseBody.put(Message, StringExtensions.capitalize(exception.getMessage()));

        return new ResponseEntity<>(responseBody, exception.getErrorStatus());
    }
}
