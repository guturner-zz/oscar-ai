package com.guy.home.ai.wikipedia;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import com.guy.home.ai.constants.DeviceConstants;
import com.guy.home.ai.model.wiki.WikiGenericRequest;

@Component
public class WikipediaHelper {
	private static final String API_ENDPOINT = "https://en.wikipedia.org/w/api.php";
	
	@Autowired
	private RestTemplate restTemplate;
	
	public List<String> requestFacts(String subject) {
		MultiValueMap<String, String> paramMap = new LinkedMultiValueMap<String, String>() {{
			add("titles", subject);
			add("action", "query");
			add("exintro", "");
			add("format", "json");
			add("prop", "extracts");
		}};
		
		URI uri = UriComponentsBuilder.fromUriString(API_ENDPOINT).queryParams(paramMap).build().toUri();
		ResponseEntity<WikiGenericRequest> response = restTemplate.getForEntity(uri, WikiGenericRequest.class);
		
		return generateFactsList(getArticleIntro(response.getBody().getQuery().getPages()), subject);
	}
	
	private List<String> generateFactsList(String articleIntro, String subject) {
		List<String> facts = new ArrayList<String>();
		
		String deviceName  = DeviceConstants.valueOf(subject.toUpperCase()).getName();
		
		for (String fact : articleIntro.split("\\. ")) {
			if (fact.length() > 150) {
				continue;
			}
			facts.add(formatFact(deviceName, fact));
		}
		
		return facts;
	}

	private String formatFact(String deviceName, String fact) {
		
		String htmlRegex = "(</*b>|</*i>|</*p>)";
		String pronounRegex = "(?i)(It |They )";
		String pronounPossessiveRegex = "(?i)(It's )";
		
		String formattedFact = fact.trim();
		formattedFact = formattedFact.replaceAll(htmlRegex, "");
		formattedFact = formattedFact.replaceAll(pronounRegex, deviceName + " ");
		formattedFact = formattedFact.replaceAll(pronounPossessiveRegex, deviceName + "'s ");
		formattedFact = formattedFact.replaceAll("(?i)( are )", " is ");
		
		if (!formattedFact.endsWith(".")) {
			formattedFact += ".";
		}
		
		return formattedFact;
	}
	
	/**
	 * Wikipedia API payload contains a KV pair of "articleId" to article metadata.
	 * Returns the "extract" metadata for the first article in the requestMap.
	 */
	private String getArticleIntro(Map<String, Object> requestMap) {
		Object[] articleIDs = requestMap.keySet().toArray();
		Map<String, Object> metadataMap = ((Map)requestMap.get(articleIDs[0]));
		return metadataMap.get("extract").toString();
	}
}
