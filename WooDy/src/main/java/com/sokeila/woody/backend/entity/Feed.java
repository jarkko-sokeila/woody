package com.sokeila.woody.backend.entity;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

import org.hibernate.annotations.Formula;

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
	
	@Formula("(select count(*) from J_FEED_IP_HASH j where j.C_FEED_ID = C_ID)")
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
	
	@Override
	public String toString() {
		return "Feed [id=" + id + ", title=" + title + ", description=" + description + ", link=" + link + ", guid="
				+ guid + ", published=" + published + ", category=" + category + ", subCategory=" + subCategory + "]";
	}
}
