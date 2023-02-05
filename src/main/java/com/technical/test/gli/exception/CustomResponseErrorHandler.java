package com.technical.test.gli.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;
import java.net.NoRouteToHostException;

public class CustomResponseErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
                || response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR;
    }

    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        if (response.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR) {
            throw new ResourceAccessException("Server error");
        } else if (response.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
            if (response.getStatusCode() == HttpStatus.NOT_FOUND) {
                throw new NoRouteToHostException("No route to host");
            }
        }
    }
}

