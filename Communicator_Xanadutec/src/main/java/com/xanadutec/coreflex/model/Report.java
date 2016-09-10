package com.xanadutec.coreflex.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.beans.factory.annotation.Configurable;

@Entity
@Table(name="Report")
public class Report {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	@Column(name="name")
	private  String name ;
	
	@Column(name="fileDate")
	@Temporal(TemporalType.DATE)
	private Date date;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userFrom",nullable=false)
	UserModel userModelFrom;
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userTo",nullable=false)
	UserModel userModelTo;
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userFeedBack",nullable=false)
	UserModel userfeedback;
	@Column(name="Description")
    String description;
	
	 @Column(name="fileDateTime")
	 private Date dateTime;
	 
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public UserModel getUserfeedback() {
		return userfeedback;
	}
	public void setUserfeedback(UserModel userfeedback) {
		this.userfeedback = userfeedback;
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
	
	

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	

}
