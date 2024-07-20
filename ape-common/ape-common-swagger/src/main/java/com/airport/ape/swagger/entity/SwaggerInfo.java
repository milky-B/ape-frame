package com.airport.ape.swagger.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import springfox.documentation.service.Contact;

@Data
@Component
@ConfigurationProperties("swagger.info")
public class SwaggerInfo {
    private String title;
    private String description;
    private String contactName;
    private String contactUrl;
    private String contactEmail;
    private String version;
    private String basePackage;
}
