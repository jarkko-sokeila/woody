package com.sokeila.woody.backend;

import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.net.ssl.SSLContext;

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

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    @Autowired
    private FeedRepository feedRepository;
    
    @Scheduled(fixedRate = 5000)
    public void readRSSFeeds() {
        //log.info("The time is now {}", dateFormat.format(new Date()));
        readFeeds();
    }

	private void readFeeds() {
		for(RssSource rssSource: RssSource.values()) {
			parseRss(rssSource, Category.NEWS, RssSource::getNewsSource);
			parseRss(rssSource, Category.SPORTS, RssSource::getSportsSource);
			parseRss(rssSource, Category.IT, RssSource::getItSource);
		}
		
		/*try {
            String url = "https://www.iltalehti.fi/rss/uutiset.xml";
            String url2 = "https://www.is.fi/rss/kotimaa.xml";
 
            try (XmlReader reader = new XmlReader(new URL(url2))) {
                SyndFeed feed = new SyndFeedInput().build(reader);
                System.out.println(feed.getTitle());
                System.out.println("***********************************");
                for (SyndEntry entry : feed.getEntries()) {
                    System.out.println(entry);
                    System.out.println("***********************************");
                }
                System.out.println("Done");
            }
        }  catch (Exception e) {
            e.printStackTrace();
        }*/
	}

	private void parseRss(RssSource rssSource, Category category, Function<RssSource, String> urlFunction) {
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
	                for (SyndEntry entry : feed.getEntries()) {
	                	if(!feedExists(entry)) {
	                    	createNewFeed(entry, rssSource, category);
	                    	log.debug("{}", entry);
	                    	log.debug("***********************************");
	                    }
	                }
	                log.debug("Done");
	            }
			}
        }  catch (Exception e) {
            log.error("Error while reading rss source", e);
            log.error("Url trying to read was {}", url);
        }
	}

	private void createNewFeed(SyndEntry entry, RssSource rssSource, Category category) {
		Feed entity = new Feed();
		
		CategoryData categoryData = resolveCategoryData(entry);
		
		entity.setRssSource(rssSource);
		entity.setTitle(entry.getTitle());
		entity.setDescription(formatDescription(entry.getDescription() != null ? entry.getDescription().getValue(): null));
		entity.setGuid(entry.getUri());
		entity.setLink(entry.getLink());
		entity.setPublished(entry.getPublishedDate());
		entity.setCategory(categoryData != null ? categoryData.getCategory(): category);
		entity.setSubCategory(categoryData != null ? categoryData.getSubCategory() : null);
		
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
		
		
		if(description.length() > 4096) {
			log.info("description length {}", description.length());
			description = description.substring(0, 4092) + "...";
			log.info("description length {}", description.length());
			log.info("description {}", description);
		}
		
		return description;
	}

	private CategoryData resolveCategoryData(SyndEntry entry) {
		CategoryData categoryData = null;
		if(entry.getCategories().size() == 1) {
			categoryData = resolveCategoryData(entry.getCategories().get(0));
		} else {
			Set<CategoryData> subCategorySet = new HashSet<>();
			for(SyndCategory syndCategory: entry.getCategories()) {
				CategoryData temp = resolveCategoryData(syndCategory);
				if(temp != null) {
					subCategorySet.add(temp);
				}
			}
			
			if(subCategorySet.size() == 1 || (subCategorySet.size() > 1 && onlySingleSubCategory(subCategorySet))) {
				categoryData = subCategorySet.iterator().next();
			}
		}
		
		if(categoryData == null) {
			log.info("Could not resolve category for title {}. \nCategories in feed are \n{}", entry.getTitle(), collectGategories(entry.getCategories()));
		}
		
		return categoryData;
	}

	private boolean onlySingleSubCategory(Set<CategoryData> subCategorySet) {
		SubCategory subCategory = null;
		boolean first = true;
		for(CategoryData cd: subCategorySet) {
			if(cd.getSubCategory() != null) {
				if(first == true) {
					subCategory = cd.getSubCategory();
					first = false;
				} else {
					if(cd.getSubCategory() != subCategory) {
						return false;
					}
				}
			}
		}
		return true;
	}

	private Collection<String> collectGategories(List<SyndCategory> categories) {
		Collection<String> result = new ArrayList<>();
		for(SyndCategory sc: categories) {
			result.add(sc.getName() + "\n");
		}
		return result;
	}

	private CategoryData resolveCategoryData(SyndCategory syndCategory) {
		String categoryName = syndCategory.getName();
		//Resolve NEWS sub categories
		if(categoryBelongs(categoryName, null, new Compare("Uutiset"))) {
			return new CategoryData(Category.NEWS, null);
		} else if(categoryBelongs(categoryName, null, new Compare("Kotimaa", "Kotimaan uutiset", "Päijät-Häme", "Turun seutu", "Keskusta nyt", "Oulun seutu", "Tampereen seutu", "Vantaa", "Kaupunki"))) {
			return new CategoryData(Category.NEWS, SubCategory.HOMELAND);
		} else if(categoryBelongs(categoryName, null, new Compare("Ulkomaa", "Ulkomaat"))) {
			return new CategoryData(Category.NEWS, SubCategory.ABROAD);
		} else if(categoryBelongs(categoryName, null, new Compare("Tiede"))) {
			return new CategoryData(Category.NEWS, SubCategory.SCIENCE);
		} else if(categoryBelongs(categoryName, null, new Compare("Talous", "Taloussanomat", "Pörssiuutiset", "Sijoittaminen", "Kauppa", "Kansantalous", "Yrittäminen", "tyoelama"))) {
			return new CategoryData(Category.NEWS, SubCategory.ECONOMY);
		} else if(categoryBelongs(categoryName, null, new Compare("Politiikka"))) {
			return new CategoryData(Category.NEWS, SubCategory.POLITICS);
		}
		
		//Resolve ENTERTAINMENT sub categories
		else if(categoryBelongs(categoryName, null, new Compare("Viihde"))) {
			return new CategoryData(Category.ENTERTAINMENT, null);
		} else if(categoryBelongs(categoryName, null, new Compare("Musiikki"))) {
			return new CategoryData(Category.ENTERTAINMENT, SubCategory.MUSIC);
		} else if(categoryBelongs(categoryName, null, new Compare("Elokuvat"))) {
			return new CategoryData(Category.ENTERTAINMENT, SubCategory.MOVIES);
		}
		
		//Resolve SPORTS sub categories
		else if(categoryBelongs(categoryName, null, new Compare("Urheilu"))) {
			return new CategoryData(Category.SPORTS, null);
		} else if(categoryBelongs(categoryName, new Compare("kiekko"), new Compare("NHL", "Liiga", "SM-liiga", "smliiga", "KHL"))) {
			return new CategoryData(Category.SPORTS, SubCategory.ICE_HOCKEY);
		} else if(categoryBelongs(categoryName, null, new Compare("Jalkapallo", "Veikkausliiga", "Mestarien liiga", "mestarienliiga", "Eurosarjat", "valioliiga"))) {
			return new CategoryData(Category.SPORTS, SubCategory.FOOTBALL);
		} else if(categoryBelongs(categoryName, null, new Compare("Yleisurheilu"))) {
			return new CategoryData(Category.SPORTS, SubCategory.ATHLEITCS);
		} else if(categoryBelongs(categoryName, null, new Compare("Formula 1", "formulat"))) {
			return new CategoryData(Category.SPORTS, SubCategory.FORMULA1);
		} else if(categoryBelongs(categoryName, null, new Compare("Ralli"))) {
			return new CategoryData(Category.SPORTS, SubCategory.RALLY);
		} else if(categoryBelongs(categoryName, null, new Compare("Salibandy"))) {
			return new CategoryData(Category.SPORTS, SubCategory.FLOORBALL);
		} else if(categoryBelongs(categoryName, null, new Compare("Koripallo", "NBA", "Korisliiga"))) {
			return new CategoryData(Category.SPORTS, SubCategory.BASKETBALL);
		} else if(categoryBelongs(categoryName, new Compare("ravi", "Ravi"), new Compare("ravi"))) {
			return new CategoryData(Category.SPORTS, SubCategory.HARNESS_RACING);
		} else if(categoryBelongs(categoryName, null, new Compare("Nyrkkeily", "kamppailulajit"))) {
			return new CategoryData(Category.SPORTS, SubCategory.MARTIAL_ARTS);
		} else if(categoryBelongs(categoryName, null, new Compare("talviurheilu"))) {
			return new CategoryData(Category.SPORTS, SubCategory.WINTER_SPORTS);
		} else if(categoryBelongs(categoryName, null, new Compare("Esports"))) {
			return new CategoryData(Category.SPORTS, SubCategory.E_SPORTS);
		} else if(categoryBelongs(categoryName, null, new Compare("Golf"))) {
			return new CategoryData(Category.SPORTS, SubCategory.GOLF);
		} else if(categoryBelongs(categoryName, null, new Compare("Tennis"))) {
			return new CategoryData(Category.SPORTS, SubCategory.TENNIS);
		} else if(categoryBelongs(categoryName, null, new Compare("Lentopallo"))) {
			return new CategoryData(Category.SPORTS, SubCategory.VOLLEY_BALL);
		} else if(categoryBelongs(categoryName, null, new Compare("Moottoripyöräily"))) {
			return new CategoryData(Category.SPORTS, SubCategory.MOTORBIKES);
		} else if(categoryBelongs(categoryName, null, new Compare("Pesäpallo"))) {
			return new CategoryData(Category.SPORTS, SubCategory.BASEBALL);
		} else if(categoryBelongs(categoryName, null, new Compare("muutlajit", "fitnessvoimailu", "Muut lajit"))) {
			return new CategoryData(Category.SPORTS, SubCategory.OTHER_SPORTS);
		}
		
		//Resolve IT sub categories
		else if(categoryBelongs(categoryName, null, new Compare("Tietotekniikka", "Mobiili", "Pelit", "Digitalous", "Android", "Teknologia", "Digitoday", "Tietoturva"))) {
			return new CategoryData(Category.IT, null);
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
		if(feedRepository.existsByGuid(entry.getUri())) {
			return true;
		}
		
		return false;
	}
	
	private static class CategoryData {
		private Category category;
		private SubCategory subCategory;
		
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
	}
	
	private static class Compare {
		private Collection<String> categoryNames;
		
		public Compare(String... values) {
			categoryNames = Arrays.asList(values);
		}
		public Collection<String> getCategoryNames() {
			return categoryNames;
		}
	}
}
