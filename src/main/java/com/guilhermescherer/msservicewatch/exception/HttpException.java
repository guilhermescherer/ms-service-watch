package com.guilhermescherer.msservicewatch.exception;

import com.guilhermescherer.msservicewatch.enums.ErrorCatalog;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class HttpException extends RuntimeException {

    private final HttpStatus status;
    private final ErrorCatalog error;

    public HttpException(ErrorCatalog error) {
        super(error.getMessage());
        this.status = error.getStatus();
        this.error = error;
    }
}
