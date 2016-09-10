package com.xanadutec.coreflex.company;

import java.util.List;

import com.xanadutec.coreflex.model.Company;

public interface CompanyDao {

	
	
	//this is the change in company dao
	
	
	public void saveCompany(Company company );
	public List<Company> getListOfCompany();
	public void updateCompany(Company company);
	public Company getCompanyById(String id);
}
