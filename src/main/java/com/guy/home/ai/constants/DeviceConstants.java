package com.guy.home.ai.constants;

public enum DeviceConstants {

	AMAZON_ALEXA("Alexa", "Amazon_Alexa"), ECOBEE("the ecobee3", "Ecobee"), LOGITECH_HARMONY("Harmony", "Logitech_Harmony");

	private String name;
	private String wikiArticle;

	private DeviceConstants(String name, String wikiArticle) {
		this.name = name;
		this.wikiArticle = wikiArticle;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getWikiArticle() {
		return wikiArticle;
	}

	public void setWikiArticle(String wikiArticle) {
		this.wikiArticle = wikiArticle;
	}
}