package com.arthur.Webflux.Framework.Trainning.exception;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Component
public class CustomAttributes extends DefaultErrorAttributes {

    @Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {

        Map<String, Object> errorObjectMap  =  super.getErrorAttributes(webRequest, options);
        Throwable throwable = getError(webRequest);
        if(throwable instanceof ResponseStatusException ex){
            errorObjectMap.put("message", ex.getMessage());
            errorObjectMap.put("developerMessage", "A ResponseStatusException Happened");
        }
        return errorObjectMap;
    }
}
