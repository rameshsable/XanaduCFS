package com.xanadutec.coreflex.login;

import java.util.List;

import org.hibernate.Query;
import org.springframework.security.core.userdetails.UserDetails;

import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.UserModel;

public interface LoginDao {

	public UserDetails loadUserByUsername(String username);

	public UserModel getUserByUnamePassword(String username, String password);

	public void addUser(String userName, String password);

	public UserModel getUserByUname(String userName);

	public UserModel getUserById(int id);

	public UserModel getCoreflexAdmin();

	public UserModel getclientCompanyAdminById(int companyId);

	public UserModel getSsiAdmin();

	public List<UserModel> getListOfUserIfUnamePasswordIsValid(String userName,
			String password);

	public List<UserModel> getListOfUserAccordingToCompany(Integer companyId);

	public List<UserModel> getListOfCoreflexUser();

	public List<UserModel> getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(
			Company company);
}
