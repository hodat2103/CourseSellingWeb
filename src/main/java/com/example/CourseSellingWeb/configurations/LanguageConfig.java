package com.example.CourseSellingWeb.configurations;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.web.servlet.LocaleResolver;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;

import java.util.Locale;

@Configuration
public class LanguageConfig {
    @Bean(name = "messageSource")
    public MessageSource messageSource(){
        ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
        messageSource.setBasename("i18n.messages");
        messageSource.setDefaultLocale(Locale.US);
        messageSource.setUseCodeAsDefaultMessage(true);

        messageSource.setDefaultEncoding("UTF-8");
        return messageSource;
    }


}
