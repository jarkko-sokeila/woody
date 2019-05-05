package com.sokeila.woody.backend.rest;

import java.time.LocalDateTime;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.sokeila.woody.backend.entity.Feed;
import com.sokeila.woody.backend.services.FeedRepository;

@RestController
public class FeedController {
	private static final Logger log = LoggerFactory.getLogger(FeedController.class);
	
	@Autowired
    private FeedRepository feedRepository;
	
	@GetMapping(path = "/rest/news")
	public Page<Feed> news(@RequestParam(name = "page", required = false) Integer page) {
		if(page == null) {
			page = 0;
		}
		
		log.info("Load news in page {}", page);
		
		Pageable pageable = PageRequest.of(page, 50);
		Page<Feed> result = feedRepository.findByOrderByPublishedDesc(pageable);
		return result;
	}
	
	@GetMapping(path = "/rest/test")
	public String test() {
		return "Server time " + LocalDateTime.now().toString();
	}
}
