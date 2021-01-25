package com.sokeila.woody.backend;

import java.net.URL;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.net.ssl.SSLContext;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.rometools.rome.feed.synd.SyndCategory;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;
import com.sokeila.woody.backend.entity.Category;
import com.sokeila.woody.backend.entity.Feed;
import com.sokeila.woody.backend.entity.RssSource;
import com.sokeila.woody.backend.entity.SubCategory;
import com.sokeila.woody.backend.services.FeedRepository;

@Component
public class ScheduledTasks {

    private static final Logger log = LoggerFactory.getLogger(ScheduledTasks.class);

    @Autowired
    private FeedRepository feedRepository;

    private static Cache<String, Boolean> cache = CacheBuilder.newBuilder()
			.expireAfterWrite(6, TimeUnit.HOURS)
			.build();
    
    @Scheduled(fixedRate = 5000)
    public void readRSSFeeds() {
		for(RssSource rssSource: RssSource.values()) {
			parseRss(rssSource, Category.NEWS, RssSource::getNewsSource);
			parseRss(rssSource, Category.SPORTS, RssSource::getSportsSource);
			parseRss(rssSource, Category.IT, RssSource::getItSource);
		}
    }

	private void parseRss(RssSource rssSource, Category defaultCategory, Function<RssSource, String> urlFunction) {
		String url = urlFunction.apply(rssSource);
		
		try {
			SSLContext context = SSLContext.getInstance("TLSv1.2");
			context.init(null,null,null);
			SSLContext.setDefault(context);
			if(url != null && url.length() > 0) {
	            try (XmlReader reader = new XmlReader(new URL(url))) {
	                SyndFeed feed = new SyndFeedInput().build(reader);
	                
	                log.debug(feed.getTitle());
	                log.debug("***********************************");
	                int newCount = 0;
	                for (SyndEntry entry : feed.getEntries()) {
	                	if(entry != null && !feedExists(entry)) {
							log.debug("{}", entry);
	                    	createNewFeed(entry, rssSource, defaultCategory);
							newCount++;
	                    	log.debug("***********************************");
	                    }
	                }
	                if(newCount > 0) {
	                	log.info("Created {} new feeds from ur {}", newCount, url);
					}
	                log.debug("Done");
	            }
			}
        }  catch (Exception e) {
            log.debug("Error while reading rss source", e);
			Boolean hasValue = cache.getIfPresent(url);
			if(hasValue == null || !hasValue) {
				cache.put(url, true);
				log.warn("Url trying to read was {}", url);
			}
        }
	}

	private void createNewFeed(SyndEntry entry, RssSource rssSource, Category defaultCategory) {
		Feed entity = new Feed();
		
		CategoryData categoryData = resolveCategoryData(entry);
		
		Date now = Calendar.getInstance().getTime();
		entity.setRssSource(rssSource);
		entity.setTitle(entry.getTitle());
		entity.setDescription(formatDescription(entry.getDescription() != null ? entry.getDescription().getValue(): null));
		entity.setGuid(entry.getUri());
		entity.setLink(entry.getLink());
		entity.setPublished(entry.getPublishedDate().after(now) ? now: entry.getPublishedDate());
		entity.setCategory(categoryData != null ? categoryData.getCategory(): defaultCategory);
		entity.setSubCategory(categoryData != null ? categoryData.getSubCategory() : null);
		entity.setCreated(now);
		
		feedRepository.save(entity);
	}

	private String formatDescription(String description) {
		if(description == null)
			return null;
		
		String strRegEx = "<[^>]*>";
		description = description.replaceAll(strRegEx, "");
		
		description = description.replace("&#8221;", "\"");
		description = description.replace("&#8211;", "-");
		description = description.replace("&#8230;", "...");
		description = description.replace("&#8217;", "’");
		description = description.replace("&#228;", "ä");
		description = description.replace("&#246;", "ö");

		if(description.length() > 4096) {
			log.debug("description length {}", description.length());
			description = description.substring(0, 4092) + "...";
			log.debug("description length {}", description.length());
			log.debug("description {}", description);
		}
		
		return description;
	}

	private CategoryData resolveCategoryData(SyndEntry entry) {
		CategoryData categoryData = null;
		List<SyndCategory> syndCategories = entry.getCategories();
		if(syndCategories.size() == 1) {
			categoryData = resolveCategoryData(syndCategories.get(0));
		} else {
			Set<CategoryData> categorySet = new HashSet<>();
			syndCategories = filterGeneralCategories(syndCategories);
			for(SyndCategory syndCategory: syndCategories) {
				CategoryData temp = resolveCategoryData(syndCategory);
				if(temp != null) {
					categorySet.add(temp);
				}
			}
			
			if(categorySet.size() == 1) {
				categoryData = categorySet.iterator().next();
			}
		}
		
		if(categoryData == null && syndCategories.size() > 0) {
			Collection<String> categories = syndCategories.stream().map(SyndCategory::getName).filter(Objects::nonNull).collect(Collectors.toList());
			log.info("Could not resolve category for title {}. \nCategories in feed are: {}", entry.getTitle(), categories);
		}
		
		return categoryData;
	}

	private List<SyndCategory> filterGeneralCategories(List<SyndCategory> syndCategories) {
    	Compare generalCompare = new Compare("Uutiset", "Uutinen");
    	return syndCategories.stream().filter(syndCategory -> !categoryBelongs(syndCategory.getName().trim(), null, generalCompare)).collect(Collectors.toList());
	}

	private CategoryData resolveCategoryData(SyndCategory syndCategory) {
    	if(StringUtils.isBlank(syndCategory.getName()))
    		return null;

		String categoryName = syndCategory.getName().trim();
		//Resolve NEWS sub categories
		if(categoryBelongs(categoryName, null, new Compare("Uutiset", "Uutinen"))) {
			return new CategoryData(Category.NEWS, null);
		} else if(categoryBelongs(categoryName, null, new Compare("Kotimaa", "Kotimaan uutiset", "Päijät-Häme", "Turun seutu", "Keskusta nyt", "Oulun seutu", "Tampereen seutu", "Vantaa", "Kaupunki", "Oulu", "Tuusula"))) {
			return new CategoryData(Category.NEWS, SubCategory.HOMELAND);
		} else if(categoryBelongs(categoryName, null, new Compare("Ulkomaa", "Ulkomaat"))) {
			return new CategoryData(Category.NEWS, SubCategory.ABROAD);
		} else if(categoryBelongs(categoryName, null, new Compare("Tiede"))) {
			return new CategoryData(Category.NEWS, SubCategory.SCIENCE);
		} else if(categoryBelongs(categoryName, null, new Compare("Talous", "Taloussanomat", "Pörssiuutiset", "Sijoittaminen", "Kauppa", "Kansantalous", "Yrittäminen", "tyoelama", "Työelämä", "Asunnot", "asuntokauppa", "Rakentaminen", "Kiinteistöt", "Suunnittelu", "Yhteiskunta"))) {
			return new CategoryData(Category.NEWS, SubCategory.ECONOMY);
		} else if(categoryBelongs(categoryName, null, new Compare("Politiikka"))) {
			return new CategoryData(Category.NEWS, SubCategory.POLITICS);
		}
		
		//Resolve ENTERTAINMENT sub categories
		else if(categoryBelongs(categoryName, null, new Compare("Viihde", "Kulttuuri", "Ajanviete", "Viihdeuutiset", "uutiset - viihde"))) {
			return new CategoryData(Category.ENTERTAINMENT, null);
		} else if(categoryBelongs(categoryName, null, new Compare("Musiikki", "uutiset - musiikki"))) {
			return new CategoryData(Category.ENTERTAINMENT, SubCategory.MUSIC);
		} else if(categoryBelongs(categoryName, null, new Compare("Elokuvat", "TV & elokuvat", "uutiset - elokuvat", "uutiset - tv", "Elokuvauutiset", "Netflix", "HBO Nordic"))) {
			return new CategoryData(Category.ENTERTAINMENT, SubCategory.MOVIES);
		}
		
		//Resolve SPORTS sub categories
		else if(categoryBelongs(categoryName, null, new Compare("Urheilu", "Sport"))) {
			return new CategoryData(Category.SPORTS, null);
		} else if(categoryBelongs(categoryName, new Compare("kiekko"), new Compare("NHL", "Liiga", "SM-liiga", "smliiga", "KHL"))) {
			return new CategoryData(Category.SPORTS, SubCategory.ICE_HOCKEY);
		} else if(categoryBelongs(categoryName, null, new Compare("Jalkapallo", "Veikkausliiga", "Mestarien liiga", "mestarienliiga", "Eurosarjat", "valioliiga"))) {
			return new CategoryData(Category.SPORTS, SubCategory.FOOTBALL);
		} else if(categoryBelongs(categoryName, null, new Compare("Yleisurheilu"))) {
			return new CategoryData(Category.SPORTS, SubCategory.ATHLEITCS);
		} else if(categoryBelongs(categoryName, null, new Compare("Formula 1", "formulat"))) {
			return new CategoryData(Category.SPORTS, SubCategory.FORMULA1);
		} else if(categoryBelongs(categoryName, null, new Compare("Ralli", "Rallin MM-sarja", "ralliautoilu", "moottoriurheilu"))) {
			return new CategoryData(Category.SPORTS, SubCategory.RALLY);
		} else if(categoryBelongs(categoryName, null, new Compare("Salibandy"))) {
			return new CategoryData(Category.SPORTS, SubCategory.FLOORBALL);
		} else if(categoryBelongs(categoryName, null, new Compare("Koripallo", "NBA", "Korisliiga"))) {
			return new CategoryData(Category.SPORTS, SubCategory.BASKETBALL);
		} else if(categoryBelongs(categoryName, new Compare("ravi", "Ravi"), new Compare("ravi"))) {
			return new CategoryData(Category.SPORTS, SubCategory.HARNESS_RACING);
		} else if(categoryBelongs(categoryName, null, new Compare("Nyrkkeily", "kamppailulajit"))) {
			return new CategoryData(Category.SPORTS, SubCategory.MARTIAL_ARTS);
		} else if(categoryBelongs(categoryName, null, new Compare("talviurheilu", "Alppihiihto", "Maastohiihto", "Hiihtolajit", "Ampumahiihto", "mäkihyppy"))) {
			return new CategoryData(Category.SPORTS, SubCategory.WINTER_SPORTS);
		} else if(categoryBelongs(categoryName, null, new Compare("Esports"))) {
			return new CategoryData(Category.SPORTS, SubCategory.E_SPORTS);
		} else if(categoryBelongs(categoryName, null, new Compare("Golf"))) {
			return new CategoryData(Category.SPORTS, SubCategory.GOLF);
		} else if(categoryBelongs(categoryName, null, new Compare("Tennis", "tennis"))) {
			return new CategoryData(Category.SPORTS, SubCategory.TENNIS);
		} else if(categoryBelongs(categoryName, null, new Compare("Lentopallo"))) {
			return new CategoryData(Category.SPORTS, SubCategory.VOLLEY_BALL);
		} else if(categoryBelongs(categoryName, null, new Compare("Moottoripyöräily"))) {
			return new CategoryData(Category.SPORTS, SubCategory.MOTORBIKES);
		} else if(categoryBelongs(categoryName, null, new Compare("Pesäpallo"))) {
			return new CategoryData(Category.SPORTS, SubCategory.BASEBALL);
		} else if(categoryBelongs(categoryName, null, new Compare("muutlajit", "fitnessvoimailu", "Muut lajit", "Muut sarjat"))) {
			return new CategoryData(Category.SPORTS, SubCategory.OTHER_SPORTS);
		}
		
		//Resolve IT sub categories
		else if(categoryBelongs(categoryName, null, new Compare("Tietokoneet", "Tietotekniikka", "Pelit", "Digitalous", "Teknologia", "Digitoday", "Tietoturva", "Tekoäly", "ECF", "Tekniset artikkelit", "Virtuaalitodellisuus", "Digi", "Bitcoin", "Iot"))) {
			return new CategoryData(Category.IT, null);
		} else if(categoryBelongs(categoryName, null, new Compare("Oppo", "5G", "Honor", "puhelin", "Android", "Mobiili", "Älypuhelimet"))) {
			return new CategoryData(Category.IT, SubCategory.MOBILE);
		}
		
		//Resolve CARS sub categories
		else if(categoryBelongs(categoryName, null, new Compare("Autot", "Auto"))) {
			return new CategoryData(Category.CARS, null);
		}
		
		//Resolve MOTORBIKES sub categories
		else if(categoryBelongs(categoryName, null, new Compare("Moottoripyörä", "Road Racing"))) {
			return new CategoryData(Category.MOTORBIKES, null);
		}
		
		//Resolve SCIENCE sub categories
		else if(categoryBelongs(categoryName, null, new Compare("Tähdet", "Avaruus"))) {
			return new CategoryData(Category.SCIENCE, null);
		}
		
		//Resolve LIFESTYLE sub categories
		else if(categoryBelongs(categoryName, null, new Compare("Blogit", "Me Naiset", "Asuminen", "MyStyle", "Laihdutus", "Meidän Perhe", "Soppa365", "Kodin Kuvalehti", "Hyvä terveys", "Gloria", "Matkailu", "Lifestyle", "Matkat", "Tyyli"))) {
			return new CategoryData(Category.LIFESTYLE, null);
		}
		
		return null;
	}

	private boolean categoryBelongs(String categoryName, Compare compareContains, Compare compareEquals) {
		if(compareContains != null) {
			for(String category: compareContains.getCategoryNames()) {
				if(categoryName.contains(category))
					return true;
			}
		}
		
		if(compareEquals != null) {
			for(String category: compareEquals.getCategoryNames()) {
				if(categoryName.equalsIgnoreCase(category))
					return true;
			}
		}
		return false;
	}

	private boolean feedExists(SyndEntry entry) {
		return feedRepository.existsByGuid(entry.getUri());
	}
	
	private static class CategoryData {
		private final Category category;
		private final SubCategory subCategory;
		
		public CategoryData(Category category, SubCategory subCategory) {
			this.category = category;
			this.subCategory = subCategory;
		}
		
		public Category getCategory() {
			return category;
		}
		public SubCategory getSubCategory() {
			return subCategory;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;

			if (!(o instanceof CategoryData)) return false;

			CategoryData that = (CategoryData) o;

			return new EqualsBuilder().append(getCategory(), that.getCategory()).append(getSubCategory(), that.getSubCategory()).isEquals();
		}

		@Override
		public int hashCode() {
			return new HashCodeBuilder(17, 37).append(getCategory()).append(getSubCategory()).toHashCode();
		}
	}
	
	private static class Compare {
		private final Collection<String> categoryNames;
		
		public Compare(String... values) {
			categoryNames = Arrays.asList(values);
		}
		public Collection<String> getCategoryNames() {
			return categoryNames;
		}
	}
}
