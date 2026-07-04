package com.sokeila.woody.backend.services;

import com.sokeila.woody.backend.entity.Category;
import com.sokeila.woody.backend.entity.Feed;
import com.sokeila.woody.backend.entity.RssSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Collection;
import java.util.Date;

public interface FeedRepository extends PagingAndSortingRepository<Feed, Long>, CrudRepository<Feed, Long> {
	Feed findByGuid(String guid);
	boolean existsByGuid(String guid);
	Collection<Feed> findByTitle(String title);
	boolean existsByRssSourceAndTitle(RssSource rssSource, String title);
	Collection<Feed> findByOrderByPublishedDesc();
	
	Page<Feed> findByOrderByPublishedDesc(Pageable pageable);
	Page<Feed> findByCategoryOrderByPublishedDesc(Category category, Pageable pageable);
	
	long countByCreatedGreaterThan(Date date);
	long countByCategoryAndCreatedGreaterThan(Category category, Date date);

	long deleteByPublishedBefore(Date date);
}
