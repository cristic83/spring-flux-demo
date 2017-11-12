package com.cristi.springflux.annotations;

import com.cristi.springflux.domain.DummyPersonRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import com.cristi.springflux.domain.PersonRepository;
import org.springframework.web.reactive.config.EnableWebFlux;

@Configuration
@EnableWebFlux
@ComponentScan(basePackageClasses = WebFluxConfiguration.class)
public class WebFluxConfiguration {

    @Bean
    public PersonRepository personRepository() {
        return new DummyPersonRepository();
    }

}
