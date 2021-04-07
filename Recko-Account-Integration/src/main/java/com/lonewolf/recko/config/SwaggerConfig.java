package com.lonewolf.recko.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Collections;

@Configuration
@EnableSwagger2
public class SwaggerConfig {

    @Bean
    public Docket configureSwagger() {
        return new Docket(DocumentationType.SWAGGER_2)
                .select()
                .paths(PathSelectors.ant("/api/*"))
                .apis(RequestHandlerSelectors.basePackage("com.lonewolf.recko"))
                .build()
                .apiInfo(configureApiInfo());
    }

    private ApiInfo configureApiInfo() {
        String title = "Recko Account Integration Documentation";
        String description = "Documentation of Account Management of Recko";
        String version = "1.0";
        String termsOfServiceUrl = "https://swagger.io/specification/v2/";

        Contact contact = new Contact("Subham Das", "https://crio-profile.netlify.app/", "subhamkumardas98@gmail.com");
        String license = "Free to Use";

        return new ApiInfo(title, description, version, termsOfServiceUrl, contact, license, null, Collections.emptyList());
    }
}