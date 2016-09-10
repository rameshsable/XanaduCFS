package com.xanadutec.coreflex.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="UserLastVisitDateTime")
public class UserLastVisitDateTime {

	@Id
	@GeneratedValue
	int id;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="user",nullable=false)
	UserModel userModel;
	@Column(name="feedcom")
	Date feedCom;
	@Column(name="Mom")
	Date mom;
	@Column(name="Report")
	Date report;
	@Column(name="Document")
	Date document;
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public UserModel getUserModel() {
		return userModel;
	}
	public void setUserModel(UserModel userModel) {
		this.userModel = userModel;
	}
	public Date getFeedCom() {
		return feedCom;
	}
	public void setFeedCom(Date feedCom) {
		this.feedCom = feedCom;
	}
	public Date getMom() {
		return mom;
	}
	public void setMom(Date mom) {
		this.mom = mom;
	}
	public Date getReport() {
		return report;
	}
	public void setReport(Date report) {
		this.report = report;
	}
	public Date getDocument() {
		return document;
	}
	public void setDocument(Date document) {
		this.document = document;
	}
	
	
	
}
