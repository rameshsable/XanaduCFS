package com.xanadutec.coreflex.company;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.ModelAndView;

import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.Mom;
import com.xanadutec.coreflex.model.UserModel;


@Repository("CompanyDao")
public class CompanyDaoImpl implements CompanyDao{
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void  saveCompany(Company  company) {
		 Session session=sessionFactory.openSession();
		 session.saveOrUpdate(company);
		 System.out.println("company called");
		 session.close();
	}

	@Override
	public List<Company> getListOfCompany() {
		 Session session=sessionFactory.openSession();
		 Criteria criteria=session.createCriteria(Company.class);
		 List<Company>companies =criteria.list();
		 session.close();
	return companies;
	}

	@Override
	public void updateCompany(Company company) {
		 Session session=sessionFactory.openSession();
		 session.saveOrUpdate(company);
		 session.close();
	}

	@Override
	public Company getCompanyById(String id) {
	
		System.out.println("ramesjh2222");
		Session session=sessionFactory.openSession();
		Criteria criteria=session.createCriteria(Company.class);
		criteria.add(Restrictions.eq("companyId",Integer.parseInt(id)));
		Company bill=(Company) criteria.uniqueResult();
		session.close();
		
		return bill;
		
	}
	

	

}
