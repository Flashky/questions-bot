package com.xio91.bots.exception;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import feign.FeignException;

@RestControllerAdvice
public class FeignExceptionHandler {

    @ExceptionHandler(FeignException.Unauthorized.class)  
    public Map<String, Object> handleFeignStatusException(FeignException e, HttpServletResponse response) {
        response.setStatus(e.status());
        return null; 
    }

}
