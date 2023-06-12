package ru.practicum.ewmservice;

import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.practicum.stat.client.StatClient;

@Configuration
public class AppConfig {

    @Bean
    public StatClient getStatsClient() {
        return new StatClient(new RestTemplateBuilder());
    }
}
