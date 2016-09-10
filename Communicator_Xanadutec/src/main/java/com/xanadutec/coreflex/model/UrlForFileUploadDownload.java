package com.xanadutec.coreflex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity(name="UrlForFileUploadDownload")
public class UrlForFileUploadDownload {
	@Id 
	@GeneratedValue
	@Column(name="url_id")
	int urlId;
	
	@Column(name="url_NAME",length=2000)
	String urlName;

	public int getUrlId() {
		return urlId;
	}

	public void setUrlId(int urlId) {
		this.urlId = urlId;
	}

	public String getUrlName() {
		return urlName;
	}

	public void setUrlName(String urlName) {
		this.urlName = urlName;
	}
	
	
}

