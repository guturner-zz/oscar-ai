package com.guy.home.ai.controllers;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.guy.home.ai.constants.DeviceConstants;
import com.guy.home.ai.constants.IntentConstants;
import com.guy.home.ai.model.AIRequestObject;
import com.guy.home.ai.model.AIResponseObject;
import com.guy.home.ai.model.Metadata;

@RestController
public class WebhookController {

	private static final String DEFAULT_SPEECH = "Hmm, I don't know what you mean.";

	@RequestMapping(value = "/webhook", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public AIResponseObject webhook(@RequestBody AIRequestObject request, HttpServletResponse response) {
		AIResponseObject respObj = new AIResponseObject();
		respObj.setSpeech(DEFAULT_SPEECH);
	
		Metadata metadata = request.getMetadata();
		String intentName = metadata.getIntentName();
		
		if (intentName != null) {
			switch (intentName) {
			case IntentConstants.INQUIRE_SMARTDEVICE:
				respObj.setSpeech(getInquireSmartDeviceResponse(request));
				break;
			}
		}
		
		return respObj;
	}

	private String getInquireSmartDeviceResponse(AIRequestObject request) {
		String device = request.getParameters().get("device");
		String response = "Sorry, I actually don't know much about " + device + ".";
				
		switch (device) {
		case DeviceConstants.ALEXA:
			response = "Alexa is Amazon's smart home device.";
		}
		
		return response;
	}
}