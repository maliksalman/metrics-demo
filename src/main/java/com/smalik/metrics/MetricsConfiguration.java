package com.smalik.metrics;

import io.micrometer.core.instrument.Meter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.config.MeterFilter;
import io.micrometer.core.instrument.config.MeterFilterReply;
import io.micrometer.core.instrument.distribution.DistributionStatisticConfig;
import org.springframework.boot.actuate.autoconfigure.metrics.MeterRegistryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MetricsConfiguration {

	@Bean
	public MeterRegistryCustomizer<MeterRegistry> customizeMetrics() {
		return registry -> registry.config()
			.commonTags("env", "dev", "app", "demo")
			.meterFilter(new MeterFilter() {
				@Override
				public MeterFilterReply accept(Meter.Id id) {
					return id.getName().startsWith("app.") && !id.getName().endsWith(".percentile") ? MeterFilterReply.ACCEPT : MeterFilterReply.DENY;
				}

				@Override
				public DistributionStatisticConfig configure(Meter.Id id, DistributionStatisticConfig config) {
					return DistributionStatisticConfig.builder()
						.percentiles(0.8, 0.95)
						.build()
						.merge(config);
				}
			});
	}}
