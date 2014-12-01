package com.prylutskyi.blackjack.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Created by Patap on 28.11.2014.
 */
@Configuration
@PropertySource(value = {"classpath:test.properties"})
public class MainTestConfig {
}
