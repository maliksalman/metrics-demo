package com.smalik.metrics;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@EnableScheduling()
@Configuration
public class LoadGenerator {

	private MetricsDemoController controller;

	public LoadGenerator(MetricsDemoController controller) {
		this.controller = controller;
	}

	@Scheduled(fixedDelay = 5000)
	public void callTimers() {
		for (int i = 0; i < 4; i++) {
			controller.generateArtificalTimers("some.timer", 50);
		}
		controller.generateArtificalTimers("some.timer", 1000);
	}

	@Scheduled(fixedDelay = 5000)
	public void callCounter() {
		controller.generateArtificialCounters("some.counter");
	}
}
