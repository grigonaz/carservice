package cz.cvut.kbss.ear.carservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.Scope;

import java.util.Date;

@Configuration
public class DateConfig {

    @Bean
    @Scope ("prototype")
    @Primary
    public Date getDate () {
        return new Date();
    }

}
