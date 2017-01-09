package com.guy.home.ai.controllers;

import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.guy.home.ai.model.AIResponseObject;

@RestController
public class WebhookController {

	@RequestMapping(value = "/webhook", produces = MediaType.APPLICATION_JSON_VALUE, method = RequestMethod.POST)
	public AIResponseObject webhook(@RequestBody Map<String, Object> request, HttpServletResponse response) {
		AIResponseObject respObj = new AIResponseObject();
		respObj.setSpeech("abc");
		respObj.setDisplayText("def");
		respObj.setSource("oscar-ai");
		
		String intentName = request.get("intentName").toString();
		if (intentName != null) {
			switch (intentName) {
			case "inquire.smartdevice":
				String device = ((Map)request.get("parameters")).get("device").toString();
				switch (device) {
				case "alexa":
					respObj.setSpeech("Alexa is Amazon's smart home device.");
					break;
				}
				break;
			}
		}
		
		return respObj;
	}
}