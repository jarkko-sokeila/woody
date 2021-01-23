package com.sokeila.woody.backend.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

@Entity
@Table(name = "T_FEED")
public class Feed {
	@Id
	@Column(name = "C_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;
	
	@Enumerated(EnumType.STRING)
	@Column(name = "C_RSS_SOURCE")
	private RssSource rssSource;

	@Column(name = "C_TITLE")
	private String title;

	@Column(name = "C_DESCRIPTION")
	private String description;

	@Column(name = "C_LINK")
	private String link;

	@Column(name = "C_GUID", unique = true)
	private String guid;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "C_PUBLISHED")
	private Date published;

	@Enumerated(EnumType.STRING)
	@Column(name = "C_CATEGORY")
	private Category category;

	@Enumerated(EnumType.STRING)
	@Column(name = "C_SUB_CATEGORY")
	private SubCategory subCategory;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "C_CREATED")
	private Date created;
	
	@ManyToMany(fetch = FetchType.LAZY, cascade =  CascadeType.ALL)
	@JoinTable(name = "J_FEED_IP_HASH", joinColumns = @JoinColumn(name = "C_FEED_ID"), inverseJoinColumns = @JoinColumn(name = "C_IP_HASH_ID"))
	private Set<IPHash> IPClicks;
	
	@Transient
	private int clickCount;
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public RssSource getRssSource() {
		return rssSource;
	}

	public void setRssSource(RssSource rssSource) {
		this.rssSource = rssSource;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getLink() {
		return link;
	}

	public void setLink(String link) {
		this.link = link;
	}

	public String getGuid() {
		return guid;
	}

	public void setGuid(String guid) {
		this.guid = guid;
	}

	public Date getPublished() {
		return published;
	}

	public void setPublished(Date published) {
		this.published = published;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}

	public SubCategory getSubCategory() {
		return subCategory;
	}

	public void setSubCategory(SubCategory subCategory) {
		this.subCategory = subCategory;
	}
	
	public Date getCreated() {
		return created;
	}
	
	public void setCreated(Date created) {
		this.created = created;
	}
	
	public Set<IPHash> getIPClicks() {
		return IPClicks;
	}
	
	public void setIPClicks(Set<IPHash> iPClicks) {
		IPClicks = iPClicks;
	}
	
	public int getClickCount() {
		return clickCount;
	}
	
	public void setClickCount(int clickCount) {
		this.clickCount = clickCount;
	}
	
	public void addIpClick(IPHash ipHash) {
		if(IPClicks == null) {
			IPClicks = new HashSet<>();
		}
		
		IPClicks.add(ipHash);
	}
	
	public void resolveClickCount() {
		if(IPClicks != null) {
			this.clickCount = IPClicks.size();
		}
	}
	
	@Override
	public String toString() {
		return "Feed [id=" + id + ", title=" + title + ", description=" + description + ", link=" + link + ", guid="
				+ guid + ", published=" + published + ", category=" + category + ", subCategory=" + subCategory + "]";
	}
}
