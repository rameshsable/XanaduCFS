package com.xanadutec.coreflex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="ServerInfo")
public class ServerInfo {
	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="serverId")
	int serverId;
	
	@Column(name="mail_host")
	String mail_host;
	
	@Column(name="mail_port")
	String mail_port;
	
	@Column(name="mail_serverName")
	String mail_serverName;
	
	@Column(name="mail_password")
	String mail_password;
	
	@Column(name="msgName")
	String msgName;

	public int getServerId() {
		return serverId;
	}

	public void setServerId(int serverId) {
		this.serverId = serverId;
	}

	public String getMail_host() {
		return mail_host;
	}

	public void setMail_host(String mail_host) {
		this.mail_host = mail_host;
	}

	public String getMail_port() {
		return mail_port;
	}

	public void setMail_port(String mail_port) {
		this.mail_port = mail_port;
	}

	public String getMail_serverName() {
		return mail_serverName;
	}

	public void setMail_serverName(String mail_serverName) {
		this.mail_serverName = mail_serverName;
	}

	public String getMail_password() {
		return mail_password;
	}

	public void setMail_password(String mail_password) {
		this.mail_password = mail_password;
	}

	public String getMsgName() {
		return msgName;
	}

	public void setMsgName(String msgName) {
		this.msgName = msgName;
	}
	
	
	


}
