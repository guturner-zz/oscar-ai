package com.guy.home.ai.wikipedia;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.powermock.reflect.Whitebox;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WikipediaHelper.class)
public class WikipediaHelperTest {

	@Test
	public void testFormatFact_Pronoun() throws Exception {
		WikipediaHelper wikiHelper = new WikipediaHelper();

		String deviceName = "Alexa";
		String fact = "Alexa is software, it's a home assistant.";
		String expectedFact = "Alexa is software, Alexa's a home assistant.";

		assertEquals(expectedFact, Whitebox.invokeMethod(wikiHelper, "formatFact", deviceName, fact));
	}

	@Test
	public void testFormatFact_HTML() throws Exception {
		WikipediaHelper wikiHelper = new WikipediaHelper();

		String deviceName = "Alexa";
		String fact = "<b>Alexa</b> is <i>software</i>.";
		String expectedFact = "Alexa is software.";

		assertEquals(expectedFact, Whitebox.invokeMethod(wikiHelper, "formatFact", deviceName, fact));
	}

	@Test
	public void testGetArticleIntro() throws Exception {
		WikipediaHelper wikiHelper = new WikipediaHelper();

		Map<String, Object> metadataMap = new HashMap<String, Object>() {{
			put("pageid", "51060375");
			put("ns", 0);
			put("title", "Amazon_Alexa");
			put("extract", "This is an intro.");
		}};
		Map<String, Object> requestMap = new HashMap<String, Object>() {{
			put("51060375", metadataMap);
		}};
		String expectedIntro = "This is an intro.";

		assertEquals(expectedIntro, Whitebox.invokeMethod(wikiHelper, "getArticleIntro", requestMap));
	}
	
	@Test
	public void testGenerateFactsList() throws Exception {
		WikipediaHelper wikiHelper = new WikipediaHelper();

		String subject = "Amazon_Alexa";
		String intro = "Alexa is software. It is smart. The program works, it's great!";
		
		List<String> facts = Whitebox.invokeMethod(wikiHelper, "generateFactsList", intro, subject);
		
		assertTrue(facts.contains("Alexa is software"));
		assertTrue(facts.contains("Alexa is smart"));
		assertTrue(facts.contains("The program works, Alexa's great!"));
	}
}
