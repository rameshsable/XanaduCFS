package com.xanadutec.coreflex.FileUploadDownload;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.FtpInfo;
import com.xanadutec.coreflex.model.Operator;
import com.xanadutec.coreflex.model.RoleModel;
import com.xanadutec.coreflex.model.UserModel;


@Repository("FileUploadDao")
public class FileUploadDaoImpl implements FileUploadDao{



	@Autowired
	private SessionFactory sessionFactory;
	
	public List<Operator> getListOfActiveOperators(){
		Session session = sessionFactory.openSession();
		Criteria criteria =session.createCriteria(Operator.class);
		criteria.add(Restrictions.eq("status", "Active"));
		List<Operator> operators=criteria.list();
		session.close();
		return operators;
	}

	@Override
	public void saveFTP(FtpInfo ftpInfo) {
		Session session = sessionFactory.openSession();
		Transaction transaction =session.beginTransaction();
		session.save(ftpInfo);
		transaction.commit();
		session.close();
		
	}

	@Override
	public FtpInfo getFtpInfo() {
		Session session = sessionFactory.openSession();
		Criteria criteria =session.createCriteria(FtpInfo.class);
		List<FtpInfo> ftpInfos=criteria.list();
		session.close();
		return ftpInfos.get(0);
	}

}
