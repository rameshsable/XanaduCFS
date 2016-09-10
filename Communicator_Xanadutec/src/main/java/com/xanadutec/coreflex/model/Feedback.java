package com.xanadutec.coreflex.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

@Entity(name="Feedback")
public class Feedback implements Serializable {
	@Id @GeneratedValue
	@Column(name="Feedback_id")
	int feedbackId;
	
	@Column(name="Feedback_text",length=3000)
	String feedbackText;
	
	@Column(name="SO_Number",length=200)
	String soNumber;
	
	@Column(name="dateoffeed")
	Date fdate;
	public Date getFdate() {
		return fdate;
	}

	public void setFdate(Date fdate) {
		this.fdate = fdate;
	}

	@Column(name="EmailSubject",length=2000)
	String emailSubject;
	
	@Column(name="Attachments",length=2000)
	String attachment;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userFrom",nullable=false)
	UserModel userModelFrom;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userTo",nullable=false)
	UserModel userModelTo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userFeedBack",nullable=false)
	UserModel userfeedback;
	
	@Cascade(value={CascadeType.ALL, CascadeType.PERSIST})
	@ManyToOne(fetch = FetchType.EAGER )
	@JoinColumn(name="operatorId",nullable=true)
	Operator operator;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="statusId",nullable=false)
	Status status;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="feedBackType",nullable=false)
	FeedBackTypeTable feedBackType;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="feedbackCounter",nullable=false)
	FeedBackChatingCounter counter;

	public int getFeedbackId() {
		return feedbackId;
	}

	public void setFeedbackId(int feedbackId) {
		this.feedbackId = feedbackId;
	}

	public String getFeedbackText() {
		return feedbackText;
	}

	public void setFeedbackText(String feedbackText) {
		this.feedbackText = feedbackText;
	}

	public String getSoNumber() {
		return soNumber;
	}

	public void setSoNumber(String soNumber) {
		this.soNumber = soNumber;
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

	public Operator getOperator() {
		return operator;
	}

	public void setOperator(Operator operator) {
		this.operator = operator;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public FeedBackTypeTable getFeedBackType() {
		return feedBackType;
	}

	public void setFeedBackType(FeedBackTypeTable feedBackType) {
		this.feedBackType = feedBackType;
	}

	public FeedBackChatingCounter getCounter() {
		return counter;
	}

	public void setCounter(FeedBackChatingCounter counter) {
		this.counter = counter;
	}

	public UserModel getUserfeedback() {
		return userfeedback;
	}

	public void setUserfeedback(UserModel userfeedback) {
		this.userfeedback = userfeedback;
	}

	public String getEmailSubject() {
		return emailSubject;
	}

	public void setEmailSubject(String emailSubject) {
		this.emailSubject = emailSubject;
	}

	public String getAttachment() {
		return attachment;
	}

	public void setAttachment(String attachment) {
		this.attachment = attachment;
	} 
	
	
	
	
}
