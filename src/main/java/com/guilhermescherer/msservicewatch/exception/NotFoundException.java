package com.guilhermescherer.msservicewatch.exception;

import com.guilhermescherer.msservicewatch.enums.ErrorCatalog;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends HttpException {

    public NotFoundException() {
        super(ErrorCatalog.NOT_FOUND_ERROR);
    }
}
