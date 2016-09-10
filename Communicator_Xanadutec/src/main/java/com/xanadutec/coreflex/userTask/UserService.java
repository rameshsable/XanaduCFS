package com.xanadutec.coreflex.userTask;

import java.util.List;

import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.EmailSendReceivePermission;
import com.xanadutec.coreflex.model.RoleModel;
import com.xanadutec.coreflex.model.UserLastVisitDateTime;
import com.xanadutec.coreflex.model.UserModel;
import com.xanadutec.coreflex.model.UserPermission;



public interface UserService {


	public void addUser(UserModel userModel);
	public List<UserModel> listUser();
	public void deleteUser(int userId);
	public RoleModel userRoll(int userRoll);
	public List<Company> getAllModeratorClients();
	public List<UserModel>  getAllCoreFlexModerators();
	public List<UserPermission> getListOfUserPermission(int userId);
	public void deleteUSerPermission(int userId);
	public void saveUserpermission(UserPermission  userPermission);
	public void updateUser(UserModel user);
	public List<UserModel>getAllAdminUser();
	public List<UserModel>getListOfUserByCompanyId(String companyId);
	public void deleteCompanyUSerListFromEmailPermission(Company company);
	public void saveEmailSendReceivePermission(EmailSendReceivePermission emailSendReceivePermission);
	public void SaveUserLastVisitedTime(int model,String type);
	public UserLastVisitDateTime getLastVisitedObjDetails(int userid);
	public UserModel getUserById(int id);
	
	
}
