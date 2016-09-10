package com.xanadutec.coreflex.company;

import java.util.List;

import com.xanadutec.coreflex.model.Company;

public interface CompanyDao {

	
	
	
	
	
	public void saveCompany(Company company );
	public List<Company> getListOfCompany();
	public void updateCompany(Company company);
	public Company getCompanyById(String id);
}
