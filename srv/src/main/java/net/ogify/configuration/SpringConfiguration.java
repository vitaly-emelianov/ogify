package net.ogify.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

/**
 * Spring Configuration file
 */
@Configuration
@ComponentScan(basePackages = {"net.ogify.engine", "net.ogify.rest.resources", "net.ogify.database"})
@PropertySource("classpath:ogify.properties")
public class SpringConfiguration {
    @Bean
    public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
        return new PropertySourcesPlaceholderConfigurer();
    }
}
