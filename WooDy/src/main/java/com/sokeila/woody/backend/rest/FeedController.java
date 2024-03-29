package com.sokeila.woody.backend.rest;

import java.nio.charset.StandardCharsets;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.google.common.hash.Hashing;
import com.sokeila.woody.backend.entity.Category;
import com.sokeila.woody.backend.entity.Feed;
import com.sokeila.woody.backend.entity.IPHash;
import com.sokeila.woody.backend.entity.RssSource;
import com.sokeila.woody.backend.services.FeedRepository;
import com.sokeila.woody.backend.services.IPHashRepository;

@RestController
@RequestMapping("/rest")
public class FeedController {
	private static final Logger log = LoggerFactory.getLogger(FeedController.class);
	
	@PersistenceContext
    private EntityManager entityManager;
	
	@Autowired
    private FeedRepository feedRepository;
	
	@Autowired
    private IPHashRepository ipHashRepository;
	
	@GetMapping(path = "/news")
	public ResponseEntity<Page<Feed>> news(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "category", required = false) Category category, HttpServletRequest request) {
		if(page == null) {
			page = 0;
		}
		
		log.info("f ip: {}, r ip: {}", request.getHeader("X-FORWARDED-FOR"), request.getRemoteAddr());
		log.info("Load news in page {} and category {}", page, category);
		
		Pageable pageable = PageRequest.of(page, 200);
		Page<Feed> result;
		if(category == null) {
			result = feedRepository.findByOrderByPublishedDesc(pageable);
		} else {
			result = feedRepository.findByCategoryOrderByPublishedDesc(category, pageable);
		}
		
		result.get().forEach(feed -> {
			feed.resolveClickCount();  // Get click count from IP clicks
			feed.setIPClicks(null); // Don't expose IP clicks to rest client
		});
		
		return ResponseEntity.ok().body(result);
	}
	
	@GetMapping(path = "/getfeed")
	public ResponseEntity<Feed> getFeed(@RequestParam(name = "id") long id) {
		log.info("Find feed with id {}", id);

		Optional<Feed> feedOpt = feedRepository.findById(id);
		if(feedOpt.isPresent()) {
			Feed result = feedOpt.get();
			result.resolveClickCount(); // Get click count from IP clicks
			result.setIPClicks(null); // Don't expose IP clicks to rest client

			return ResponseEntity.ok().body(result);
		}

		return ResponseEntity.notFound().build();
	}
	
	@PostMapping(path = "/linkclick")
	public void linkClick(@RequestParam(name = "id") long id, HttpServletRequest request) {
		log.info("Link with id {} clicked", id);
		
		String remoteAddr = "";
        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }
        
        if(remoteAddr != null && remoteAddr.length() > 0) {
        	String hashed = Hashing.sha256().hashString(remoteAddr, StandardCharsets.UTF_8).toString();
        	log.info("IP is {}, hashed {}, length {}", remoteAddr, hashed, hashed.length());
        	Optional<Feed> feedOptional = feedRepository.findById(id);
        	if(feedOptional.isPresent()) {
        		Feed feed = feedOptional.get();
        		log.info("Add click to feed id {}", feed.getId());
        		IPHash ipHash = ipHashRepository.findByIpHash(hashed);
        		if(ipHash == null) {
        			ipHash = new IPHash(hashed);
        		}
        		feed.addIpClick(ipHash);
        		feedRepository.save(feed);
        	}
        }
	}

	@GetMapping(path = "/unreadcount")
	public ResponseEntity<Long> getUnreadCount(@RequestParam(name = "category", required = false) Category category, @RequestParam(name = "datetime") String dateTime) {
		Date date = new Date(Long.parseLong(dateTime));
		
		long unreadCount;
		if(category == null) {
			unreadCount = feedRepository.countByCreatedGreaterThan(date);
		} else {
			unreadCount = feedRepository.countByCategoryAndCreatedGreaterThan(category, date);
		}
		
		log.debug("UnreadCount after date " + dateToString(date) + " in category " + category + " is " + unreadCount);

		return ResponseEntity.ok().body(unreadCount);
	}
	
	private String dateToString(Date date) {
		String pattern = "dd.MM.yyyy HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern);
		String dateAsString = df.format(date);
		return dateAsString;
	}

	@GetMapping(path = "/test")
	public String test() {
		return "Server time " + LocalDateTime.now().toString();
	}
	
	@GetMapping(path = "/addfeed")
	public void addFeed(@RequestParam(name = "category", required = false) Category category) {
		Feed entity = new Feed();
		entity.setRssSource(RssSource.ILTALEHTI);
		entity.setTitle("Test title");
		entity.setDescription("Test description");
		entity.setGuid(UUID.randomUUID().toString());
		entity.setLink("http://www.google.com");
		entity.setPublished(Calendar.getInstance().getTime());
		entity.setCategory(category == null ? Category.NEWS: category);
		entity.setSubCategory(null);
		entity.setCreated(Calendar.getInstance().getTime());
		
		feedRepository.save(entity);
	}
}
