package com.smalik.metrics;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LoadController {

	private boolean paused;

	@PostMapping("/load/pause")
	public void pause() {
		this.paused = true;
	}

	@PostMapping("/load/resume")
	public void resume() {
		this.paused = false;
	}

	public boolean isPaused() {
		return paused;
	}
}
