package com.xanadutec.coreflex.company;

import java.util.List;

import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.UserPermission;

public interface CompanyService {

	 public void saveCompany(Company company);
	public List<Company> getListOfCompany();
	public void updateCompany(Company company);
	public Company getCompanyById(String id);
	
	

}
