package com.xanadutec.coreflex.company;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.Mom;
import com.xanadutec.coreflex.model.UserModel;


@Service("CompanyService")
public class CompanyServiceImpl implements CompanyService{

	@Autowired 
	private CompanyDao companyDao;
	@Override
	public void saveCompany(Company company ) {
		companyDao.saveCompany(company);
	}
	public List<Company> getListOfCompany(){
		return companyDao.getListOfCompany();
}
	@Override
	public void updateCompany(Company company) {
		 companyDao.updateCompany(company);
		
	}
	@Override
	public Company getCompanyById(String id) {
		// TODO Auto-generated method stub
		return companyDao.getCompanyById(id);
	}
	

}
