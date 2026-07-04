package com.sokeila.woody.backend.entity;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "T_IP_HASH")
public class IPHash {
	@Id
	@Column(name = "C_ID")
    @GeneratedValue(strategy=GenerationType.IDENTITY)
	private long id;

	@Column(name = "C_IP_HASH", unique = true)
	private String ipHash;
	
	@ManyToMany(mappedBy="IPClicks")
    private Set<Feed> feeds;

	public IPHash() {
	}
	
	public IPHash(String ipHash) {
		this.ipHash = ipHash;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getIpHash() {
		return ipHash;
	}

	public void setIpHash(String ipHash) {
		this.ipHash = ipHash;
	}

	public Set<Feed> getFeeds() {
		return feeds;
	}

	public void setFeeds(Set<Feed> feeds) {
		this.feeds = feeds;
	}
}
