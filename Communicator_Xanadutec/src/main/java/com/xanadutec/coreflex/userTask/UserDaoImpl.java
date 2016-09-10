package com.xanadutec.coreflex.userTask;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.EmailSendReceivePermission;
import com.xanadutec.coreflex.model.Mom;
import com.xanadutec.coreflex.model.RoleModel;
import com.xanadutec.coreflex.model.UserLastVisitDateTime;
import com.xanadutec.coreflex.model.UserModel;
import com.xanadutec.coreflex.model.UserPermission;


@Repository("userDao")
public class UserDaoImpl implements UserDao {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private UserService userService;

	@Override
	public void addUser(UserModel userModel) {
	
		System.out.println(userModel.getCompany().getCompanyName()+"inside the add user ");
		
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(userModel);
		tx.commit();
		session.close();
		
	}

	@Override
	public List<UserModel> listUser() {
		Session session = sessionFactory.openSession();
		Criteria criteria=session.createCriteria(UserModel.class);
		List<UserModel> listUser=criteria.list();
		session.close();
		return listUser;
	}

	@Override
	public void deleteUser(int userId) {
		Session session = sessionFactory.openSession();
		Query query=session.createQuery("DELETE FROM UserModel WHERE userId=:userId");
		query.setParameter("userId", userId);
		query.executeUpdate();
		session.close();

	}

	@Override
	public RoleModel userRoll(int userRoll) {
		Session session = sessionFactory.openSession();
		Criteria criteria=session.createCriteria(RoleModel.class);
		criteria.add(Restrictions.eq("id", userRoll));
		RoleModel userRole=(RoleModel)criteria.uniqueResult();
		session.close();
		return userRole;
	}

	@Override
	public List<Company> getAllModeratorClients() {
		Session session = sessionFactory.openSession();
		RoleModel roleModel =new RoleModel();
		roleModel.setId(2);
		Criteria criteria=session.createCriteria(Company.class);
		List<Company> userlist=criteria.list();
		
		for(Company model:userlist){
			if(model.getCompanyName().equalsIgnoreCase("Coreflex")){
				userlist.remove(model);
				break;
			}
			
		}
		session.close();
		return userlist;
	}

	@Override
	public List<UserModel> getAllCoreFlexModerators() {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
	try{
		RoleModel roleModel =new RoleModel();
		roleModel.setId(2);
		Company company=new Company();
		company.setCompanyId(1);
		Criteria criteria=session.createCriteria(UserModel.class);
		criteria.add(Restrictions.eq("company", company));
		criteria.add(Restrictions.eq("userRoll",roleModel));
		List<UserModel> userlist=criteria.list();
		return userlist;
	}catch(Exception e){
		e.printStackTrace();
	}finally {
	session.close();	
	}
	
	return null;
	}

	@Override
	public List<UserPermission> getListOfUserPermission(int userId) {
		Session session = sessionFactory.openSession();
		try{
			UserModel userModel =new  UserModel();
			userModel.setUserId(userId);
			Criteria criteria=session.createCriteria(UserPermission.class);
			criteria.add(Restrictions.eq("userModel", userModel));
			List<UserPermission> userlist=criteria.list();
			return userlist;
		}catch(Exception e){
			e.printStackTrace();
		}finally {
		session.close();	
		}
		return null;
	}

	@Override
	public void deleteUSerPermission(int userId) {
		Session session = sessionFactory.openSession();
		try{
			UserModel userModel =new UserModel();
			userModel.setUserId(userId); 
			String hql = "delete from UserPermission where userModel= :userModel";  
			Query query=session.createQuery(hql);
			query.setParameter("userModel", userModel);
			query.executeUpdate(); 
		}catch(Exception exception){
			
		}finally {
			session.close();
		}
		
	}

	@Override
	public void saveUserpermission(UserPermission userPermission) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(userPermission);
		tx.commit();
		session.close();
	}

	@Override
	public void updateUser(UserModel user) {
		// TODO Auto-generated method stub
		
		Session session=sessionFactory.openSession();
		Transaction tx=session.beginTransaction();
		session.saveOrUpdate(user);
		tx.commit();
		session.close();
		
	}

	@Override
	public List<UserModel> getAllAdminUser() {
		// TODO Auto-generated method stub
		
		Session session = sessionFactory.openSession();
		Criteria criteria=session.createCriteria(UserModel.class);
         RoleModel  roll=new RoleModel();
        roll.setId(1);
		criteria.add(Restrictions.eq("userRoll", roll));
		List<UserModel>adminList=criteria.list();
		
		return adminList;
	}

	@Override
	public List<UserModel> getListOfUserByCompanyId(String companyId) {
		
		Company company =new Company();
		company.setCompanyId(Integer.parseInt(companyId));
		Session session = sessionFactory.openSession();
		Criteria criteria=session.createCriteria(UserModel.class);
		criteria.add(Restrictions.eq("company", company));
		List<UserModel>userModels =criteria.list();
		session.close();
		return userModels;
	}

	@Override
	public void deleteCompanyUSerListFromEmailPermission(Company company) {
		Session session = sessionFactory.openSession();
		try{
			
			
			
			Transaction tx = session.beginTransaction();
			String hql = "delete from EmailSendReceivePermission where company= :company";  
			Query query=session.createQuery(hql);
			query.setParameter("company", company);
			query.executeUpdate(); 
			tx.commit();
		}catch(Exception exception){
			exception.printStackTrace();
		}finally {
			session.close();
		}
	}

	@Override
	public void saveEmailSendReceivePermission(EmailSendReceivePermission emailSendReceivePermission) {
		// TODO Auto-generated method stub
		
		
		System.out.println("called save email permission function");
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.saveOrUpdate(emailSendReceivePermission);
		tx.commit();
		session.close();
	}

	@Override
	public void SaveUserLastVisitedTime(int model, String type) {
		// TODO Auto-generated method stub
		
		UserModel UserModel=new UserModel();
		UserModel.setUserId(model);
		UserLastVisitDateTime visit=new UserLastVisitDateTime();
		//visit.setUserModel(UserModel);
		int id=UserModel.getUserId();
		visit=userService.getLastVisitedObjDetails(id);
		if(visit!=null)
		{
			//visit.setUserModel(UserModel);//at firt time it will return null user 
			if(type.equalsIgnoreCase("feedback")){
				
				visit.setFeedCom(new Date());
			}
			if(type.equalsIgnoreCase("Mom"))
			{
				visit.setMom(new Date());
			}
			if(type.equalsIgnoreCase("Report"))
			{

				visit.setReport(new Date());
			}
			if(type.equalsIgnoreCase("Document"))
			{
				visit.setDocument(new Date());
			}
			
			Session session = sessionFactory.openSession();
			Transaction t=session.beginTransaction();
			session.saveOrUpdate(visit);
			t.commit();
			session.close();
		}
		else
		{
			
			UserModel UserModel1=userService.getUserById(model);
			
			UserLastVisitDateTime visit1=new UserLastVisitDateTime();
			
						visit1.setUserModel(UserModel1);
					if(type.equalsIgnoreCase("feedback")){
									
									visit1.setFeedCom(new Date());
								}
								if(type.equalsIgnoreCase("Mom"))
								{
									visit1.setMom(new Date());
								}
								if(type.equalsIgnoreCase("Report"))
								{
					
									visit1.setReport(new Date());
								}
								if(type.equalsIgnoreCase("Document"))
								{
									visit1.setDocument(new Date());
								}
								
								Session session1 = sessionFactory.openSession();
								Transaction t=session1.beginTransaction();
								session1.saveOrUpdate(visit1);
								t.commit();
								session1.close();
								
			
		}
		
		
	
		
		
		
		}

	@Override
	public UserLastVisitDateTime getLastVisitedObjDetails(int userid) {
		
		UserLastVisitDateTime last=new UserLastVisitDateTime();
		UserModel model=new UserModel();
		model.setUserId(userid);
		Session session=sessionFactory.openSession();
		Criteria criteria=session.createCriteria(UserLastVisitDateTime.class);
		criteria.add(Restrictions.eq("userModel",model));
		UserLastVisitDateTime	 mom1=(UserLastVisitDateTime) criteria.uniqueResult();
		
		session.close();
		return mom1;
	}

	@Override
	public UserModel getUserById(int id) {
		
		
		Session session=sessionFactory.openSession();
		Criteria criteria=session.createCriteria(UserModel.class);
		criteria.add(Restrictions.eq("userId",id));
		UserModel user=(UserModel) criteria.uniqueResult();
		session.close();
		return user;
	}
	

}
