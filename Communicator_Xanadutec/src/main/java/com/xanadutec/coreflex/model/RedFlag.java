package com.xanadutec.coreflex.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
@Entity
@Table(name="RedFlag")
public class RedFlag {
	@Id
	@GeneratedValue
	int id;
	
	@Column(name="SO_Number",length=200)
	String soNumber;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="feedBackType",nullable=false)
	FeedBackTypeTable feedBackType;
	
	@Column(name="Status")
	String status;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="Company",nullable=false)
	Company company;
	
	public String getSoNumber() {
		return soNumber;
	}

	public void setSoNumber(String soNumber) {
		this.soNumber = soNumber;
	}

	public FeedBackTypeTable getFeedBackType() {
		return feedBackType;
	}

	public void setFeedBackType(FeedBackTypeTable feedBackType) {
		this.feedBackType = feedBackType;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

	
}
