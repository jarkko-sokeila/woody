package com.sokeila.woody.backend.services;

import java.util.Collection;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import com.sokeila.woody.backend.entity.Category;
import com.sokeila.woody.backend.entity.Feed;
import com.sokeila.woody.backend.entity.RssSource;

public interface FeedRepository extends PagingAndSortingRepository<Feed, Long> {
	public Feed findByGuid(String guid);
	public boolean existsByGuid(String guid);
	public Collection<Feed> findByTitle(String title);
	public boolean existsByRssSourceAndTitle(RssSource rssSource, String title);
	public Collection<Feed> findByOrderByPublishedDesc();
	
	public Page<Feed> findByOrderByPublishedDesc(Pageable pageable);
	public Page<Feed> findByCategoryOrderByPublishedDesc(Category category,Pageable pageable);
}
