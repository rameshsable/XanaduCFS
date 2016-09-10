package com.xanadutec.coreflex.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="FTP")
public class FtpInfo {

	
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(name="id")
	private int id;
	
	
	@Column(name="serverName")
	private  String serverName ;


	@Column(name="User_Name")
	private  String userName ;
	
	
	
	@Column(name="Port")
	private  int  port;
	
	
	
	@Column(name="password")
	private  String password;
	
	
	
	@Column(name="FolderName")
	private  String folderName ;



	public int getId() {
		return id;
	}



	public void setId(int id) {
		this.id = id;
	}



	public String getServerName() {
		return serverName;
	}



	public void setServerName(String serverName) {
		this.serverName = serverName;
	}



	public String getUserName() {
		return userName;
	}



	public void setUserName(String userName) {
		this.userName = userName;
	}



	public int getPort() {
		return port;
	}



	public void setPort(int port) {
		this.port = port;
	}



	public String getPassword() {
		return password;
	}



	public void setPassword(String password) {
		this.password = password;
	}



	public String getFolderName() {
		return folderName;
	}



	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}
	
	
	
	
}
