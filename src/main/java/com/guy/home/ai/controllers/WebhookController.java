package com.guy.home.ai.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.guy.home.ai.constants.DeviceConstants;
import com.guy.home.ai.constants.IntentConstants;
import com.guy.home.ai.model.apiai.AIGenericRequest;
import com.guy.home.ai.model.apiai.AIRequestObject;
import com.guy.home.ai.model.apiai.AIResponseObject;
import com.guy.home.ai.model.apiai.Metadata;
import com.guy.home.ai.wikipedia.WikipediaHelper;

@RestController
public class WebhookController {

	private static final String DEFAULT_SPEECH = "Hmm, I don't know what you mean.";

	private static final String ALEXA = "Alexa";
	private static final String ECOBEE = "the ecobee3";
	private static final String HARMONY = "Harmony";

	private Map<String, String> deviceMap = new HashMap<String, String>() {
		{
			put(ALEXA, "AMAZON_ALEXA");
			put(ECOBEE, "ECOBEE");
			put(HARMONY, "LOGITECH_HARMONY");
		}
	};

	@Autowired
	private WikipediaHelper wikiHelper;

	@RequestMapping(value = "/webhook", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public AIResponseObject webhook(@RequestBody AIGenericRequest request, HttpServletResponse response) {
		AIResponseObject respObj = new AIResponseObject();
		respObj.setSpeech(DEFAULT_SPEECH);

		AIRequestObject reqObj = request.getResult();
		Metadata metadata = reqObj.getMetadata();
		String intentName = metadata.getIntentName();

		if (intentName != null) {
			switch (intentName) {
			case IntentConstants.INQUIRE_SMARTDEVICE:
				respObj.setSpeech(getInquireSmartDeviceResponse(reqObj));
				break;
			}
		}

		return respObj;
	}

	private String getInquireSmartDeviceResponse(AIRequestObject request) {
		String device = request.getParameters().get("device");
		String response = "Sorry, I actually don't know much about " + device + ".";

		String deviceConstant = deviceMap.get(device);
		List<String> facts = wikiHelper.requestFacts(DeviceConstants.valueOf(deviceConstant).getWikiArticle());
		Integer randomNumber = new Random().nextInt(facts.size());
		response = facts.get(randomNumber);

		return response;
	}
}