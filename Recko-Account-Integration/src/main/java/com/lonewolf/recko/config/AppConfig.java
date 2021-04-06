package com.lonewolf.recko.config;

import com.lonewolf.recko.config.converter.PartnerConverter;
import com.lonewolf.recko.model.exception.handler.RestTemplateExceptionHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class AppConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {
        registry.addConverter(new PartnerConverter());
    }

    @Bean(name = BeanNameRepository.Custom_Rest_Template)
    public org.springframework.web.client.RestTemplate buildCustomRestTemplate() {
        return new RestTemplateBuilder().errorHandler(new RestTemplateExceptionHandler()).build();
    }
}
