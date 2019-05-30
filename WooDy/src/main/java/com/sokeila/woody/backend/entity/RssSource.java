package com.sokeila.woody.backend.entity;

/**
 * When add new source, remember edit FeedItem.js
 *
 */
public enum RssSource {
	ILTALEHTI("https://www.iltalehti.fi/rss/uutiset.xml", "https://www.iltalehti.fi/rss/urheilu.xml", null),
	IS("https://www.is.fi/rss/tuoreimmat.xml", "https://www.is.fi/rss/urheilu.xml", "https://www.is.fi/rss/digitoday.xml"),
	IO_TECH(null, null, "https://www.io-tech.fi/feed/"),
	YLE("https://feeds.yle.fi/uutiset/v1/recent.rss?publisherIds=YLE_UUTISET", "https://feeds.yle.fi/uutiset/v1/recent.rss?publisherIds=YLE_URHEILU", null),
	KAUPPALEHTI("https://feeds.kauppalehti.fi/rss/main", null, null),
	MUROPAKETTI(null, null, "https://muropaketti.com/feed"),
	HS("https://www.hs.fi/rss/tuoreimmat.xml", "https://www.hs.fi/rss/urheilu.xml", null),
	ESS("https://www.ess.fi/feed/rss/1672716", null, null),
	SUOMIURHEILU(null, "https://www.suomiurheilu.com/feed/", null),
	SUOMIKIEKKO(null, "https://www.suomikiekko.com/feed/", null),
	SUOMIF1(null, "https://www.suomif1.com/feed/", null),
	SUOMIKORIS(null, "https://www.suomikoris.com/feed/", null),
	SUOMIFUTIS(null, "https://www.suomifutis.com/feed/", null),
	TEKNIIKAN_MAAILMA("https://vanha.tekniikanmaailma.fi/feed/", null, null),
	SUOMENMAA("https://www.suomenmaa.fi/neo/neoproxy.dll?tem=rss_neo&tem=rss_neo", null, null),
	KALEVA("https://www.kaleva.fi/rss/show/", "https://www.kaleva.fi/rss/show/?channels=2", null),
	POLIISI("https://www.poliisi.fi/RSS/fi/1/1/tietoa_poliisista/tiedotteet", null, null),
	TIVI(null, null, "https://www.tivi.fi/api/feed/v2/rss/tv"),
	ETN(null, "http://etn.fi/index.php?format=feed&type=rss", null),
	ME_NAISET("https://www.menaiset.fi/rss/artikkelit.xml", null, null),
	BIKE("https://www.bike.fi/feed/", null, null),
	RADIO_NOVA("http://www.radionova.fi/feed/rss", null, null),
	PELAAJA("https://pelaaja.fi/rss/uusimmat.xml", null, null),
	RAKENNUSLEHTI("https://www.rakennuslehti.fi/feed/", null, null),
	PUHELINVERTAILU("https://rss.afterdawn.com/news_puhelinvertailu.cfm", null, null),
	MOBIILI("https://mobiili.fi/feed/", null, null),
	SOUNDI("https://www.soundi.fi/feed/", null, null),
	MIKROBITTI("https://www.mikrobitti.fi/api/feed/v2/rss/mb", null, null),
	STARA("http://feeds.feedburner.com/stara?format=xml", null, null),
	FINDANCE("http://feeds.feedburner.com/findancecom?format=xml", null, null),
	HYMY("https://hymy.fi/feed/", null, null);
	//LEIJONAT("https://www.leijonat.fi/index.php/uutiset?format=feed", null, null);
	//KESKISUOMALAINEN("https://www.jyvaskyla.fi/term/103/rss.xml", null, null);
	
	/*
	 * https://www.v2.fi/pelit/rss/feed.xml
	 * https://www.v2.fi/viihde/rss/feed.xml
		https://www.gamereactor.fi/rss/rss.php?texttype=4
		https://www.gamereactor.fi/rss/rss.php?texttype=3
		https://www.gamereactor.fi/rss/rss.php?texttype=2
		https://www.gamereactor.fi/rss/rss.php?texttype=1
	*/
	
	
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
