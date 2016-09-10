package com.xanadutec.coreflex.dropBox;

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

import org.springframework.stereotype.Repository;

import com.xanadutec.coreflex.model.Documents;

import com.xanadutec.coreflex.model.Report;

import com.xanadutec.coreflex.model.UserModel;

@Repository("DropBoxDao")
public class DropBoxDaoImpl implements DropBoxDao {
	@Autowired
	private SessionFactory sessionFactory;

	@Override
	public void saveFIleDetails(Report fileName) {

		Session session = sessionFactory.openSession();
		session.save(fileName);
		Transaction t = session.beginTransaction();
		t.commit();
		session.close();
	}

	@Override
	public List<Date> getDistinctDropBoxList() {

		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Report.class);
		criteria.setProjection(Projections.distinct(Projections.property("date")));
		List<Date> lst = criteria.list();
		session.close();
		return lst;

	}

	@Override
	public void deleteFileByName(String name) {
		// TODO Auto-generated method stub

		Session session = sessionFactory.openSession();
		Transaction t = session.beginTransaction();
		Criteria criteria = session.createCriteria(Report.class);

		Report obj = (Report) criteria.add(Restrictions.eq("name", name)).uniqueResult();
		session.delete(obj);
		t.commit();
		session.close();
	}

	@Override
	public Map<Date, Map<Integer, List<Report>>> getCountOfDistinctDropBoxListByDistinctDatesOfDropBoxlist(
			List<Date> distinctDatesOfDropBoxlist, HttpServletRequest request) {
		Map<Date, Map<Integer, List<Report>>> mapCountOfDistinctReportListByDate = new HashMap<Date, Map<Integer, List<Report>>>();

		HttpSession httpSession = request.getSession();
		UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
		UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");

		for (Date date : distinctDatesOfDropBoxlist) {

			Map<Integer, List<Report>> innermapCountOfDistinctReportListByDate = new HashMap<Integer, List<Report>>();
			Session session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Report.class);
			criteria.add(Restrictions.eq("date", date));
			criteria.add(Restrictions.eq("userModelFrom", userModelFro));
			criteria.add(Restrictions.eq("userModelTo", userModelTo));
			criteria.setProjection(Projections.rowCount());
			Long l = (Long) criteria.uniqueResult();
			criteria = session.createCriteria(Report.class);
			criteria.add(Restrictions.eq("date", date));
			criteria.add(Restrictions.eq("userModelFrom", userModelTo));
			criteria.add(Restrictions.eq("userModelTo", userModelFro));
			criteria.setProjection(Projections.rowCount());
			Long l2 = (Long) criteria.uniqueResult();
			l = l + l2;

			criteria = session.createCriteria(Report.class);
			criteria.add(Restrictions.eq("date", date));
			criteria.add(Restrictions.eq("userModelFrom", userModelFro));
			criteria.add(Restrictions.eq("userModelTo", userModelTo));
			List<Report> lstOne = criteria.list();

			criteria = session.createCriteria(Report.class);
			criteria.add(Restrictions.eq("date", date));
			criteria.add(Restrictions.eq("userModelFrom", userModelTo));
			criteria.add(Restrictions.eq("userModelTo", userModelFro));
			List<Report> lstTwo = criteria.list();
			lstOne.addAll(lstTwo);

			if (l.intValue() > 0) {
				innermapCountOfDistinctReportListByDate.put(l.intValue(), lstOne);
				mapCountOfDistinctReportListByDate.put(date, innermapCountOfDistinctReportListByDate);
			}

			session.close();

		}

		return mapCountOfDistinctReportListByDate;
	}

	@Override
	public List<Report> getListOfDropBoxByDate(Date date,Date dateTo, HttpServletRequest request) {

		HttpSession httpSession = request.getSession();
		UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
		UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Report.class);
		criteria.add(Restrictions.between("date", date ,dateTo));
		criteria.add(Restrictions.eq("userModelFrom", userModelFro));
		criteria.add(Restrictions.eq("userModelTo", userModelTo));

		List<Report> lst1 = criteria.list();
		criteria = session.createCriteria(Report.class);
		criteria.add(Restrictions.between("date", date ,dateTo));
		criteria.add(Restrictions.eq("userModelFrom", userModelTo));
		criteria.add(Restrictions.eq("userModelTo", userModelFro));
		List<Report> lst = criteria.list();
		session.close();
		lst.addAll(lst1);

		return lst;
	}

	@Override
	public void saveDocumentFIleDetails(Documents fileName) {
		Session session = sessionFactory.openSession();
		session.save(fileName);
		Transaction t = session.beginTransaction();
		t.commit();
		session.close();

	}

	@Override
	public List<Date> getDistinctDocumentList() {
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Documents.class);
		criteria.setProjection(Projections.distinct(Projections.property("date")));
		List<Date> lst = criteria.list();
		session.close();
		return lst;

	}

	@Override
	public Map<Date, Map<Integer, List<Documents>>> getCountOfDistinctDocumentListByDistinctDatesOfDocuement(
			List<Date> distinctDatesOfDocuement, HttpServletRequest request) {

		Map<Date, Map<Integer, List<Documents>>> mapCountOfDistinctDocumentListByDate = new HashMap<Date, Map<Integer, List<Documents>>>();

		HttpSession httpSession = request.getSession();
		UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
		UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");

		for (Date date : distinctDatesOfDocuement) {
			Map<Integer, List<Documents>> innermapCountOfDistinctDocumentListByDate = new HashMap<Integer, List<Documents>>();
			Session session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(Documents.class);
			criteria.add(Restrictions.eq("date", date));
			criteria.add(Restrictions.eq("userModelFrom", userModelFro));
			criteria.add(Restrictions.eq("userModelTo", userModelTo));
			criteria.setProjection(Projections.rowCount());
			Long l = (Long) criteria.uniqueResult();
			criteria = session.createCriteria(Documents.class);
			criteria.add(Restrictions.eq("date", date));
			criteria.add(Restrictions.eq("userModelFrom", userModelTo));
			criteria.add(Restrictions.eq("userModelTo", userModelFro));
			criteria.setProjection(Projections.rowCount());
			Long l2 = (Long) criteria.uniqueResult();
			l = l + l2;

			criteria = session.createCriteria(Documents.class);
			criteria.add(Restrictions.eq("date", date));
			criteria.add(Restrictions.eq("userModelFrom", userModelFro));
			criteria.add(Restrictions.eq("userModelTo", userModelTo));
			List<Documents> lstOne = criteria.list();

			criteria = session.createCriteria(Report.class);
			criteria.add(Restrictions.eq("date", date));
			criteria.add(Restrictions.eq("userModelFrom", userModelTo));
			criteria.add(Restrictions.eq("userModelTo", userModelFro));
			List<Documents> lstTwo = criteria.list();
			lstOne.addAll(lstTwo);

			if (l.intValue() > 0) {

				innermapCountOfDistinctDocumentListByDate.put(l.intValue(), lstOne);

				mapCountOfDistinctDocumentListByDate.put(date, innermapCountOfDistinctDocumentListByDate);
			}

			session.close();

		}

		return mapCountOfDistinctDocumentListByDate;
	}

	@Override
	public List<Documents> getListOfDocumentByDate(Date date,Date dateTo, HttpServletRequest request) {
		/*
		 * Session session = sessionFactory.openSession(); Criteria criteria =
		 * session.createCriteria(Documents.class);
		 * criteria.add(Restrictions.eq("date", date)); List<Documents> lst =
		 * criteria.list(); session.close(); return lst;
		 */

		HttpSession httpSession = request.getSession();
		UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
		UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Documents.class);
		criteria.add(Restrictions.between("date", date ,dateTo));
		criteria.add(Restrictions.eq("userModelFrom", userModelFro));
		criteria.add(Restrictions.eq("userModelTo", userModelTo));

		List<Documents> lst1 = criteria.list();
		criteria = session.createCriteria(Documents.class);
		criteria.add(Restrictions.between("date", date ,dateTo));
		criteria.add(Restrictions.eq("userModelFrom", userModelTo));
		criteria.add(Restrictions.eq("userModelTo", userModelFro));
		List<Documents> lst = criteria.list();
		session.close();
		lst.addAll(lst1);

		return lst;
	}

	@Override
	public Map<Date, Long> getCountOfDistinctDropBoxListByDistinctDatesOfDropBoxlistAndUserLastVisitTime(
			List<Date> distinctDatesOfDocuement, Date lastVisitedTime, UserModel userFrom, UserModel userTo) {

		Map<Date, Long> mapCountOfDistinctDocumentListByDate = new HashMap<Date, Long>();

		for (Date date : distinctDatesOfDocuement) {

			if (lastVisitedTime != null) {
				Session session = sessionFactory.openSession();
				Criteria criteria = session.createCriteria(Report.class);
				criteria.add(Restrictions.gt("dateTime", lastVisitedTime));
				criteria.add(Restrictions.eq("userModelFrom", userFrom));
				criteria.add(Restrictions.eq("userModelTo", userTo));
				criteria.setProjection(Projections.rowCount());
				Long l = (Long) criteria.uniqueResult();

				criteria = session.createCriteria(Report.class);
				criteria.add(Restrictions.gt("dateTime", lastVisitedTime));
				criteria.add(Restrictions.eq("userModelFrom", userTo));
				criteria.add(Restrictions.eq("userModelTo", userFrom));
				criteria.setProjection(Projections.rowCount());
				Long l2 = (Long) criteria.uniqueResult();
				l = l + l2;
				if (l > 0) {
					mapCountOfDistinctDocumentListByDate.put(date, l);
				}
				session.close();
			} else {
				Session session = sessionFactory.openSession();
				Criteria criteria = session.createCriteria(Report.class);
				criteria.add(Restrictions.eq("userModelFrom", userFrom));
				criteria.add(Restrictions.eq("userModelTo", userTo));
				criteria.setProjection(Projections.rowCount());
				Long l = (Long) criteria.uniqueResult();

				criteria = session.createCriteria(Report.class);
				criteria.add(Restrictions.eq("userModelFrom", userTo));
				criteria.add(Restrictions.eq("userModelTo", userFrom));
				criteria.setProjection(Projections.rowCount());
				Long l2 = (Long) criteria.uniqueResult();
				l = l + l2;
				if (l > 0) {
					mapCountOfDistinctDocumentListByDate.put(date, l);
				}
				session.close();
			}

		}

		return mapCountOfDistinctDocumentListByDate;
	}

	@Override
	public Map<Date, Long> getCountOfDistinctDocumentListByDistinctDatesOfDocuementAndUserLastVisitTime(
			List<Date> distinctDatesOfDocuement, Date lastVisitedTime, UserModel userFrom, UserModel userTo) {

		Map<Date, Long> mapCountOfDistinctDocumentListByDate = new HashMap<Date, Long>();

		for (Date date : distinctDatesOfDocuement) {

			if (lastVisitedTime != null) {
				Session session = sessionFactory.openSession();
				Criteria criteria = session.createCriteria(Documents.class);
				criteria.add(Restrictions.gt("dateTime", lastVisitedTime));
				criteria.add(Restrictions.eq("userModelFrom", userFrom));
				criteria.add(Restrictions.eq("userModelTo", userTo));
				criteria.setProjection(Projections.rowCount());
				Long l = (Long) criteria.uniqueResult();

				criteria = session.createCriteria(Documents.class);
				criteria.add(Restrictions.gt("dateTime", lastVisitedTime));
				criteria.add(Restrictions.eq("userModelFrom", userTo));
				criteria.add(Restrictions.eq("userModelTo", userFrom));
				criteria.setProjection(Projections.rowCount());
				Long l2 = (Long) criteria.uniqueResult();
				l = l + l2;
				if (l > 0) {
					mapCountOfDistinctDocumentListByDate.put(date, l);
				}
				session.close();
			} else {
				Session session = sessionFactory.openSession();
				Criteria criteria = session.createCriteria(Documents.class);
				criteria.add(Restrictions.eq("userModelFrom", userFrom));
				criteria.add(Restrictions.eq("userModelTo", userTo));
				criteria.setProjection(Projections.rowCount());
				Long l = (Long) criteria.uniqueResult();

				criteria = session.createCriteria(Documents.class);
				criteria.add(Restrictions.eq("userModelFrom", userTo));
				criteria.add(Restrictions.eq("userModelTo", userFrom));
				criteria.setProjection(Projections.rowCount());
				Long l2 = (Long) criteria.uniqueResult();
				l = l + l2;
				if (l > 0) {
					mapCountOfDistinctDocumentListByDate.put(date, l);
				}
				session.close();
			}

		}

		return mapCountOfDistinctDocumentListByDate;
	}

	@Override
	public List<Report> getListOfupdated50Reports(UserModel userFrom, UserModel userTo) {
		// TODO Auto-generated method stub

		Session session = sessionFactory.openSession();

		Criteria criteria = session.createCriteria(Report.class);
		criteria.add(Restrictions.eq("userModelFrom", userFrom));
		criteria.add(Restrictions.eq("userModelTo", userTo));
		criteria.setMaxResults(50);
		List<Report> list = criteria.list();

		criteria = session.createCriteria(Report.class);
		criteria.add(Restrictions.eq("userModelFrom", userTo));
		criteria.add(Restrictions.eq("userModelTo", userFrom));
		criteria.setMaxResults(50);
		List<Report> list1 = criteria.list();

		try {
			list.addAll(list1);
			Collections.sort(list, new ReportDateCompairator());
			if (list.size() > 50) {
				list = list.subList(0, 50);
			}
		} catch (Exception e) {

		}
		session.close();

		return list;
	}

	@Override
	public List<Documents> getListOfupdated50Documents(UserModel userFrom, UserModel userTo) {
		// TODO Auto-generated method stub

		Session session = sessionFactory.openSession();

		Criteria criteria = session.createCriteria(Documents.class);
		criteria.add(Restrictions.eq("userModelFrom", userFrom));
		criteria.add(Restrictions.eq("userModelTo", userTo));
		criteria.setMaxResults(50);
		List<Documents> list = criteria.list();

		criteria = session.createCriteria(Documents.class);
		criteria.add(Restrictions.eq("userModelFrom", userTo));
		criteria.add(Restrictions.eq("userModelTo", userFrom));
		criteria.setMaxResults(50);
		List<Documents> list1 = criteria.list();

		try {
			list.addAll(list1);
			if (list.size() > 50) {
				list = list.subList(0, 50);
			}
			Collections.sort(list, new DocumentDateCompairator());

		} catch (Exception e) {

		}
		session.close();

		return list;
	}

	@Override
	public List<Report> getListOfDropBoxByFileName(String fileName, HttpServletRequest request) {


		HttpSession httpSession = request.getSession();
		UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
		UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Report.class);
		criteria.add(Restrictions.ilike("name", "%"+fileName+"%"));
		criteria.add(Restrictions.eq("userModelFrom", userModelFro));
		criteria.add(Restrictions.eq("userModelTo", userModelTo));

		List<Report> lst1 = criteria.list();
		criteria = session.createCriteria(Report.class);
		criteria.add(Restrictions.ilike("name", "%"+fileName+"%"));
		criteria.add(Restrictions.eq("userModelFrom", userModelTo));
		criteria.add(Restrictions.eq("userModelTo", userModelFro));
		List<Report> lst = criteria.list();
		session.close();
		lst.addAll(lst1);

		return lst;
	
	}

	@Override
	public List<Documents> getListOfDocumentByFileName(String fileName, HttpServletRequest request) {


		HttpSession httpSession = request.getSession();
		UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
		UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");
		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Documents.class);
		criteria.add(Restrictions.ilike("name", "%"+fileName+"%"));
		criteria.add(Restrictions.eq("userModelFrom", userModelFro));
		criteria.add(Restrictions.eq("userModelTo", userModelTo));

		List<Documents> lst1 = criteria.list();
		criteria = session.createCriteria(Report.class);
		criteria.add(Restrictions.ilike("name", "%"+fileName+"%"));
		criteria.add(Restrictions.eq("userModelFrom", userModelTo));
		criteria.add(Restrictions.eq("userModelTo", userModelFro));
		List<Documents> lst = criteria.list();
		session.close();
		lst.addAll(lst1);

		return lst;
	
	}

	@Override
	public Report getReportObjectByReportId(int id) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		Report report =	(Report) session.get(Report.class, id);
		session.close();
		return report;
	}

	@Override
	public Documents getDocumentsObjectByDocumentsId(int id) {
		Session session = sessionFactory.openSession();
		Documents documents =	(Documents) session.get(Documents.class, id);
		session.close();
		return documents;
	}

}
