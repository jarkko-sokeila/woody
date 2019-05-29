package com.sokeila.woody.backend.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

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
