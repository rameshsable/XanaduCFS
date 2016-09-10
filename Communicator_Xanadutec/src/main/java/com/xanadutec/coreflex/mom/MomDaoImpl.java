package com.xanadutec.coreflex.mom;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
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

import com.xanadutec.coreflex.dropBox.DocumentDateCompairator;
import com.xanadutec.coreflex.model.Documents;
import com.xanadutec.coreflex.model.Mom;
import com.xanadutec.coreflex.model.Report;
import com.xanadutec.coreflex.model.UserModel;


@Repository("MomDao")
public class MomDaoImpl implements MomDao{
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public int  saveMom(Mom mom_obj) {
		 Session session=sessionFactory.openSession();
		 session.save(mom_obj);
		 session.close();
		return mom_obj.getId();
		
	}

	@Override
	public List<Mom> viewMom() {
		Session session=sessionFactory.openSession();
		Criteria criteria=session.createCriteria(Mom.class);
		List<Mom>momlist=criteria.list();
		session.close();
		return momlist;
		
	}

	@Override
	public Mom getMomById(int id) {
		Session session=sessionFactory.openSession();
		/*Mom mom =new Mom();
		mom.setId(id);*/
		Criteria criteria=session.createCriteria(Mom.class);
		criteria.add(Restrictions.eq("id",id));
	Mom	 mom1=(Mom) criteria.uniqueResult();
		session.close();
		return mom1;
	}

	@Override
	public List<Mom> getListOfupdated50Mom(UserModel userFrom, UserModel userTo) {
		// TODO Auto-generated method stub


		Session session = sessionFactory.openSession();
		
		Criteria criteria = session.createCriteria(Mom.class);
		criteria.add(Restrictions.eq("userModelFrom", userFrom));
		criteria.add(Restrictions.eq("userModelTo", userTo));
		criteria.setMaxResults(50);
		List<Mom> list =criteria.list();
		
		criteria = session.createCriteria(Mom.class);
		criteria.add(Restrictions.eq("userModelFrom", userTo));
		criteria.add(Restrictions.eq("userModelTo", userFrom));
		criteria.setMaxResults(50);
		List<Mom> list1 =criteria.list();
		
		
		try{
			list.addAll(list1);
			if(list.size()>50){
				list = list.subList(0, 50);
			}
			Collections.sort(list, new MomtDateCompairator());
			
			
		}catch(Exception e){
			
		}
		session.close();
	
		return list;
	}

	@Override
	public List<Date> getDistinctMomDates() {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Mom.class);
		criteria.setProjection(Projections.distinct(Projections.property("date")));
		List<Date> lst = criteria.list();
		session.close();
		return lst;

	}

	@Override
	public Map<Date, Long> getCountOfMomListByDistinctDatesOfMomAndUserLastVisitTime(List<Date> distinctDatesOfMom,
			Date momLastVisitDate, UserModel userFrom, UserModel userTo,HttpServletRequest request) {
		
		Map<Date, Long>  mapCountOfDistinctDocumentListByDate =new HashMap<Date, Long>();

		for (Date date : distinctDatesOfMom) {
			if(momLastVisitDate!=null){
				Session session = sessionFactory.openSession();
				Criteria criteria = session.createCriteria(Mom.class);
				criteria.add(Restrictions.gt("dateTime", momLastVisitDate));
				criteria.add(Restrictions.eq("userModelFrom", userFrom));
				criteria.add(Restrictions.eq("userModelTo", userTo));
				criteria.setProjection(Projections.rowCount());
				Long l = (Long) criteria.uniqueResult();
				
				criteria = session.createCriteria(Mom.class);
				criteria.add(Restrictions.gt("dateTime", momLastVisitDate));
				criteria.add(Restrictions.eq("userModelFrom", userTo));
				criteria.add(Restrictions.eq("userModelTo", userFrom));
				criteria.setProjection(Projections.rowCount());
				Long l2 = (Long) criteria.uniqueResult();
				l=l+l2;

				if(l>0){
					mapCountOfDistinctDocumentListByDate.put(date, l);	
				}
				session.close();
			}else{
				Session session = sessionFactory.openSession();
				Criteria criteria = session.createCriteria(Mom.class);
				criteria.add(Restrictions.eq("userModelFrom", userFrom));
				criteria.add(Restrictions.eq("userModelTo", userTo));
				criteria.setProjection(Projections.rowCount());
				Long l = (Long) criteria.uniqueResult();
				
				criteria = session.createCriteria(Mom.class);
				criteria.add(Restrictions.eq("userModelFrom", userTo));
				criteria.add(Restrictions.eq("userModelTo", userFrom));
				criteria.setProjection(Projections.rowCount());
				Long l2 = (Long) criteria.uniqueResult();
				l=l+l2;

				if(l>0){
					mapCountOfDistinctDocumentListByDate.put(date, l);	
				}
				session.close();
			}
			
		}

		return mapCountOfDistinctDocumentListByDate;
	}

	@Override
	public List<Mom> getListOfMomByDate(Date date, Date dateTo, HttpServletRequest request) {

		HttpSession httpSession = request.getSession();
		UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
		UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Mom.class);
		criteria.add(Restrictions.between("date", date ,dateTo));
		criteria.add(Restrictions.eq("userModelFrom", userModelFro));
		criteria.add(Restrictions.eq("userModelTo", userModelTo));

		List<Mom> lst1 = criteria.list();
		criteria = session.createCriteria(Mom.class);
		criteria.add(Restrictions.between("date", date ,dateTo));
		criteria.add(Restrictions.eq("userModelFrom", userModelTo));
		criteria.add(Restrictions.eq("userModelTo", userModelFro));
		List<Mom> lst = criteria.list();
		session.close();
		lst.addAll(lst1);

		return lst;
	}
	

	

}
