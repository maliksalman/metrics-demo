package com.smalik.metrics;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.Random;

@EnableScheduling
@Configuration
public class LoadGenerator {

	private MetricsDemoController controller;
	private Random random;

	public LoadGenerator(MetricsDemoController controller) {
		this.controller = controller;
		this.random = new Random();
	}

	@Scheduled(fixedDelay = 5000)
	public void callEastTimers() {
	    makeSomeCalls("foo", "east", 250);
	    makeSomeCalls("foo", "east", 500);
	}

	@Scheduled(fixedDelay = 2500)
	public void callWestTimers() {
	    makeSomeCalls("foo", "west", 50);
	    makeSomeCalls("foo", "west", 100);
	}

	private void makeSomeCalls(String name, String region, int max) {
        for (int i = 0; i < random.nextInt(5); i++) {
            controller.generateArtificalTimers(name, region, max);
        }
    }

	@Scheduled(fixedDelay = 5000)
	public void callEastCounter() {
		controller.generateArtificialCounters("bar", "east");
	}

    @Scheduled(fixedDelay = 2500)
    public void callWestCounter() {
        controller.generateArtificialCounters("bar", "west");
    }
}
