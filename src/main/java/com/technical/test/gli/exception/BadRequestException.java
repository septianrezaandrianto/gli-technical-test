package com.technical.test.gli.exception;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BadRequestException extends RuntimeException {
    private Integer responseCode;
    private String responseMessage;

    public BadRequestException(String message) {
        super(message);
    }
}
