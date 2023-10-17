package br.com.smashcode.smashmanagements.config;

import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;

@Configuration
public class LocaleConfig {
    @Bean
    public MessageSource messageSource() {
        var source = new ResourceBundleMessageSource();
        source.addBasenames("lang/messages");
        source.setDefaultEncoding("UTF-8");
        return source;
    }
}
