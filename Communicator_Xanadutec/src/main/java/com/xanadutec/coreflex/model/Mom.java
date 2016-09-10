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
import javax.persistence.PersistenceContext;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.log4j.net.SyslogAppender;

@Entity
@Table(name="mom")
public class Mom {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
    @Column(name="mom_date")
    @Temporal(TemporalType.DATE)
	Date date;
    
    @Column(name="Attendee",length=300)
	String attendee;
    
	@Column(name="Subject",length=300)
	String subject;
	
	@Column(name="Keypoints",length=20000)
	String keypoints;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userFrom",nullable=false)
	UserModel userModelFrom;
	
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userTo",nullable=false)
	UserModel userModelTo;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userFeedBack",nullable=false)
	UserModel userfeedback;
	
	 @Column(name="fileDateTime")
	 private Date dateTime;
	
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
		System.out.println("welcome inside user model to");
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	
	
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getAttendee() {
		return attendee;
	}
	public void setAttendee(String attendee) {
		this.attendee = attendee;
	}
	public String getSubject() {
		return subject;
	}
	public void setSubject(String subject) {
		this.subject = subject;
	}
	public String getKeypoints() {
		return keypoints;
	}
	public void setKeypoints(String keypoints) {
		this.keypoints = keypoints;
	}
	public Date getDateTime() {
		return dateTime;
	}
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}
	
	

}
