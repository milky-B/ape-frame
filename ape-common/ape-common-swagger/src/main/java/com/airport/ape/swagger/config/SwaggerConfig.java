package com.airport.ape.swagger.config;

import com.airport.ape.swagger.entity.SwaggerInfo;
import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Autowired
    private SwaggerInfo swaggerInfo;
    @Bean
    public Docket createApi(){
        return new Docket(DocumentationType.SWAGGER_2)//指定版本
                .apiInfo(apiInfo())
                .select()
                .apis(RequestHandlerSelectors.basePackage(swaggerInfo.getBasePackage()))//选择所有请求处理器controller
                .paths(Predicates.not(PathSelectors.regex("/error.*")))//选择处理路径
                .build();
    }
    private ApiInfo apiInfo(){
        return new ApiInfoBuilder()
                .title(swaggerInfo.getTitle())
                .description(swaggerInfo.getDescription())
                .contact(new Contact(swaggerInfo.getContactName(),swaggerInfo.getContactUrl(),swaggerInfo.getContactEmail()))
                .version(swaggerInfo.getVersion())
                .build();
    }
}
