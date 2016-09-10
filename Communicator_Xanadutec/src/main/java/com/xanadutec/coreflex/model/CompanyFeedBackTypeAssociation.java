package com.xanadutec.coreflex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity(name="CompanyAndFeedBackTypeAssociation")
public class CompanyFeedBackTypeAssociation {


	@Id
	@GeneratedValue
	@Column(name="ID")
	int id;
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "feedBackType")
	FeedBackTypeTable feedBackType;
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="CompanyId")
	private Company company;

	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}




	public FeedBackTypeTable getFeedBackType() {
		return feedBackType;
	}


	public void setFeedBackType(FeedBackTypeTable feedBackType) {
		this.feedBackType = feedBackType;
	}


	public Company getCompany() {
		return company;
	}


	public void setCompany(Company company) {
		this.company = company;
	}
}
