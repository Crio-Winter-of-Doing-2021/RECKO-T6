package com.lonewolf.recko.model.exception.handler;

import com.lonewolf.recko.model.exception.ReckoException;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class RestTemplateErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                || response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
            throw new ReckoException("error occurred from client side, please try again", HttpStatus.FORBIDDEN);
        } else if (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
            throw new ReckoException("error occurred from remote server side, plase try again", HttpStatus.INTERNAL_SERVER_ERROR);
        }

        throw new ReckoException("unable to connect to third party services services", HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
