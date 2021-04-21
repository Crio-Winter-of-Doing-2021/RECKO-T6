package com.lonewolf.recko.config;

import com.lonewolf.recko.model.CompanyHandlerRole;
import com.lonewolf.recko.model.PartnerNameRepository;
import com.lonewolf.recko.model.exception.handler.RestTemplateErrorHandler;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.format.FormatterRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class ReckoApplicationConfig implements WebMvcConfigurer {

    @Override
    public void addFormatters(FormatterRegistry registry) {

        registry.addConverter(new Converter<String, PartnerNameRepository>() {
            @Override
            public PartnerNameRepository convert(@NonNull String source) {
                return PartnerNameRepository.parsePartner(source);
            }
        });

        registry.addConverter(new Converter<String, CompanyHandlerRole>() {
            @Override
            public CompanyHandlerRole convert(@NonNull String source) {
                return CompanyHandlerRole.parseHandlerRole(source);
            }
        });
    }

    @Bean(name = BeanNameRepository.Custom_Rest_Template)
    public org.springframework.web.client.RestTemplate buildCustomRestTemplate() {
        return new RestTemplateBuilder().errorHandler(new RestTemplateErrorHandler()).build();
    }

    @Bean(BeanNameRepository.Mail_Sender_Email_Address)
    public String mailSenderEmailAddress(@Value("${spring.mail.username}") String senderEmail) {
        return senderEmail;
    }
}
