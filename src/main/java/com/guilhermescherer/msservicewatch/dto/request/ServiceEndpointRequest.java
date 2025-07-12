package com.guilhermescherer.msservicewatch.dto.request;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ServiceEndpointRequest {

    @NotBlank(message = "Name must not be blank")
    @Size(max = 255, message = "Name must be at most 255 characters")
    private String name;
    @NotBlank(message = "URL must not be blank")
    @Size(max = 2048, message = "URL must be at most 2048 characters")
    @Pattern(regexp = "^(http|https)://.*$", message = "URL must start with http:// or https://")
    private String url;
    @NotNull(message = "Active must not be null")
    private Boolean active;
    @NotNull(message = "Check interval must not be null")
    @Min(value = 1, message = "Check interval must be at least 1 minute")
    @Max(value = 180, message = "Check interval must not exceed 180 minutes")
    private Integer checkInterval;
}
