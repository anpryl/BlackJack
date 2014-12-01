package com.prylutskyi.blackjack.config;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * Created by anatolii.prylutskyi on 12/1/2014.
 */

@Configuration
@ComponentScan("com.prylutskyi.blackjack")
@EnableAutoConfiguration
public class BlackJackWebApp {
    public static void main(String[] args) {
        SpringApplication.run(BlackJackWebApp.class, args);
    }
}
