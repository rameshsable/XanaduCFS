package com.xanadutec.coreflex.login;

import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.Documents;
import com.xanadutec.coreflex.model.UserModel;

@Service("loginService")
public class LoginServiceImpl implements LoginService{

	@Autowired
	private LoginDao loginDao;
	@Override
	public UserDetails loadUserByUsername(String username) {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public UserModel getUserByUnamePassword(String userName, String password) {
		return loginDao.getUserByUnamePassword(userName, password);
	}
	@Override
	public void addUser(String userName, String password) {
		 loginDao.addUser(userName, password);
	}
	@Override
	public UserModel getUserByUname(String userName) {
		return loginDao.getUserByUname(userName);
	}
	@Override
	public UserModel getUserById(int id) {
		// TODO Auto-generated method stub
		return loginDao.getUserById(id);
	}
	@Override
	public UserModel getCoreflexAdmin() {
		// TODO Auto-generated method stub
		return loginDao.getCoreflexAdmin();
	}
	@Override
	public UserModel getclientCompanyAdminById(int companyId){
		return loginDao.getclientCompanyAdminById(companyId);
		
	}
	@Override
	public UserModel getSsiAdmin() {
		// TODO Auto-generated method stub
		return loginDao.getSsiAdmin();
	}
	@Override
	public List<UserModel> getListOfUserIfUnamePasswordIsValid(String userName, String password) {
		// TODO Auto-generated method stub
		return loginDao.getListOfUserIfUnamePasswordIsValid(userName, password);
	}
	@Override
	public List<UserModel> getListOfUserAccordingToCompany(Integer companyId) {
		// TODO Auto-generated method stub
		return loginDao.getListOfUserAccordingToCompany(companyId);
	}
	@Override
	public List<UserModel> getListOfCoreflexUser() {
		// TODO Auto-generated method stub
		return loginDao.getListOfCoreflexUser();
	}
	@Override
	public List<UserModel> getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(
			Company company) {
		// TODO Auto-generated method stub
		return loginDao.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(company);
	}


}
