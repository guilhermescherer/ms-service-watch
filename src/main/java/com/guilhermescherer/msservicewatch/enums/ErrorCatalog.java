package com.guilhermescherer.msservicewatch.enums;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCatalog {

    NOT_FOUND_ERROR("Not found entity", HttpStatus.NOT_FOUND, "https://github.com/guilhermescherer/ms-service-watch/wiki/Not-found-error"),
    VALIDATION_ERROR("Validation error", HttpStatus.BAD_REQUEST, "https://github.com/guilhermescherer/ms-service-watch/wiki/Validation-error"),
    GENERAL_ERROR("General error", HttpStatus.INTERNAL_SERVER_ERROR, "https://github.com/guilhermescherer/ms-service-watch/wiki/General-error");

    private final String message;
    private final HttpStatus status;
    private final String url;

    ErrorCatalog(String message, HttpStatus status, String url) {
        this.message = message;
        this.status = status;
        this.url = url;
    }
}
