package com.smalik.metrics;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Random;

@RestController
public class MetricsDemoController {

	private MeterRegistry metrics;
	private Random random;

	public MetricsDemoController(MeterRegistry metrics) {
		this.metrics = metrics;
		this.random = new Random();
	}

	@PostMapping("/metrics/timer/{name}/{region}/{maxWaitMillis}")
	public void generateArtificalTimers(
			@PathVariable("name") String name,
			@PathVariable("region") String region,
			@PathVariable("maxWaitMillis") int millis) {

		Timer timer = metrics.timer("app." + name, "region", region);
		timer.wrap(() -> {
			try {
				Thread.sleep((int)random.nextInt(millis));
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}).run();
	}

	@PostMapping("/metrics/counter/{name}/{region}")
	public void generateArtificialCounters(
			@PathVariable("name") String name,
            @PathVariable("region") String region) {
		metrics.counter("app." + name, "region", region).increment();
	}
}
