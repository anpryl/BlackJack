package com.prylutskyi.blackjack.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by anatolii.prylutskyi on 12/1/2014.
 */

@Configuration
@ComponentScan("com.prylutskyi.blackjack")
@EnableAutoConfiguration
public class BlackJackWebApp extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(BlackJackWebApp.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(BlackJackWebApp.class);
    }
}
