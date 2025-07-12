package com.guilhermescherer.msservicewatch.data;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatusCode;

@Builder(setterPrefix = "with")
@Data
public class ResponseData {

    private HttpStatusCode httpStatusCode;
    private Long responseTimeMillis;
}
