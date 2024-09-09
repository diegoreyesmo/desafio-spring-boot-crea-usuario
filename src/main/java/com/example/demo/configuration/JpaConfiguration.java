package com.example.demo.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing
class JpaConfiguration {

    private static final Logger log = LoggerFactory.getLogger(JpaConfiguration.class);

/*
    @Bean
    public AuditorAware<AuditableUser> auditorProvider() {
        return new AuditorAwareImpl();
    }*/
    /*@Bean
    CommandLineRunner initDatabase(UserRepository repository) {

        return args -> {
         //   log.info("Preloading " + repository.save(new User()));
         //   log.info("Preloading " + repository.save(new User()));
        };
    }*/
}