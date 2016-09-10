package com.xanadutec.coreflex.userTask;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.EmailSendReceivePermission;
import com.xanadutec.coreflex.model.RoleModel;
import com.xanadutec.coreflex.model.UserLastVisitDateTime;
import com.xanadutec.coreflex.model.UserModel;
import com.xanadutec.coreflex.model.UserPermission;



@Service("userService")
public class UserServiceImpl implements UserService {

	@Autowired
	private UserDao userDao;
	@Override
	public void addUser(UserModel userModel) {
		userDao.addUser(userModel);
		
	}

	@Override
	public List<UserModel> listUser() {

		return userDao.listUser();
	}

	@Override
	public void deleteUser(int userId) {
		userDao.deleteUser(userId);
	}

	@Override
	public RoleModel userRoll(int userRoll) {
		// TODO Auto-generated method stub
		return userDao.userRoll(userRoll);
	}

	@Override
	public List<Company> getAllModeratorClients() {
		// TODO Auto-generated method stub
		return userDao.getAllModeratorClients();
	}

	@Override
	public List<UserModel> getAllCoreFlexModerators() {
		// TODO Auto-generated method stub
		return userDao.getAllCoreFlexModerators();
	}

	@Override
	public List<UserPermission> getListOfUserPermission(int userId) {
		// TODO Auto-generated method stub
		return userDao.getListOfUserPermission(userId);
	}

	@Override
	public void deleteUSerPermission(int userId) {
		userDao.deleteUSerPermission(userId);
		
	}

	@Override
	public void saveUserpermission(UserPermission userPermission) {
		userDao.saveUserpermission(userPermission);
		
	}

	@Override
	public void updateUser(UserModel user) {
		userDao.updateUser(user);
		
		
	}

	@Override
	public List<UserModel> getAllAdminUser() {
		// TODO Auto-generated method stub
		return userDao.getAllAdminUser();
	}

	@Override
	public List<UserModel> getListOfUserByCompanyId(String companyId) {
		// TODO Auto-generated method stub
		return userDao.getListOfUserByCompanyId(companyId);
	}

	@Override
	public void deleteCompanyUSerListFromEmailPermission(Company company) {
		// TODO Auto-generated method stub
		userDao.deleteCompanyUSerListFromEmailPermission(company);
	}

	@Override
	public void saveEmailSendReceivePermission(EmailSendReceivePermission emailSendReceivePermission) {
		// TODO Auto-generated method stub
		userDao.saveEmailSendReceivePermission(emailSendReceivePermission);
	}

	@Override
	public void SaveUserLastVisitedTime(int model, String type) {
		// TODO Auto-generated method stub
		userDao.SaveUserLastVisitedTime(model, type);
		
	}

	@Override
	public UserLastVisitDateTime getLastVisitedObjDetails(int userid) {
		// TODO Auto-generated method stub
		return userDao.getLastVisitedObjDetails(userid);
	}

	@Override
	public UserModel getUserById(int id) {
		// TODO Auto-generated method stub
		return userDao.getUserById(id);
	}
	

}
