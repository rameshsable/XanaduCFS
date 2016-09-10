package com.xanadutec.coreflex.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity(name="FeedBackSonumberSortingByDate")

public class FeedBackSonumberSortingByDate {
	@Id @GeneratedValue
	@Column(name="feedBackSonumberSorting_id")
	int feedBackSonumberSortingId;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="feedBackType",nullable=false)
	FeedBackTypeTable feedBackType;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="feedback_Id",nullable=false)
	Feedback feedback;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="feedback_close_Id")
	Feedback feedback_close;
	
	@Column(name="SO_Number",length=200)
	String soNumber;
	
	
	@Column(name="date")
	Date fdate;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userFrom",nullable=false)
	UserModel userModelFrom;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userTo",nullable=false)
	UserModel userModelTo;
	
	public Date getFdate() {
		return fdate;
	}
	public int getFeedBackSonumberSortingId() {
		return feedBackSonumberSortingId;
	}
	public void setFeedBackSonumberSortingId(int feedBackSonumberSortingId) {
		this.feedBackSonumberSortingId = feedBackSonumberSortingId;
	}
	public FeedBackTypeTable getFeedBackType() {
		return feedBackType;
	}
	public void setFeedBackType(FeedBackTypeTable feedBackType) {
		this.feedBackType = feedBackType;
	}
	public String getSoNumber() {
		return soNumber;
	}
	public void setSoNumber(String soNumber) {
		this.soNumber = soNumber;
	}
	public void setFdate(Date fdate) {
		this.fdate = fdate;
	}
	public Feedback getFeedback() {
		return feedback;
	}
	public void setFeedback(Feedback feedback) {
		this.feedback = feedback;
	}
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
	public Feedback getFeedback_close() {
		return feedback_close;
	}
	public void setFeedback_close(Feedback feedback_close) {
		this.feedback_close = feedback_close;
	}
	
	
	
	
}

