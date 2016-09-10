package com.xanadutec.coreflex.feedcomWebServices;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
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

import com.xanadutec.coreflex.feedback.FeedBackSortingCompairator;
import com.xanadutec.coreflex.login.LoginService;
import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.FeedBackCounterForHome;
import com.xanadutec.coreflex.model.FeedBackSonumberSortingByDate;
import com.xanadutec.coreflex.model.FeedBackTypeTable;
import com.xanadutec.coreflex.model.Feedback;
import com.xanadutec.coreflex.model.RedFlag;
import com.xanadutec.coreflex.model.RoleModel;
import com.xanadutec.coreflex.model.Status;
import com.xanadutec.coreflex.model.UserModel;


@Repository("FeedcomWebServicesDao")
public class FeedcomWebServicesDaoImpl implements FeedcomWebServicesDao{
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private LoginService loginService;

	@Override
	public List<FeedBackCounterForHome> webServiceGetfeedBackCounterForHome(List<FeedBackTypeTable> feedBackTypeTables, UserModel userFrom, UserModel userTo){
		Session session = sessionFactory.openSession();
		try{
		
			
			UserModel userModelFro= userFrom;
			UserModel userModelTo= userTo;
			

			
			Status status=new Status();
			status.setStatusId(1);
			List<FeedBackCounterForHome> forHomes=new ArrayList<FeedBackCounterForHome>();
		for (FeedBackTypeTable feedBackTypeTable : feedBackTypeTables) {
			FeedBackCounterForHome feedBackCounterForHome=new FeedBackCounterForHome();
			Criteria criteria=session.createCriteria(Feedback.class);
			criteria.setProjection(Projections.distinct(Projections.countDistinct("soNumber")));
			criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
			criteria.add(Restrictions.eq("status", status));
			criteria.add(Restrictions.eq("userModelFrom", userModelFro));
			criteria.add(Restrictions.eq("userModelTo", userModelTo));
			List l= criteria.list();
			criteria=session.createCriteria(Feedback.class);
			criteria.setProjection(Projections.distinct(Projections.countDistinct("soNumber")));
			criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
			criteria.add(Restrictions.eq("status", status));
			criteria.add(Restrictions.eq("userModelFrom",userModelTo ));
			criteria.add(Restrictions.eq("userModelTo", userModelFro));
			List l1= criteria.list();
			int countft=Integer.parseInt(l1.get(0)+"");
			int counttf=Integer.parseInt(l.get(0)+"");
			
			criteria=session.createCriteria(RedFlag.class);
			criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
			criteria.add(Restrictions.eq("company", userModelFro.getCompany()));
			List<RedFlag> redflagCompany= criteria.list();
			
			criteria=session.createCriteria(RedFlag.class);
			criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
			criteria.add(Restrictions.eq("company", userModelTo.getCompany()));
			List<RedFlag> redflagCompanyTo= criteria.list();
			criteria.add(Restrictions.eq("company", userModelTo.getCompany()));
			
			int redf=0;
			int redft=0;
			try{
			redf=redflagCompany.size();
			redft=redflagCompanyTo.size();
			}catch(Exception e){
				
			}
			if(countft==counttf ){
				feedBackCounterForHome.setCount(countft);
			}else if(countft>counttf){
				feedBackCounterForHome.setCount(countft); 
			}else{
				feedBackCounterForHome.setCount(counttf);
			}
		
			feedBackCounterForHome.setRedflagCount(redf+redft);
			feedBackCounterForHome.setFeedBackTypeTable(feedBackTypeTable);
			forHomes.add(feedBackCounterForHome);
		}
		return forHomes;
	}catch(Exception e){
		e.printStackTrace();
	}finally {
		session.close();	
	}
		return null;

}

	@Override
	public List<FeedBackSonumberSortingByDate> getUpdatedSortedFeedBackList(UserModel userFrom,
			UserModel clientCompanyAdmin) {


		Session session = sessionFactory.openSession();
		
		Status status = new Status();
		status.setStatusId(1);

		System.out.println(userFrom.getUserName()+" from username1");
		System.out.println(clientCompanyAdmin.getUserName()+" from username2");
		
			Criteria criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
			criteria.createCriteria("feedback", "feed");
			criteria.add(Restrictions.eq("feed.userModelFrom", userFrom));
			criteria.add(Restrictions.eq("feed.userModelTo", clientCompanyAdmin));
			List<FeedBackSonumberSortingByDate> list = criteria.list();

			criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
			criteria.createCriteria("feedback", "feed");
			criteria.add(Restrictions.eq("feed.userModelFrom", clientCompanyAdmin));
			criteria.add(Restrictions.eq("feed.userModelTo", userFrom));
			List<FeedBackSonumberSortingByDate> list1 = criteria.list();
			
			try {
				list.addAll(list1);
				System.out.println(list.size()+" FeedBackSonumberSortingByDate size  ");
			} catch (Exception e) {
				e.printStackTrace();
			}
			Collections.sort(list, new FeedBackSortingCompairator());
			session.close();

		return list;
	
	}
	
	@Override
	public List<Feedback> getListOfChatingUsingSoNumberAndFeedBackType(String soNumber, FeedBackTypeTable feedBackTypeTable,UserModel userFrom,
			UserModel clientCompanyAdmin) {
		Session session = sessionFactory.openSession();
		try {

			Status status = new Status();
			status.setStatusId(1);


			Criteria criteria = session.createCriteria(Feedback.class);
			criteria.add(Restrictions.eq("soNumber", soNumber));
			criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
			criteria.add(Restrictions.eq("status", status));
			criteria.add(Restrictions.eq("userModelFrom", userFrom));
			criteria.add(Restrictions.eq("userModelTo", clientCompanyAdmin));
			List<Feedback> list = criteria.list();

			criteria = session.createCriteria(Feedback.class);
			criteria.add(Restrictions.eq("soNumber", soNumber));
			criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
			criteria.add(Restrictions.eq("status", status));
			criteria.add(Restrictions.eq("userModelFrom",clientCompanyAdmin));
			criteria.add(Restrictions.eq("userModelTo", userFrom));
			List<Feedback> list1 = criteria.list();

			list.addAll(list1);

			return list;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<FeedBackSonumberSortingByDate> getUpdatedSortedFeedBackListWithFeedbackType(UserModel userFrom, UserModel clientCompanyAdmin,
			FeedBackTypeTable feedBackTypeTable) {

		Session session = sessionFactory.openSession();
		
		Status status = new Status();
		status.setStatusId(1);

		System.out.println(userFrom.getUserName()+" from username1");
		System.out.println(clientCompanyAdmin.getUserName()+" from username2");
		
			Criteria criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
			criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
			criteria.createCriteria("feedback", "feed");
			criteria.add(Restrictions.eq("feed.userModelFrom", userFrom));
			criteria.add(Restrictions.eq("feed.userModelTo", clientCompanyAdmin));
			//criteria.setProjection(Projections.property("soNumber"));
			List<FeedBackSonumberSortingByDate> list = criteria.list();

			criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
			criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
			criteria.createCriteria("feedback", "feed");
			criteria.add(Restrictions.eq("feed.userModelFrom", clientCompanyAdmin));
			criteria.add(Restrictions.eq("feed.userModelTo", userFrom));
		//	criteria.setProjection(Projections.property("soNumber"));
			
			List<FeedBackSonumberSortingByDate> list1 = criteria.list();
			
			try {
				list.addAll(list1);
				System.out.println(list.size()+" FeedBackSonumberSortingByDate size  ");
			} catch (Exception e) {
				e.printStackTrace();
			}
			Collections.sort(list, new FeedBackSortingCompairator());
			session.close();

		return list;
	}
	
}
