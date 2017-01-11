package com.guy.home.ai.controllers;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

import com.guy.home.ai.constants.IntentConstants;
import com.guy.home.ai.model.apiai.AIGenericRequest;
import com.guy.home.ai.model.apiai.AIRequestObject;
import com.guy.home.ai.model.apiai.AIResponseObject;
import com.guy.home.ai.model.apiai.Metadata;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WebhookController.class)
public class WebhookControllerTest {

	@Test
	public void testGetInquireSmartDeviceResponse_InvalidRequest() throws Exception {
		WebhookController webhookController = new WebhookController();
		AIRequestObject reqObj = new AIRequestObject();

		Map<String, String> params = new HashMap<String, String>() {
			{
				put("device", "Invalid Device");
			}
		};
		reqObj.setParameters(params);

		String response = Whitebox.invokeMethod(webhookController, "getInquireSmartDeviceResponse", reqObj);
		assertEquals("Sorry, I actually don't know much about Invalid Device.", response);
	}

	@Test
	public void testWebhook() throws Exception {
		// Given
		WebhookController controller = new WebhookController();
		WebhookController mockController = PowerMockito.spy(controller);

		AIGenericRequest request = mock(AIGenericRequest.class);
		AIRequestObject reqObj = mock(AIRequestObject.class);
		Metadata metadata = mock(Metadata.class);
		HttpServletResponse response = mock(HttpServletResponse.class);

		// When
		String expectedResponse = "Valid Response.";
		PowerMockito.doReturn(expectedResponse).when(mockController, "getInquireSmartDeviceResponse", reqObj);

		when(request.getResult()).thenReturn(reqObj);
		when(reqObj.getMetadata()).thenReturn(metadata);
		when(metadata.getIntentName()).thenReturn(IntentConstants.INQUIRE_SMARTDEVICE);

		AIResponseObject respObj = mockController.webhook(request, response);

		// Then
		assertEquals(expectedResponse, respObj.getSpeech());
	}
}
