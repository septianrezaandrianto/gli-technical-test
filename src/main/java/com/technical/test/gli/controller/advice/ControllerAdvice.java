package com.technical.test.gli.controller.advice;

import com.technical.test.gli.exception.BadRequestException;
import com.technical.test.gli.exception.ConflictException;
import com.technical.test.gli.exception.NotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.TreeMap;

@RestControllerAdvice
public class ControllerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    public Map<String, Object> notFoundExceptionHandler(NotFoundException ex) {
        Map<String, Object> errorMap = new TreeMap<>();
        errorMap.put("responseCode", HttpStatus.NOT_FOUND.value());
        errorMap.put("responseMessage", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    public Map<String, Object> conflictExceptionHandler(ConflictException ex) {
        Map<String, Object> errorMap = new TreeMap<>();
        errorMap.put("responseCode", HttpStatus.CONFLICT.value());
        errorMap.put("responseMessage", ex.getMessage());
        return errorMap;
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BadRequestException.class)
    public Map<String, Object> badRequestHandler(BadRequestException ex) {
        Map<String, Object> errorMap = new TreeMap<>();
        errorMap.put("responseCode", HttpStatus.BAD_REQUEST.value());
        errorMap.put("responseMessage", ex.getMessage());
        return errorMap;
    }
}
