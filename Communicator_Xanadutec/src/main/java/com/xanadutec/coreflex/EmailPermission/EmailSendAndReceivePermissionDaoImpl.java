package com.xanadutec.coreflex.EmailPermission;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.EmailSendReceivePermission;
import com.xanadutec.coreflex.model.FeedBackTypeTable;
import com.xanadutec.coreflex.model.ServerInfo;
import com.xanadutec.coreflex.model.UserModel;
@Repository("EmailSendAndReceivePermissionDao")
public class EmailSendAndReceivePermissionDaoImpl implements EmailSendAndReceivePermissionDao {

	@Autowired
	private SessionFactory sessionFactory;
	public List<EmailSendReceivePermission> getEmailPermissionUserListAccordingToCompany(String companyId) {
		Session session = sessionFactory.openSession();
		Company company= new Company();
		company.setCompanyId(Integer.parseInt(companyId));
		List<EmailSendReceivePermission>  emailSendReceivePermissions=null;
		try {
			Criteria criteria = session.createCriteria(EmailSendReceivePermission.class);
			criteria.add(Restrictions.eq("company", company));
			
			 emailSendReceivePermissions= criteria.list();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			session.close();
		}
		return emailSendReceivePermissions;
	}
	@Override
	public void SaveEmailPermissionByUserId(int id,int comid) {
		// TODO Auto-generated method stub
		
		
		System.out.println("inside save email DaoImpl");
		UserModel user=new UserModel();
		user.setUserId(id);
		Company company=new Company();
		company.setCompanyId(comid);
		EmailSendReceivePermission email=new EmailSendReceivePermission();
		Session session = sessionFactory.openSession();
		Transaction t=session.beginTransaction();
		
		email.setCompany(company);
		email.setUserModel(user);
		session.saveOrUpdate(email);
		t.commit();
		session.close();
		
	}
	
	@Override
	public ServerInfo getServerInfo() {
		org.hibernate.Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(ServerInfo.class);
		List<ServerInfo> ftpInfos = criteria.list();
		session.close();
		return ftpInfos.get(0);
	}
	
	
}
