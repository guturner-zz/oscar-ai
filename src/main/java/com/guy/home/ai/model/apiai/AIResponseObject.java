package com.guy.home.ai.model.apiai;

import org.springframework.stereotype.Component;

public class AIResponseObject {
	private String speech;
	private String displayText;
	private String source = "oscar-ai";

	public String getSpeech() {
		return speech;
	}

	public void setSpeech(String speech) {
		this.speech = speech;
	}

	public String getDisplayText() {
		return displayText;
	}

	public void setDisplayText(String displayText) {
		this.displayText = displayText;
	}

	public String getSource() {
		return source;
	}
}