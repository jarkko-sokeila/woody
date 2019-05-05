package com.sokeila.woody.backend.entity;

public enum RssSource {
	ILTALEHTI("https://www.iltalehti.fi/rss/uutiset.xml", "https://www.iltalehti.fi/rss/urheilu.xml", null),
	IS("https://www.is.fi/rss/tuoreimmat.xml", "https://www.is.fi/rss/urheilu.xml", null),
	IO_TECH(null, null, "https://www.io-tech.fi/feed/"),
	YLE("https://feeds.yle.fi/uutiset/v1/recent.rss?publisherIds=YLE_UUTISET", null, null);
	
	private String newsSource;
	private String sportsSource;
	private String itSource;
	
	private RssSource(String newsSource, String sportsSource, String itSource) {
		this.newsSource = newsSource;
		this.sportsSource = sportsSource;
		this.itSource = itSource;
	}

	public String getNewsSource() {
		return newsSource;
	}

	public String getSportsSource() {
		return sportsSource;
	}

	public String getItSource() {
		return itSource;
	}
}
