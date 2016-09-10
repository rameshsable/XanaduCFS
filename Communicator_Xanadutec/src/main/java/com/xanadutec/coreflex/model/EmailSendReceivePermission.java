package com.xanadutec.coreflex.model;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
@Entity(name="EmailSendReceivePermission")
public class EmailSendReceivePermission {


	@Id
	@GeneratedValue
	@Column(name="ID")
	int id;
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_Id")
	UserModel userModel;
	

	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="CompanyId")
	private Company company;

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


	public Company getCompany() {
		return company;
	}


	public void setCompany(Company company) {
		this.company = company;
	}
}
