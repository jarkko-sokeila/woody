package com.sokeila.woody.backend.rest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sokeila.woody.backend.entity.Category;
import com.sokeila.woody.backend.entity.Feed;
import com.sokeila.woody.backend.services.FeedRepository;

@RestController
public class FeedController {
	private static final Logger log = LoggerFactory.getLogger(FeedController.class);
	
	@Autowired
    private FeedRepository feedRepository;
	
	@GetMapping(path = "/rest/news")
	public Page<Feed> news(@RequestParam(name = "page", required = false) Integer page, @RequestParam(name = "category", required = false) Category category, HttpServletRequest request) {
		if(page == null) {
			page = 0;
		}
		

		log.info("f ip: {}, r ip: {}", request.getHeader("X-FORWARDED-FOR"), request.getRemoteAddr());

		log.info("Load news in page {} and category {}", page, category);
		
		Pageable pageable = PageRequest.of(page, 100);
		Page<Feed> result;
		if(category == null) {
			result = feedRepository.findByOrderByPublishedDesc(pageable);
		} else {
			result = feedRepository.findByCategoryOrderByPublishedDesc(category, pageable);
		}
		return result;
	}
	
	@GetMapping(path = "/rest/unreadcount")
	public long getUnreadCount(@RequestParam(name = "datetime") String dateTime) {
		Date date = new Date(Long.parseLong(dateTime));
		long unreadCount = feedRepository.countByCreatedGreaterThan(date);
		log.debug("UnreadCount after date " + dateToString(date) + " is " + unreadCount);
		return unreadCount;
	}
	
	private String dateToString(Date date) {
		String pattern = "dd.MM.yyyy HH:mm:ss";
		DateFormat df = new SimpleDateFormat(pattern);
		String dateAsString = df.format(date);
		return dateAsString;
	}

	@GetMapping(path = "/rest/test")
	public String test() {
		return "Server time " + LocalDateTime.now().toString();
	}
}
