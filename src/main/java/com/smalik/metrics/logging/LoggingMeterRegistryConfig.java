package com.smalik.metrics.logging;

import io.micrometer.core.instrument.Clock;
import io.micrometer.core.instrument.step.StepRegistryConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.time.Duration;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;

@Configuration
@Profile("logging")
public class LoggingMeterRegistryConfig {

    @Bean
    public StepRegistryConfig customRegistryConfig() {
        return new StepRegistryConfig() {
            @Override
            public String prefix() {
                return "";
            }

            @Override
            public String get(String key) {
                return null;
            }

            @Override
            public Duration step() {
                return Duration.ofSeconds(60);
            }
        };
    }

    @Bean
    public LoggingMeterRegistry customRegistry(StepRegistryConfig registryConfig) {
        LoggingMeterRegistry registry = new LoggingMeterRegistry(registryConfig, Clock.SYSTEM);
        registry.start(Executors.defaultThreadFactory());
        return registry;
    }
}