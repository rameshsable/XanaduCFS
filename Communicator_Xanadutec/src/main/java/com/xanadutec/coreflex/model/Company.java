package com.xanadutec.coreflex.model;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="Company")
public class Company {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	@Column(name = "CompanyId", nullable = false)
	private Integer companyId;
	
	
	@Column(name = "Company_Name")
	private String companyName;  
	
	@Column(name = "Company_Contact")
	private String companyContact;
	
	@Column(name = "Company_Address")
	private String companyAddress;
	
	@Column(name = "Company_Email")
	private String companyEmail;
		
	   
/*	 @OneToOne(fetch = FetchType.EAGER)
	@JoinColumn(name="userID")
		UserModel userID;




	public UserModel getUserID() {
	return userID;
}


public void setUserID(UserModel userID) {
	this.userID = userID;
}
*/

	public Integer getCompanyId() {
		return companyId;
	}


	public void setCompanyId(Integer companyId) {
		this.companyId = companyId;
	}


	public String getCompanyName() {
		return companyName;
	}


	public void setCompanyName(String companyName) {
		this.companyName = companyName;
	}


	public String getCompanyContact() {
		return companyContact;
	}


	public void setCompanyContact(String companyContact) {
		this.companyContact = companyContact;
	}


	public String getCompanyAddress() {
		return companyAddress;
	}


	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}


	public String getCompanyEmail() {
		return companyEmail;
	}


	public void setCompanyEmail(String companyEmail) {
		this.companyEmail = companyEmail;
	}
	
	
	
}
