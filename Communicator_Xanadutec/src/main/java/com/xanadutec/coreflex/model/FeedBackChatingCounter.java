package com.xanadutec.coreflex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="FeedBackChatingCounter")
public class FeedBackChatingCounter {

	@Id @GeneratedValue
	@Column(name="FeedBack_Counter")
	int feedbackCounter;
	
	@Column(name="SoNumber")
	String so_number;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userFrom",nullable=false)
	UserModel userModelFrom;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userTo",nullable=false)
	UserModel userModelTo;
		
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="feedBackType",nullable=false)
	FeedBackTypeTable feedBackType;
	
	public UserModel getUserModelFrom() {
		return userModelFrom;
	}

	public void setUserModelFrom(UserModel userModelFrom) {
		this.userModelFrom = userModelFrom;
	}

	public UserModel getUserModelTo() {
		return userModelTo;
	}

	public void setUserModelTo(UserModel userModelTo) {
		this.userModelTo = userModelTo;
	}

	public FeedBackTypeTable getFeedBackType() {
		return feedBackType;
	}

	public void setFeedBackType(FeedBackTypeTable feedBackType) {
		this.feedBackType = feedBackType;
	}

	@Column(name="count")
	int count;

	public int getFeedbackCounter() {
		return feedbackCounter;
	}

	public void setFeedbackCounter(int feedbackCounter) {
		this.feedbackCounter = feedbackCounter;
	}

	public String getSo_number() {
		return so_number;
	}

	public void setSo_number(String so_number) {
		this.so_number = so_number;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
	
	
	
	
}
