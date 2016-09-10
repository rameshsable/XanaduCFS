package com.xanadutec.coreflex.feedback;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SQLQuery;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xanadutec.coreflex.login.LoginService;
import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.CompanyFeedBackTypeAssociation;
import com.xanadutec.coreflex.model.FcmNotification;
import com.xanadutec.coreflex.model.FeedBackChatingCounter;
import com.xanadutec.coreflex.model.FeedBackCounterForHome;
import com.xanadutec.coreflex.model.FeedBackSonumberSortingByDate;
import com.xanadutec.coreflex.model.FeedBackTypeTable;
import com.xanadutec.coreflex.model.Feedback;
import com.xanadutec.coreflex.model.Operator;
import com.xanadutec.coreflex.model.RedFlag;
import com.xanadutec.coreflex.model.Status;
import com.xanadutec.coreflex.model.UserModel;
import com.xanadutec.coreflex.notifiation.NotificationService;
import com.xanadutec.coreflex.operators.OperatorService;

@Repository("FeedbackDao")
public class FeedbackDaoImpl implements FeedbackDao {

	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	HttpServletRequest servletRequest;
	@Autowired
	FeedbackService feedbackservice;
	
	@Autowired
	private NotificationService notificationService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private OperatorService operatorService;

	public String addFeedbackTypes(FeedBackTypeTable feedBackTypeTable) {
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		int id = (Integer) session.save(feedBackTypeTable);
		tx.commit();
		session.close();
		return id + "";
	}

	public int getfeedBackCount() {
		Session session = sessionFactory.openSession();
		try {
			Criteria criteria = session.createCriteria(FeedBackTypeTable.class);
			if (criteria.list().size() > 0) {
				return 1;
			} else {
				return 0;
			}
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			session.close();
		}
		return 0;

	}

	public List<FeedBackTypeTable> getFeedbackList() {
		Session session = sessionFactory.openSession();
		try {
			Criteria criteria = session.createCriteria(FeedBackTypeTable.class);
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;

	}

	public List<FeedBackCounterForHome> getfeedBackCounterForHome(List<FeedBackTypeTable> feedBackTypeTables,
			HttpServletRequest request) {
		Session session = sessionFactory.openSession();
		try {

			HttpSession httpSession = request.getSession();
			UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
			UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");

			Status status = new Status();

			List<FeedBackCounterForHome> forHomes = new ArrayList<FeedBackCounterForHome>();
			for (FeedBackTypeTable feedBackTypeTable : feedBackTypeTables) {
				status.setStatusId(1);
				FeedBackCounterForHome feedBackCounterForHome = new FeedBackCounterForHome();
				Criteria criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);

				criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
				criteria.setProjection(Projections.distinct(Projections.countDistinct("soNumber")));
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
				criteria.add(Restrictions.eq("userModelFrom", userModelFro));
				criteria.add(Restrictions.eq("userModelTo", userModelTo));
				List l = criteria.list();

				criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
				criteria.setProjection(Projections.distinct(Projections.countDistinct("soNumber")));
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
				criteria.add(Restrictions.eq("userModelFrom", userModelTo));
				criteria.add(Restrictions.eq("userModelTo", userModelFro));
				List l1 = criteria.list();

				int countft1 = Integer.parseInt(l1.get(0) + "");
				int counttf1 = Integer.parseInt(l.get(0) + "");

				int totalCount = countft1 + counttf1;

				feedBackCounterForHome.setTotalCount(countft1 + counttf1);

				criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
				criteria.setProjection(Projections.distinct(Projections.countDistinct("soNumber")));
				criteria.createCriteria("feedback", "feed");
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
				criteria.add(Restrictions.isNull("feedback_close"));
				criteria.add(Restrictions.eq("userModelFrom", userModelFro));
				criteria.add(Restrictions.eq("userModelTo", userModelTo));
				l = criteria.list();

				criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
				criteria.setProjection(Projections.distinct(Projections.countDistinct("soNumber")));
				criteria.createCriteria("feedback", "feed");
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
				criteria.add(Restrictions.isNull("feedback_close"));
				criteria.add(Restrictions.eq("userModelFrom", userModelTo));
				criteria.add(Restrictions.eq("userModelTo", userModelFro));
				l1 = criteria.list();

				int countft2 = Integer.parseInt(l1.get(0) + "");
				int counttf2 = Integer.parseInt(l.get(0) + "");
				int openCount = countft2 + counttf2;
				int closeCount = totalCount - openCount;

				System.out.println(
						countft2 + "countft2  " + counttf2 + " counttf2   " + feedBackTypeTable.getFeedbackType());
				feedBackCounterForHome.setCount(openCount);

				feedBackCounterForHome.setCloseCount(closeCount);

				feedBackCounterForHome.setFeedBackTypeTable(feedBackTypeTable);
				forHomes.add(feedBackCounterForHome);
			}
			return forHomes;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;

	}

	public List<FeedBackCounterForHome> getfeedBackCounterForHomeByDate(List<FeedBackTypeTable> feedBackTypeTables,
			HttpServletRequest request, Date fromDate, Date toDate) {
		Session session = sessionFactory.openSession();
		try {

			HttpSession httpSession = request.getSession();
			UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
			UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");

			Status status = new Status();

			List<FeedBackCounterForHome> forHomes = new ArrayList<FeedBackCounterForHome>();
			for (FeedBackTypeTable feedBackTypeTable : feedBackTypeTables) {
				status.setStatusId(1);
				FeedBackCounterForHome feedBackCounterForHome = new FeedBackCounterForHome();
				Criteria criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
				criteria.setProjection(Projections.distinct(Projections.countDistinct("soNumber")));
				criteria.createCriteria("feedback", "feed");
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
				criteria.add(Restrictions.eq("feed.status", status));
				criteria.add(Restrictions.eq("userModelFrom", userModelFro));
				criteria.add(Restrictions.eq("userModelTo", userModelTo));
				criteria.add(Restrictions.between("emailDate", fromDate, toDate));
				List l = criteria.list();

				criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
				criteria.setProjection(Projections.distinct(Projections.countDistinct("soNumber")));
				criteria.createCriteria("feedback", "feed");
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
				criteria.add(Restrictions.eq("feed.status", status));
				criteria.add(Restrictions.eq("userModelFrom", userModelTo));
				criteria.add(Restrictions.eq("userModelTo", userModelFro));
				criteria.add(Restrictions.between("emailDate", fromDate, toDate));
				List l1 = criteria.list();

				int countft = Integer.parseInt(l1.get(0) + "");
				int counttf = Integer.parseInt(l.get(0) + "");
				if (countft == counttf) {
					feedBackCounterForHome.setCount(countft);
					System.out.println("count 1- " + countft);
				} else if (countft > counttf) {
					feedBackCounterForHome.setCount(countft);
					System.out.println("count 2- " + countft);
				} else {
					feedBackCounterForHome.setCount(counttf);
					System.out.println("count 3- " + countft);
				}

				criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
				criteria.setProjection(Projections.distinct(Projections.countDistinct("soNumber")));
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
				criteria.add(Restrictions.eq("userModelFrom", userModelFro));
				criteria.add(Restrictions.eq("userModelTo", userModelTo));
				criteria.add(Restrictions.between("emailDate", fromDate, toDate));
				l = criteria.list();

				criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
				criteria.setProjection(Projections.distinct(Projections.countDistinct("soNumber")));
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
				criteria.add(Restrictions.eq("userModelFrom", userModelTo));
				criteria.add(Restrictions.eq("userModelTo", userModelFro));
				criteria.add(Restrictions.between("emailDate", fromDate, toDate));
				l1 = criteria.list();

				int countft1 = Integer.parseInt(l1.get(0) + "");
				int counttf1 = Integer.parseInt(l.get(0) + "");

				if (countft1 == counttf1) {
					feedBackCounterForHome.setTotalCount(countft1);
				} else if (countft1 > counttf1) {
					feedBackCounterForHome.setTotalCount(countft1);
				} else {
					feedBackCounterForHome.setTotalCount(counttf1);
				}

				Status status1 = new Status();
				status1.setStatusId(2);
				criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
				criteria.setProjection(Projections.distinct(Projections.countDistinct("soNumber")));
				criteria.createCriteria("feedback", "feed");
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
				criteria.add(Restrictions.eq("feed.status", status1));
				criteria.add(Restrictions.eq("userModelFrom", userModelFro));
				criteria.add(Restrictions.eq("userModelTo", userModelTo));
				criteria.add(Restrictions.between("emailDate", fromDate, toDate));
				l = criteria.list();

				criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
				criteria.setProjection(Projections.distinct(Projections.countDistinct("soNumber")));
				criteria.createCriteria("feedback", "feed");
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
				criteria.add(Restrictions.eq("feed.status", status1));
				criteria.add(Restrictions.eq("userModelFrom", userModelTo));
				criteria.add(Restrictions.eq("userModelTo", userModelFro));
				criteria.add(Restrictions.between("emailDate", fromDate, toDate));
				l1 = criteria.list();

				int countft2 = Integer.parseInt(l1.get(0) + "");
				int counttf2 = Integer.parseInt(l.get(0) + "");
				if (countft2 == counttf2) {
					feedBackCounterForHome.setCloseCount(countft2);
				} else if (countft2 > counttf2) {
					feedBackCounterForHome.setCloseCount(countft2);
				} else {
					feedBackCounterForHome.setCloseCount(counttf2);
				}

				Status status2 = new Status();
				status2.setStatusId(3);
				criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
				criteria.setProjection(Projections.distinct(Projections.countDistinct("soNumber")));
				criteria.createCriteria("feedback", "feed");
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
				criteria.add(Restrictions.eq("feed.status", status2));
				criteria.add(Restrictions.eq("userModelFrom", userModelFro));
				criteria.add(Restrictions.eq("userModelTo", userModelTo));
				criteria.add(Restrictions.between("emailDate", fromDate, toDate));
				l = criteria.list();

				criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
				criteria.setProjection(Projections.distinct(Projections.countDistinct("soNumber")));
				criteria.createCriteria("feedback", "feed");
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
				criteria.add(Restrictions.eq("feed.status", status2));
				criteria.add(Restrictions.eq("userModelFrom", userModelTo));
				criteria.add(Restrictions.eq("userModelTo", userModelFro));
				criteria.add(Restrictions.between("emailDate", fromDate, toDate));
				l1 = criteria.list();

				int countft3 = Integer.parseInt(l1.get(0) + "");
				int counttf3 = Integer.parseInt(l.get(0) + "");
				if (countft3 == counttf3) {
					feedBackCounterForHome.setInProgressCount(countft3);
				} else if (countft3 > counttf3) {
					feedBackCounterForHome.setInProgressCount(countft3);
				} else {
					feedBackCounterForHome.setInProgressCount(counttf3);
				}

				countft = Integer.parseInt(l1.get(0) + "");
				counttf = Integer.parseInt(l.get(0) + "");

				feedBackCounterForHome.setFeedBackTypeTable(feedBackTypeTable);
				forHomes.add(feedBackCounterForHome);
			}
			return forHomes;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;

	}

	@Override
	public List<Feedback> getfeedBackListForFeedBackId(FeedBackTypeTable feedBackTypeTables,
			HttpServletRequest request) {
		Session session = sessionFactory.openSession();
		try {

			HttpSession httpSession = request.getSession();
			UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
			UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");
			Status status = new Status();
			status.setStatusId(1);

			String hql = "SELECT DISTINCT soNumber FROM Feedback where feedBackType =:feedBackType AND status =:status ";
			Query query = session.createQuery(hql);
			query.setParameter("feedBackType", feedBackTypeTables);
			query.setParameter("status", status);
			List<String> results = query.list();

			List<Feedback> feed = new ArrayList<Feedback>();
			for (String sono : results) {
				Criteria criteria = session.createCriteria(Feedback.class);
				criteria.add(Restrictions.eq("soNumber", sono));
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTables));
				criteria.add(Restrictions.eq("status", status));
				criteria.add(Restrictions.eq("userModelFrom", userModelFro));
				criteria.add(Restrictions.eq("userModelTo", userModelTo));
				List<Feedback> list = criteria.list();

				criteria = session.createCriteria(Feedback.class);
				criteria.add(Restrictions.eq("soNumber", sono));
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTables));
				criteria.add(Restrictions.eq("status", status));
				criteria.add(Restrictions.eq("userModelFrom", userModelTo));
				criteria.add(Restrictions.eq("userModelTo", userModelFro));
				List<Feedback> list1 = criteria.list();
				try {
					list.addAll(list1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Collections.sort(list, new FeedBackCountComparator());
				Collections.sort(list1, new FeedBackCountComparator());

				if (list.size() > 0) {
					Feedback feedback = new Feedback();
					feedback.setSoNumber(sono);
					Feedback f = (Feedback) list.get(0);
					feedback.setFeedbackText(f.getFeedbackText());
					feedback.setOperator(f.getOperator());
					feedback.setFdate(f.getFdate());
					feed.add(feedback);
				} else if (list1.size() > 0) {
					Feedback feedback = new Feedback();
					feedback.setSoNumber(sono);
					Feedback f = (Feedback) list1.get(0);
					feedback.setFeedbackText(f.getFeedbackText());
					feedback.setOperator(f.getOperator());
					feedback.setFdate(f.getFdate());
					feed.add(feedback);
				}

			}
			return feed;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;

	}

	@Override
	public List<Feedback> getListOfChatingUsingSoNumberAndFeedBackType(String soNumber, String feedbackType,
			HttpServletRequest request) {
		Session session = sessionFactory.openSession();
		try {

			FeedBackTypeTable feedBackTypeTable = new FeedBackTypeTable();
			feedBackTypeTable.setId(Integer.parseInt(feedbackType));

			Criteria criteria = session.createCriteria(Feedback.class);
			criteria.add(Restrictions.eq("soNumber", soNumber));
			criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
			criteria.add(Restrictions.eq("userModelFrom", (UserModel) request.getSession().getAttribute("UserFrom")));
			criteria.add(Restrictions.eq("userModelTo", (UserModel) request.getSession().getAttribute("UserTo")));
			List<Feedback> list = criteria.list();

			criteria = session.createCriteria(Feedback.class);
			criteria.add(Restrictions.eq("soNumber", soNumber));
			criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
			criteria.add(Restrictions.eq("userModelFrom", (UserModel) request.getSession().getAttribute("UserTo")));
			criteria.add(Restrictions.eq("userModelTo", (UserModel) request.getSession().getAttribute("UserFrom")));
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
	public List<Feedback> getClosedFeedComListOfChatingUsingSoNumberAndFeedBackType(String soNumber,
			String feedbackType, HttpServletRequest request) {
		Session session = sessionFactory.openSession();
		try {

			Status status = new Status();
			status.setStatusId(2);

			FeedBackTypeTable feedBackTypeTable = new FeedBackTypeTable();
			feedBackTypeTable.setId(Integer.parseInt(feedbackType));

			Criteria criteria = session.createCriteria(Feedback.class);
			criteria.add(Restrictions.eq("soNumber", soNumber));
			criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
			criteria.add(Restrictions.eq("status", status));
			criteria.add(Restrictions.eq("userModelFrom", (UserModel) request.getSession().getAttribute("UserFrom")));
			criteria.add(Restrictions.eq("userModelTo", (UserModel) request.getSession().getAttribute("UserTo")));
			List<Feedback> list = criteria.list();

			criteria = session.createCriteria(Feedback.class);
			criteria.add(Restrictions.eq("soNumber", soNumber));
			criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
			criteria.add(Restrictions.eq("status", status));
			criteria.add(Restrictions.eq("userModelFrom", (UserModel) request.getSession().getAttribute("UserTo")));
			criteria.add(Restrictions.eq("userModelTo", (UserModel) request.getSession().getAttribute("UserFrom")));
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
	public String saveChattingForFeedCom(String soNumber, String status, String subject, String Attachment,
			String chatBox, String feedbackType, String oper) {
		Session session = sessionFactory.openSession();
		try {

			if (status.equalsIgnoreCase("undefined")) {
				status = 1 + "";
			}
			Status status2 = new Status();
			status2.setStatusId(Integer.parseInt(status));

			Criteria criteria = session.createCriteria(FeedBackTypeTable.class);
			List<FeedBackTypeTable> list = criteria.list();
			FeedBackTypeTable typeTable = null;
			for (FeedBackTypeTable table : list) {
				if (table.getFeedbackType().equalsIgnoreCase(feedbackType)) {
					typeTable = table;
					break;
				}
			}
			UserModel userModel = (UserModel) servletRequest.getSession().getAttribute("UserFrom");

			userModel = (UserModel) servletRequest.getSession().getAttribute("UserTo");

			String hql = "select max(f.feedbackCounter) from FeedBackChatingCounter f where f.so_number =:so_number AND f.feedBackType =:feedBackType "
					+ " AND f.userModelFrom =:userModelFrom AND f.userModelTo=:userModelTo";
			Query query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", (UserModel) servletRequest.getSession().getAttribute("UserFrom"));
			query.setParameter("userModelTo", (UserModel) servletRequest.getSession().getAttribute("UserTo"));
			List results = query.list();

			query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", (UserModel) servletRequest.getSession().getAttribute("UserTo"));
			query.setParameter("userModelTo", (UserModel) servletRequest.getSession().getAttribute("UserFrom"));
			List results1 = query.list();

			/*
			 * int userf=Integer.parseInt(results.get(0)+""); int
			 * usert=Integer.parseInt(results1.get(0)+"");
			 */

			int usert = 0;
			int userf = 0;
			try {
				usert = Integer.parseInt(results1.get(0) + "");
			} catch (Exception e) {

			}
			try {
				userf = Integer.parseInt(results.get(0) + "");
			} catch (Exception e) {

			}

			FeedBackChatingCounter FeedchatingCount = new FeedBackChatingCounter();
			int count = 0;
			if (userf == usert) {
				count = userf + 1;
				// FeedchatingCount.setFeedbackCounter(count);
				FeedchatingCount.setSo_number(soNumber);
				FeedchatingCount.setFeedBackType(typeTable);
				FeedchatingCount.setUserModelFrom((UserModel) servletRequest.getSession().getAttribute("UserFrom"));
				FeedchatingCount.setUserModelTo((UserModel) servletRequest.getSession().getAttribute("UserTo"));
				FeedchatingCount.setCount(1);
			} else if (userf > usert) {
				criteria = session.createCriteria(FeedBackChatingCounter.class);
				criteria.add(Restrictions.eq("feedbackCounter", userf));
				FeedBackChatingCounter chatingCounter = (FeedBackChatingCounter) criteria.uniqueResult();
				count = userf + 1;
				// FeedchatingCount.setFeedbackCounter(count);
				FeedchatingCount.setSo_number(chatingCounter.getSo_number());
				FeedchatingCount.setFeedBackType(chatingCounter.getFeedBackType());
				FeedchatingCount.setUserModelFrom((UserModel) servletRequest.getSession().getAttribute("UserFrom"));
				FeedchatingCount.setUserModelTo((UserModel) servletRequest.getSession().getAttribute("UserTo"));
				FeedchatingCount.setCount(chatingCounter.getCount() + 1);

			} else if (usert > userf) {
				criteria = session.createCriteria(FeedBackChatingCounter.class);
				criteria.add(Restrictions.eq("feedbackCounter", usert));
				FeedBackChatingCounter chatingCounter = (FeedBackChatingCounter) criteria.uniqueResult();
				count = usert + 1;
				// FeedchatingCount.setFeedbackCounter(count+1);
				FeedchatingCount.setSo_number(chatingCounter.getSo_number());
				FeedchatingCount.setFeedBackType(chatingCounter.getFeedBackType());
				FeedchatingCount.setUserModelFrom((UserModel) servletRequest.getSession().getAttribute("UserFrom"));
				FeedchatingCount.setUserModelTo((UserModel) servletRequest.getSession().getAttribute("UserTo"));
				FeedchatingCount.setCount(chatingCounter.getCount() + 1);
			}

			criteria = session.createCriteria(Operator.class);
			List<Operator> listOperator = criteria.list();
			Operator operator = null;
			try{
				operator=	feedbackservice.getOperatorByUserName(oper);
			}catch(Exception e){
				e.printStackTrace();
			}
			/*for (Operator table : listOperator) {
				if (table.getUserName().equalsIgnoreCase(oper)) {
					operator = table;
					break;
				}
			}*/
			

			Transaction transaction = session.beginTransaction();
			session.save(FeedchatingCount);
			transaction.commit();
			session.close();
			System.out.println("DATA SAVED");
			
			Feedback feedback = new Feedback();
			feedback.setFeedbackText(chatBox);
			feedback.setCounter(FeedchatingCount);
			feedback.setSoNumber(soNumber);
			feedback.setFeedBackType(typeTable);
			feedback.setOperator(operator);
			feedback.setFdate(new Date());
			feedback.setAttachment(Attachment);
			feedback.setEmailSubject(subject);

			if (Integer.parseInt(status) == 2) {
				Session session1 = sessionFactory.openSession();
				String hql1 = "update Feedback set status = :status where soNumber =:soNumber AND feedBackType =:feedBackType "
						+ " AND userModelFrom =:userModelFrom AND userModelTo=:userModelTo";
				Query query1 = session1.createQuery(hql1);
				query1.setParameter("status", status2);
				query1.setParameter("feedBackType", typeTable);
				query1.setParameter("soNumber", soNumber);
				query1.setParameter("userModelFrom", (UserModel) servletRequest.getSession().getAttribute("UserFrom"));
				query1.setParameter("userModelTo", (UserModel) servletRequest.getSession().getAttribute("UserTo"));
				query1.executeUpdate();

				hql1 = "update Feedback f set status = :status where soNumber =:soNumber AND feedBackType =:feedBackType "
						+ " AND userModelFrom =:userModelFrom AND userModelTo=:userModelTo";
				query1 = session1.createQuery(hql1);
				query1.setParameter("status", status2);
				query1.setParameter("feedBackType", typeTable);
				query1.setParameter("soNumber", soNumber);
				query1.setParameter("userModelFrom", (UserModel) servletRequest.getSession().getAttribute("UserTo"));
				query1.setParameter("userModelTo", (UserModel) servletRequest.getSession().getAttribute("UserFrom"));
				query1.executeUpdate();
				session1.close();
			}
			feedback.setStatus(status2);
			feedback.setUserModelFrom((UserModel) servletRequest.getSession().getAttribute("UserFrom"));
			feedback.setUserModelTo((UserModel) servletRequest.getSession().getAttribute("UserTo"));
			feedback.setUserfeedback((UserModel) servletRequest.getSession().getAttribute("UserMessage"));

		
			org.hibernate.Session session2 = sessionFactory.openSession();
			Transaction transaction1 = session2.beginTransaction();
			session2.save(feedback);
			
			final Feedback feedback2 =feedback;
			final UserModel userModelFrom = (UserModel) servletRequest.getSession().getAttribute("UserFrom");
			final UserModel userModelTo = (UserModel) servletRequest.getSession().getAttribute("UserTo");
			new Thread() {
				public void run() {
					
				
					 List<UserModel> userModels = loginService
							.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userModelFrom.getCompany());
					userModels.addAll(loginService.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userModelTo.getCompany()));
					 FcmNotification fcmNotification = notificationService.getFcmNotification();
					notificationService.sendFeedbackNotification(feedback2, fcmNotification, userModels, servletRequest);
				
				}
			}.start();
			
			transaction1.commit();
			session2.close();
			// save feedcom preferance accoding to date

			try {
				org.hibernate.Session sessionforFeedBackSonumberSorting = sessionFactory.openSession();
				Transaction transactionsessionforFeedBackSonumberSorting = sessionforFeedBackSonumberSorting
						.beginTransaction();
				Criteria criteriasessionforFeedBackSonumberSorting = sessionforFeedBackSonumberSorting
						.createCriteria(FeedBackSonumberSortingByDate.class);
				criteriasessionforFeedBackSonumberSorting.add(Restrictions.eq("soNumber", feedback.getSoNumber()));
				criteriasessionforFeedBackSonumberSorting
						.add(Restrictions.eq("feedBackType", feedback.getFeedBackType()));

				FeedBackSonumberSortingByDate sortingByDate = (FeedBackSonumberSortingByDate) criteriasessionforFeedBackSonumberSorting
						.uniqueResult();
				if (sortingByDate != null) {
					sortingByDate.setFdate(feedback.getFdate());
					sortingByDate.setSoNumber(feedback.getSoNumber());
					sortingByDate.setFeedBackType(feedback.getFeedBackType());
					sortingByDate.setUserModelFrom(feedback.getUserModelFrom());
					sortingByDate.setUserModelTo(feedback.getUserModelTo());
					if (feedback.getStatus().getStatusId() == 2) {
						sortingByDate.setFeedback_close(feedback);
					}
					sessionforFeedBackSonumberSorting.saveOrUpdate(sortingByDate);
				} else {
					sortingByDate = new FeedBackSonumberSortingByDate();
					sortingByDate.setFdate(feedback.getFdate());
					sortingByDate.setSoNumber(feedback.getSoNumber());
					sortingByDate.setFeedBackType(feedback.getFeedBackType());
					sortingByDate.setUserModelFrom(feedback.getUserModelFrom());
					sortingByDate.setUserModelTo(feedback.getUserModelTo());
					if (feedback.getStatus().getStatusId() == 2) {
						sortingByDate.setFeedback_close(feedback);
					}

					sessionforFeedBackSonumberSorting.saveOrUpdate(sortingByDate);
				}
				transactionsessionforFeedBackSonumberSorting.commit();
				sessionforFeedBackSonumberSorting.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			// end save feedcom preferance accoding to date
			session = sessionFactory.openSession();
			hql = "select max(f.feedbackCounter) from FeedBackChatingCounter f where f.so_number =:so_number AND f.feedBackType =:feedBackType "
					+ " AND f.userModelFrom =:userModelFrom AND f.userModelTo=:userModelTo";
			query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", (UserModel) servletRequest.getSession().getAttribute("UserFrom"));
			query.setParameter("userModelTo", (UserModel) servletRequest.getSession().getAttribute("UserTo"));
			results = query.list();

			query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", (UserModel) servletRequest.getSession().getAttribute("UserTo"));
			query.setParameter("userModelTo", (UserModel) servletRequest.getSession().getAttribute("UserFrom"));
			results1 = query.list();

			usert = 0;
			userf = 0;
			try {
				usert = Integer.parseInt(results1.get(0) + "");
			} catch (Exception e) {

			}
			try {
				userf = Integer.parseInt(results.get(0) + "");
			} catch (Exception e) {

			}

			return userf + "#" + usert;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "";

	}

	public Operator getOperatorByUserName(String userName) {
		org.hibernate.Session session = sessionFactory.openSession();
		try{
			Criteria criteria = session.createCriteria(Operator.class);
			criteria.add(Restrictions.eq("userName", userName));
			List<Operator> operator = (List<Operator>) criteria.list();
			
			session.close();
			return operator.get(0);
		}catch(Exception e)
		{
			e.printStackTrace();
		}
		return null;
		
	}
	
	
	@Override
	public String saveChattingFeedComForWebService(String soNumber, String status, String subject, String Attachment,
			String chatBox, String feedbackType, String oper, final UserModel userFrom, final UserModel userTo,
			final UserModel userFeedback) {
		Session session = sessionFactory.openSession();
		try {

			if (status.equalsIgnoreCase("undefined")) {
				status = 1 + "";
			}
			Status status2 = new Status();
			status2.setStatusId(Integer.parseInt(status));

			Criteria criteria = session.createCriteria(FeedBackTypeTable.class);

			FeedBackTypeTable typeTable = (FeedBackTypeTable) session.get(FeedBackTypeTable.class,
					Integer.parseInt(feedbackType));

			/*
			 * UserModel userModel = (UserModel)
			 * servletRequest.getSession().getAttribute("UserFrom");
			 * 
			 * userModel = (UserModel)
			 * servletRequest.getSession().getAttribute("UserTo");
			 */

			String hql = "select max(f.feedbackCounter) from FeedBackChatingCounter f where f.so_number =:so_number AND f.feedBackType =:feedBackType "
					+ " AND f.userModelFrom =:userModelFrom AND f.userModelTo=:userModelTo";
			Query query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", userFrom);
			query.setParameter("userModelTo", userTo);
			List results = query.list();

			query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", userTo);
			query.setParameter("userModelTo", userFrom);
			List results1 = query.list();

			/*
			 * int userf=Integer.parseInt(results.get(0)+""); int
			 * usert=Integer.parseInt(results1.get(0)+"");
			 */

			int usert = 0;
			int userf = 0;
			try {
				usert = Integer.parseInt(results1.get(0) + "");
			} catch (Exception e) {

			}
			try {
				userf = Integer.parseInt(results.get(0) + "");
			} catch (Exception e) {

			}

			FeedBackChatingCounter FeedchatingCount = new FeedBackChatingCounter();
			int count = 0;
			if (userf == usert) {
				count = userf + 1;
				// FeedchatingCount.setFeedbackCounter(count);
				FeedchatingCount.setSo_number(soNumber);
				FeedchatingCount.setFeedBackType(typeTable);
				FeedchatingCount.setUserModelFrom(userFrom);
				FeedchatingCount.setUserModelTo(userTo);
				FeedchatingCount.setCount(1);
			} else if (userf > usert) {
				criteria = session.createCriteria(FeedBackChatingCounter.class);
				criteria.add(Restrictions.eq("feedbackCounter", userf));
				FeedBackChatingCounter chatingCounter = (FeedBackChatingCounter) criteria.uniqueResult();
				count = userf + 1;
				// FeedchatingCount.setFeedbackCounter(count);
				FeedchatingCount.setSo_number(chatingCounter.getSo_number());
				FeedchatingCount.setFeedBackType(chatingCounter.getFeedBackType());
				FeedchatingCount.setUserModelFrom(userFrom);
				FeedchatingCount.setUserModelTo(userTo);
				FeedchatingCount.setCount(chatingCounter.getCount() + 1);

			} else if (usert > userf) {
				criteria = session.createCriteria(FeedBackChatingCounter.class);
				criteria.add(Restrictions.eq("feedbackCounter", usert));
				FeedBackChatingCounter chatingCounter = (FeedBackChatingCounter) criteria.uniqueResult();
				count = usert + 1;
				// FeedchatingCount.setFeedbackCounter(count+1);
				FeedchatingCount.setSo_number(chatingCounter.getSo_number());
				FeedchatingCount.setFeedBackType(chatingCounter.getFeedBackType());
				FeedchatingCount.setUserModelFrom(userFrom);
				FeedchatingCount.setUserModelTo(userTo);
				FeedchatingCount.setCount(chatingCounter.getCount() + 1);
			}

			Operator operator = (Operator) session.get(Operator.class, Integer.parseInt(oper));

			Transaction transaction = session.beginTransaction();
			session.save(FeedchatingCount);

			Feedback feedback = new Feedback();
			feedback.setFeedbackText(chatBox);
			feedback.setCounter(FeedchatingCount);
			feedback.setSoNumber(soNumber);
			feedback.setFeedBackType(typeTable);
			feedback.setOperator(operator);
			feedback.setFdate(new Date());
			feedback.setAttachment(Attachment);
			feedback.setEmailSubject(subject);

			if (Integer.parseInt(status) == 2) {
				Session session1 = sessionFactory.openSession();
				String hql1 = "update Feedback set status = :status where soNumber =:soNumber AND feedBackType =:feedBackType "
						+ " AND userModelFrom =:userModelFrom AND userModelTo=:userModelTo";
				Query query1 = session.createQuery(hql1);
				query1.setParameter("status", status2);
				query1.setParameter("feedBackType", typeTable);
				query1.setParameter("soNumber", soNumber);
				query1.setParameter("userModelFrom", userFrom);
				query1.setParameter("userModelTo", userTo);
				query1.executeUpdate();

				hql1 = "update Feedback f set status = :status where soNumber =:soNumber AND feedBackType =:feedBackType "
						+ " AND userModelFrom =:userModelFrom AND userModelTo=:userModelTo";
				query1 = session.createQuery(hql1);
				query1.setParameter("status", status2);
				query1.setParameter("feedBackType", typeTable);
				query1.setParameter("soNumber", soNumber);
				query1.setParameter("userModelFrom", userTo);
				query1.setParameter("userModelTo", userFrom);
				query1.executeUpdate();
				session1.close();
			}
			feedback.setStatus(status2);
			feedback.setUserModelFrom(userFrom);
			feedback.setUserModelTo(userTo);
			feedback.setUserfeedback(userFeedback);

			session.save(feedback);
			transaction.commit();

			
			final Feedback feedback2 =feedback;
			
			
			new Thread() {
				public void run() {
					 List<UserModel> userModels = loginService
							.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userFrom.getCompany());
					userModels.addAll(loginService.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userTo.getCompany()));
					userModels.remove(userFeedback);
					 FcmNotification fcmNotification = notificationService.getFcmNotification();
					notificationService.sendFeedbackNotification(feedback2, fcmNotification, userModels, servletRequest);
				
				}
			}.start();
			// save feedcom preferance accoding to date

			try {
				org.hibernate.Session sessionforFeedBackSonumberSorting = sessionFactory.openSession();
				Transaction transactionsessionforFeedBackSonumberSorting = sessionforFeedBackSonumberSorting
						.beginTransaction();
				Criteria criteriasessionforFeedBackSonumberSorting = sessionforFeedBackSonumberSorting
						.createCriteria(FeedBackSonumberSortingByDate.class);
				criteriasessionforFeedBackSonumberSorting.add(Restrictions.eq("soNumber", feedback.getSoNumber()));
				criteriasessionforFeedBackSonumberSorting
						.add(Restrictions.eq("feedBackType", feedback.getFeedBackType()));

				FeedBackSonumberSortingByDate sortingByDate = (FeedBackSonumberSortingByDate) criteriasessionforFeedBackSonumberSorting
						.uniqueResult();
				if (sortingByDate != null) {
					sortingByDate.setFdate(feedback.getFdate());
					sortingByDate.setSoNumber(feedback.getSoNumber());
					sortingByDate.setFeedBackType(feedback.getFeedBackType());
					sortingByDate.setUserModelFrom(feedback.getUserModelFrom());
					sortingByDate.setUserModelTo(feedback.getUserModelTo());
					sessionforFeedBackSonumberSorting.saveOrUpdate(sortingByDate);
				} else {
					sortingByDate = new FeedBackSonumberSortingByDate();
					sortingByDate.setFdate(feedback.getFdate());
					sortingByDate.setSoNumber(feedback.getSoNumber());
					sortingByDate.setFeedBackType(feedback.getFeedBackType());
					sortingByDate.setUserModelFrom(feedback.getUserModelFrom());
					sortingByDate.setUserModelTo(feedback.getUserModelTo());
					sessionforFeedBackSonumberSorting.saveOrUpdate(sortingByDate);
				}
				transactionsessionforFeedBackSonumberSorting.commit();
				sessionforFeedBackSonumberSorting.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

			// end save feedcom preferance accoding to date

			hql = "select max(f.feedbackCounter) from FeedBackChatingCounter f where f.so_number =:so_number AND f.feedBackType =:feedBackType "
					+ " AND f.userModelFrom =:userModelFrom AND f.userModelTo=:userModelTo";
			query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", userFrom);
			query.setParameter("userModelTo", userTo);
			results = query.list();

			query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", userTo);
			query.setParameter("userModelTo", userFrom);
			results1 = query.list();

			usert = 0;
			userf = 0;
			try {
				usert = Integer.parseInt(results1.get(0) + "");
			} catch (Exception e) {

			}
			try {
				userf = Integer.parseInt(results.get(0) + "");
			} catch (Exception e) {

			}

			return userf + "#" + usert;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "";

	}

	public String getMaxOfFeedBackChattingCounter(String soNumber, String feedBacktype) {
		Session session = sessionFactory.openSession();
		try {

			Criteria criteria = session.createCriteria(FeedBackTypeTable.class);
			List<FeedBackTypeTable> list = criteria.list();
			FeedBackTypeTable typeTable = new FeedBackTypeTable();
			typeTable.setId(Integer.parseInt(feedBacktype));

			String hql = "select max(f.feedbackCounter) from FeedBackChatingCounter f where f.so_number =:so_number AND f.feedBackType =:feedBackType "
					+ " AND f.userModelFrom =:userModelFrom AND f.userModelTo=:userModelTo";
			Query query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", (UserModel) servletRequest.getSession().getAttribute("UserFrom"));
			query.setParameter("userModelTo", (UserModel) servletRequest.getSession().getAttribute("UserTo"));
			List results = query.list();

			query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", (UserModel) servletRequest.getSession().getAttribute("UserTo"));
			query.setParameter("userModelTo", (UserModel) servletRequest.getSession().getAttribute("UserFrom"));
			List results1 = query.list();
			int userf = 0;
			int usert = 0;
			try {
				if (results.size() > 0) {
					userf = Integer.parseInt(results.get(0) + "");
				}
			} catch (Exception e) {

			}
			try {
				if (results1.size() > 0) {
					usert = Integer.parseInt(results1.get(0) + "");
				}
			} catch (Exception e) {

			}

			return userf + "#" + usert;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "";

	}

	public List<Feedback> getLiveChatUserTo(String soNumber, String feedbackType, String userToCounter,
			HttpServletRequest request) {

		Session session = sessionFactory.openSession();
		try {

			Criteria criteria = session.createCriteria(FeedBackTypeTable.class);
			List<FeedBackTypeTable> list = criteria.list();
			FeedBackTypeTable typeTable = null;
			for (FeedBackTypeTable table : list) {
				if (table.getFeedbackType().equalsIgnoreCase(feedbackType)) {
					typeTable = table;
					break;
				}
			}

			if (userToCounter.isEmpty()) {
				userToCounter = "0";

			}

			String hql = "from FeedBackChatingCounter f where f.so_number =:so_number AND f.feedBackType =:feedBackType "
					+ "AND f.userModelFrom=:userModelFrom AND f.feedbackCounter >:feedbackCounter ";

			Query query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", (UserModel) request.getSession().getAttribute("UserTo"));
			query.setParameter("feedbackCounter", Integer.parseInt(userToCounter));
			UserModel n = (UserModel) request.getSession().getAttribute("UserTo");

			List<FeedBackChatingCounter> results = query.list();

			List<Feedback> feedBacks = new ArrayList<Feedback>();

			for (FeedBackChatingCounter counter : results) {
				if (counter.getFeedbackCounter() > Integer.parseInt(userToCounter)) {
					criteria = session.createCriteria(Feedback.class);
					criteria.add(Restrictions.eq("soNumber", soNumber));
					criteria.add(Restrictions.eq("feedBackType", typeTable));
					criteria.add(Restrictions.eq("counter", counter));
					criteria.add(
							Restrictions.eq("userModelFrom", (UserModel) request.getSession().getAttribute("UserTo")));
					criteria.add(
							Restrictions.eq("userModelTo", (UserModel) request.getSession().getAttribute("UserFrom")));
					if (criteria.list().size() > 0) {
						feedBacks.add((Feedback) criteria.uniqueResult());
					}
				}

			}

			return feedBacks;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;

	}

	@Override
	public Feedback saveNewFeedComChat(String soNumber, String feebackType, String Chating, String operId,
			String attachment, String subject, HttpServletRequest request) {
		Session session = sessionFactory.openSession();
		try {

			Status status2 = new Status();
			status2.setStatusId(1);

			FeedBackTypeTable typeTable = new FeedBackTypeTable();
			typeTable.setId(Integer.parseInt(feebackType));

			UserModel userModel = (UserModel) servletRequest.getSession().getAttribute("UserFrom");

			userModel = (UserModel) servletRequest.getSession().getAttribute("UserTo");

			FeedBackChatingCounter FeedchatingCount = new FeedBackChatingCounter();

			// FeedchatingCount.setFeedbackCounter(count);
			FeedchatingCount.setSo_number(soNumber);
			FeedchatingCount.setFeedBackType(typeTable);
			FeedchatingCount.setUserModelFrom((UserModel) servletRequest.getSession().getAttribute("UserFrom"));
			FeedchatingCount.setUserModelTo((UserModel) servletRequest.getSession().getAttribute("UserTo"));
			FeedchatingCount.setCount(1);

			Operator operator = new Operator();
			//operator.setOperatorId(Integer.parseInt(operId));
			operator=operatorService.getOperatorById(Integer.parseInt(operId));
			Transaction transaction = session.beginTransaction();
			session.save(FeedchatingCount);

			Feedback feedback = new Feedback();
			feedback.setFeedbackText(Chating);
			feedback.setCounter(FeedchatingCount);
			feedback.setSoNumber(soNumber);
			feedback.setFeedBackType(typeTable);
			feedback.setOperator(operator);
			feedback.setStatus(status2);
			feedback.setFdate(new Date());
			feedback.setAttachment(attachment);
			feedback.setEmailSubject(subject);

			feedback.setUserModelFrom((UserModel) servletRequest.getSession().getAttribute("UserFrom"));
			feedback.setUserModelTo((UserModel) servletRequest.getSession().getAttribute("UserTo"));
			feedback.setUserfeedback((UserModel) servletRequest.getSession().getAttribute("UserMessage"));
			session.save(feedback);

			// save feedcom preferance accoding to date
			FeedBackSonumberSortingByDate sortingByDate = new FeedBackSonumberSortingByDate();
			sortingByDate.setFdate(feedback.getFdate());
			sortingByDate.setSoNumber(feedback.getSoNumber());
			sortingByDate.setFeedBackType(feedback.getFeedBackType());
			sortingByDate.setUserModelFrom(feedback.getUserModelFrom());
			sortingByDate.setUserModelTo(feedback.getUserModelTo());
			sortingByDate.setFeedback(feedback);
			session.save(sortingByDate);
			// end save feedcom preferance accoding to date

			transaction.commit();

			return feedback;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public String validateNewFeedComChating(String soNumber, String feebackType, HttpServletRequest request) {
		Session session = sessionFactory.openSession();
		try {

			FeedBackTypeTable typeTable = new FeedBackTypeTable();
			typeTable.setId(Integer.parseInt(feebackType));
			System.out.println(soNumber + " val  " + feebackType);
			String hql = "select max(f.feedbackCounter) from FeedBackChatingCounter f where f.so_number =:so_number AND f.feedBackType =:feedBackType "
					+ " AND f.userModelFrom =:userModelFrom AND f.userModelTo=:userModelTo";
			Query query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", (UserModel) servletRequest.getSession().getAttribute("UserFrom"));
			query.setParameter("userModelTo", (UserModel) servletRequest.getSession().getAttribute("UserTo"));
			List results = query.list();

			query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", (UserModel) servletRequest.getSession().getAttribute("UserTo"));
			query.setParameter("userModelTo", (UserModel) servletRequest.getSession().getAttribute("UserFrom"));
			List results1 = query.list();

			System.out.println(results + "   " + results1);
			if (results.get(0) == null && results1.get(0) == null) {
				System.out.println("sdfgsf");
				return "NotAvailable";
			}
			if (results.size() > 0) {
				return "alreadyAvailable";
			}
			if (results1.size() > 0) {
				return "alreadyAvailable";
			}
			return "NotAvailable";
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "";
	}

	@Override
	public void saveRedFlag(String soNumber, String feebackType, HttpServletRequest request) {
		Session session = sessionFactory.openSession();
		try {

			FeedBackTypeTable typeTable = new FeedBackTypeTable();
			typeTable.setId(Integer.parseInt(feebackType));
			RedFlag flag = new RedFlag();
			flag.setSoNumber(soNumber);
			flag.setFeedBackType(typeTable);
			flag.setStatus("Active");
			UserModel userModel = (UserModel) request.getSession().getAttribute("UserFrom");
			Company company = userModel.getCompany();
			flag.setCompany(company);
			Transaction tx = session.beginTransaction();
			session.save(flag);
			tx.commit();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}

	}

	@Override
	public List<RedFlag> getListOfRedFlag(String feebackType, HttpServletRequest request) {
		Session session = sessionFactory.openSession();
		try {
			FeedBackTypeTable typeTable = new FeedBackTypeTable();
			typeTable.setId(Integer.parseInt(feebackType));

			UserModel userModelFro = (UserModel) servletRequest.getSession().getAttribute("UserTo");
			UserModel userModelTo = (UserModel) servletRequest.getSession().getAttribute("UserFrom");
			Criteria criteria = session.createCriteria(RedFlag.class);
			criteria.add(Restrictions.eq("feedBackType", typeTable));
			criteria.add(Restrictions.eq("company", userModelFro.getCompany()));
			List<RedFlag> redflagCompany = criteria.list();

			criteria = session.createCriteria(RedFlag.class);
			criteria.add(Restrictions.eq("feedBackType", typeTable));
			criteria.add(Restrictions.eq("company", userModelTo.getCompany()));
			List<RedFlag> redflagCompanyTo = criteria.list();
			criteria.add(Restrictions.eq("company", userModelTo.getCompany()));

			redflagCompany.addAll(redflagCompanyTo);
			return redflagCompany;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public String validateFlag(String soNumber, String feebackType, HttpServletRequest request) {
		Session session = sessionFactory.openSession();
		try {
			FeedBackTypeTable typeTable = new FeedBackTypeTable();
			typeTable.setId(Integer.parseInt(feebackType));

			UserModel userModelFro = (UserModel) servletRequest.getSession().getAttribute("UserTo");
			UserModel userModelTo = (UserModel) servletRequest.getSession().getAttribute("UserFrom");
			Criteria criteria = session.createCriteria(RedFlag.class);
			criteria.add(Restrictions.eq("feedBackType", typeTable));
			criteria.add(Restrictions.eq("soNumber", soNumber));
			criteria.add(Restrictions.eq("company", userModelFro.getCompany()));

			List<RedFlag> redflagCompany = criteria.list();

			criteria = session.createCriteria(RedFlag.class);
			criteria.add(Restrictions.eq("feedBackType", typeTable));
			criteria.add(Restrictions.eq("soNumber", soNumber));
			criteria.add(Restrictions.eq("company", userModelTo.getCompany()));
			List<RedFlag> redflagCompanyTo = criteria.list();

			int redf = 0;
			int redft = 0;
			try {
				redf = redflagCompany.size();
				redft = redflagCompanyTo.size();
			} catch (Exception e) {

			}

			if (redf == 0 && redft == 0) {
				return "NotAvailable";
			} else {
				return "Available";
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public int getfeedBackTypeIdByFeedBackName(String feedbackName) {
		Session session = sessionFactory.openSession();

		Criteria criteria = session.createCriteria(FeedBackTypeTable.class);
		List<FeedBackTypeTable> list = criteria.list();
		FeedBackTypeTable typeTable = null;
		for (FeedBackTypeTable table : list) {
			if (table.getFeedbackType().equalsIgnoreCase(feedbackName)) {
				typeTable = table;
				break;
			}
		}
		return typeTable.getId();

	}

	@Override
	public void deleteFlag(String soNumber, String feebackType, HttpServletRequest request) {
		Session session = sessionFactory.openSession();
		try {
			FeedBackTypeTable typeTable = new FeedBackTypeTable();
			typeTable.setId(Integer.parseInt(feebackType));

			UserModel userModelFro = (UserModel) servletRequest.getSession().getAttribute("UserTo");
			UserModel userModelTo = (UserModel) servletRequest.getSession().getAttribute("UserFrom");

			Transaction tx = session.beginTransaction();

			RedFlag flag = new RedFlag();
			flag.setFeedBackType(typeTable);
			flag.setCompany(userModelFro.getCompany());
			flag.setSoNumber(soNumber);
			Criteria criteria = session.createCriteria(RedFlag.class);
			criteria.add(Restrictions.eq("feedBackType", typeTable));
			criteria.add(Restrictions.eq("soNumber", soNumber));
			criteria.add(Restrictions.eq("company", userModelFro.getCompany()));
			List<RedFlag> redflagCompany = criteria.list();

			criteria = session.createCriteria(RedFlag.class);
			criteria.add(Restrictions.eq("feedBackType", typeTable));
			criteria.add(Restrictions.eq("soNumber", soNumber));
			criteria.add(Restrictions.eq("company", userModelTo.getCompany()));
			List<RedFlag> redflagCompanyto = criteria.list();
			try {
				if (redflagCompany.get(0) != null) {
					session.delete((redflagCompany.get(0)));

				}
			} catch (Exception e) {

			}
			try {
				if (redflagCompanyto.get(0) != null) {
					session.delete((redflagCompanyto.get(0)));
				}
			} catch (Exception e) {

			}

			tx.commit();
		} catch (Exception exception) {
			exception.printStackTrace();
		}
	}

	@Override
	public String getFeedBackNameUsingFeedBackId(int id) {
		Session session = sessionFactory.openSession();
		try {
			Criteria criteria = session.createCriteria(FeedBackTypeTable.class);
			criteria.add(Restrictions.eq("id", id));
			FeedBackTypeTable typeTable = (FeedBackTypeTable) criteria.uniqueResult();
			return typeTable.getFeedbackType();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public List<Feedback> getClosedfeedBackListForFeedBackId(FeedBackTypeTable feedBackTypeTables,
			HttpServletRequest request) {

		Session session = sessionFactory.openSession();
		try {

			HttpSession httpSession = request.getSession();
			UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
			UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");
			Status status = new Status();
			status.setStatusId(2);

			String hql = "SELECT DISTINCT soNumber FROM Feedback where feedBackType =:feedBackType AND status =:status ";
			Query query = session.createQuery(hql);
			query.setParameter("feedBackType", feedBackTypeTables);
			query.setParameter("status", status);
			List<String> results = query.list();

			List<Feedback> feed = new ArrayList<Feedback>();
			for (String sono : results) {
				Criteria criteria = session.createCriteria(Feedback.class);
				criteria.add(Restrictions.eq("soNumber", sono));
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTables));
				criteria.add(Restrictions.eq("status", status));
				criteria.add(Restrictions.eq("userModelFrom", userModelFro));
				criteria.add(Restrictions.eq("userModelTo", userModelTo));
				List<Feedback> list = criteria.list();

				criteria = session.createCriteria(Feedback.class);
				criteria.add(Restrictions.eq("soNumber", sono));
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTables));
				criteria.add(Restrictions.eq("status", status));
				criteria.add(Restrictions.eq("userModelFrom", userModelTo));
				criteria.add(Restrictions.eq("userModelTo", userModelFro));
				List<Feedback> list1 = criteria.list();
				if (list.size() > 0) {
					Feedback feedback = new Feedback();
					feedback.setSoNumber(sono);
					Feedback f = (Feedback) list.get(0);
					feedback.setFeedbackText(f.getFeedbackText());
					feedback.setOperator(f.getOperator());
					feed.add(feedback);
				} else if (list1.size() > 0) {
					Feedback feedback = new Feedback();
					feedback.setSoNumber(sono);
					Feedback f = (Feedback) list1.get(0);
					feedback.setFeedbackText(f.getFeedbackText());
					feedback.setOperator(f.getOperator());
					feed.add(feedback);
				}

			}
			return feed;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;

	}

	@Override
	public List<Feedback> getfeedBackOpenIssueDateCriteriaReport(FeedBackTypeTable feedBackTypeTables,
			HttpServletRequest request, String date1, String date2) {
		Session session = sessionFactory.openSession();

		try {

			HttpSession httpSession = request.getSession();
			UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
			UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");
			Status status = new Status();
			status.setStatusId(1);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			Date Ddate1 = formatter.parse(date1);
			Date Ddate2 = formatter.parse(date2);

			String hql = "SELECT DISTINCT soNumber FROM Feedback where feedBackType =:feedBackType AND status =:status ";
			Query query = session.createQuery(hql);
			query.setParameter("feedBackType", feedBackTypeTables);
			query.setParameter("status", status);
			List<String> results = query.list();

			System.out.println(Ddate1);
			System.out.println(Ddate2);

			List<Feedback> feed = new ArrayList<Feedback>();
			FeedBackChatingCounter chatingCounter = new FeedBackChatingCounter();
			chatingCounter.setCount(1);

			for (String sono : results) {
				Criteria criteria = session.createCriteria(Feedback.class);
				criteria.add(Restrictions.eq("soNumber", sono));
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTables));
				criteria.add(Restrictions.eq("status", status));
				criteria.add(Restrictions.eq("userModelFrom", userModelFro));
				criteria.add(Restrictions.eq("userModelTo", userModelTo));
				criteria.add(Restrictions.between("fdate", Ddate1, Ddate2));
				// criteria.add(Restrictions.eq("counter.count",
				// chatingCounter));
				List<Feedback> list = criteria.list();

				criteria = session.createCriteria(Feedback.class);
				criteria.add(Restrictions.eq("soNumber", sono));
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTables));
				criteria.add(Restrictions.eq("status", status));
				criteria.add(Restrictions.eq("userModelFrom", userModelTo));
				criteria.add(Restrictions.eq("userModelTo", userModelFro));
				criteria.add(Restrictions.between("fdate", Ddate1, Ddate2));
				// criteria.add(Restrictions.eq("counter.count",chatingCounter));

				List<Feedback> list1 = criteria.list();
				try {
					list.addAll(list1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Collections.sort(list, new FeedBackCountComparator());
				Collections.sort(list1, new FeedBackCountComparator());

				if (list.size() > 0) {
					Feedback feedback = new Feedback();
					feedback.setSoNumber(sono);
					Feedback f = (Feedback) list.get(0);
					feedback.setFeedbackText(f.getFeedbackText());
					feedback.setOperator(f.getOperator());
					feedback.setFdate(f.getFdate());
					feedback.setFeedBackType(f.getFeedBackType());
					System.out.println(f.getFdate() + "  f.getFdate()");
					feed.add(feedback);
				} else if (list1.size() > 0) {
					Feedback feedback = new Feedback();
					feedback.setSoNumber(sono);
					Feedback f = (Feedback) list1.get(0);
					feedback.setFeedbackText(f.getFeedbackText());
					feedback.setOperator(f.getOperator());
					feedback.setFdate(f.getFdate());
					feedback.setFeedBackType(f.getFeedBackType());
					System.out.println(f.getFdate() + "  f.getFdate()");
					feed.add(feedback);
				}

			}
			return feed;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;

	}

	@Override
	public List<Feedback> getfeedBackCloseIssueDateCriteriaReport(FeedBackTypeTable feedBackTypeTables,
			HttpServletRequest request, String date1, String date2) {
		Session session = sessionFactory.openSession();

		try {

			HttpSession httpSession = request.getSession();
			UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
			UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");
			Status status = new Status();
			status.setStatusId(2);

			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

			Date Ddate1 = formatter.parse(date1);
			Date Ddate2 = formatter.parse(date2);

			String hql = "SELECT DISTINCT soNumber FROM Feedback where feedBackType =:feedBackType AND status =:status ";
			Query query = session.createQuery(hql);
			query.setParameter("feedBackType", feedBackTypeTables);
			query.setParameter("status", status);
			List<String> results = query.list();

			System.out.println(Ddate1);
			System.out.println(Ddate2);

			List<Feedback> feed = new ArrayList<Feedback>();
			FeedBackChatingCounter chatingCounter = new FeedBackChatingCounter();
			chatingCounter.setCount(1);

			for (String sono : results) {
				Criteria criteria = session.createCriteria(Feedback.class);
				criteria.add(Restrictions.eq("soNumber", sono));
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTables));
				criteria.add(Restrictions.eq("status", status));
				criteria.add(Restrictions.eq("userModelFrom", userModelFro));
				criteria.add(Restrictions.eq("userModelTo", userModelTo));
				criteria.add(Restrictions.between("fdate", Ddate1, Ddate2));
				// criteria.add(Restrictions.eq("counter.count",
				// chatingCounter));
				List<Feedback> list = criteria.list();

				criteria = session.createCriteria(Feedback.class);
				criteria.add(Restrictions.eq("soNumber", sono));
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTables));
				criteria.add(Restrictions.eq("status", status));
				criteria.add(Restrictions.eq("userModelFrom", userModelTo));
				criteria.add(Restrictions.eq("userModelTo", userModelFro));
				criteria.add(Restrictions.between("fdate", Ddate1, Ddate2));
				// criteria.add(Restrictions.eq("counter.count",chatingCounter));

				List<Feedback> list1 = criteria.list();
				try {
					list.addAll(list1);
				} catch (Exception e) {
					e.printStackTrace();
				}
				Collections.sort(list, new FeedBackCountComparator());
				Collections.sort(list1, new FeedBackCountComparator());

				if (list.size() > 0) {
					Feedback feedback = new Feedback();
					feedback.setSoNumber(sono);
					Feedback f = (Feedback) list.get(0);
					feedback.setFeedbackText(f.getFeedbackText());
					feedback.setOperator(f.getOperator());
					feedback.setFdate(f.getFdate());
					feedback.setFeedBackType(f.getFeedBackType());
					System.out.println(f.getFdate() + "  f.getFdate()");
					feed.add(feedback);
				} else if (list1.size() > 0) {
					Feedback feedback = new Feedback();
					feedback.setSoNumber(sono);
					Feedback f = (Feedback) list1.get(0);
					feedback.setFeedbackText(f.getFeedbackText());
					feedback.setOperator(f.getOperator());
					feedback.setFdate(f.getFdate());
					feedback.setFeedBackType(f.getFeedBackType());
					System.out.println(f.getFdate() + "  f.getFdate()");
					feed.add(feedback);
				}

			}
			return feed;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;

	}

	@Override
	public List<String> getListOfUserByCompanyId(Company company) {

		Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(UserModel.class);
		criteria.add(Restrictions.eq("company", company));
		criteria.setProjection(Projections.property("email"));
		List<String> list = new ArrayList<String>();
		list = criteria.list();
		session.close();
		return list;
	}

	@Override
	public List<Feedback> getLiveChatUserFrom(String soNumber, String feedbackType, String userFromCounter,
			HttpServletRequest request) {

		Session session = sessionFactory.openSession();
		try {

			Criteria criteria = session.createCriteria(FeedBackTypeTable.class);
			List<FeedBackTypeTable> list = criteria.list();
			FeedBackTypeTable typeTable = null;
			for (FeedBackTypeTable table : list) {
				if (table.getFeedbackType().equalsIgnoreCase(feedbackType)) {
					typeTable = table;
					break;
				}
			}

			if (userFromCounter.isEmpty()) {
				userFromCounter = "0";

			}

			String hql = "from FeedBackChatingCounter f where f.so_number =:so_number AND f.feedBackType =:feedBackType "
					+ "AND f.userModelFrom=:userModelFrom AND f.feedbackCounter >:feedbackCounter ";

			Query query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", (UserModel) request.getSession().getAttribute("UserFrom"));
			query.setParameter("feedbackCounter", Integer.parseInt(userFromCounter));
			UserModel n = (UserModel) request.getSession().getAttribute("UserFrom");

			List<FeedBackChatingCounter> results = query.list();

			List<Feedback> feedBacks = new ArrayList<Feedback>();

			for (FeedBackChatingCounter counter : results) {
				if (counter.getFeedbackCounter() > Integer.parseInt(userFromCounter)) {
					criteria = session.createCriteria(Feedback.class);
					criteria.add(Restrictions.eq("soNumber", soNumber));
					criteria.add(Restrictions.eq("feedBackType", typeTable));
					criteria.add(Restrictions.eq("counter", counter));
					criteria.add(Restrictions.eq("userModelFrom",
							(UserModel) request.getSession().getAttribute("UserFrom")));
					criteria.add(
							Restrictions.eq("userModelTo", (UserModel) request.getSession().getAttribute("UserTo")));
					if (criteria.list().size() > 0) {
						feedBacks.add((Feedback) criteria.uniqueResult());
					}
				}

			}

			return feedBacks;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;

	}

	@Override
	public List<Feedback> getListOfQueryForCoreflex() {

		Session session = sessionFactory.openSession();
		try {
			Criteria criteria = session.createCriteria(Feedback.class);

			List<Feedback> list = criteria.list();

			return list;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;

	}

	@Override
	public List<Feedback> getListOfFeedBack(UserModel userFrom, UserModel userTo) {

		Session session = sessionFactory.openSession();
		try {
			Criteria criteria = session.createCriteria(Feedback.class);

			criteria.add(Restrictions.eq("userModelFrom", userFrom));
			criteria.add(Restrictions.eq("userModelTo", userTo));
			List<Feedback> list = criteria.list();

			criteria = session.createCriteria(Feedback.class);
			criteria.add(Restrictions.eq("userModelFrom", userTo));
			criteria.add(Restrictions.eq("userModelTo", userFrom));
			List<Feedback> list1 = criteria.list();

			list.addAll(list1);
			Collections.sort(list, new FeedBackCountComparator());

			return list;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;

	}

	@Override
	public List<FeedBackSonumberSortingByDate> getSortedFeedBackListAccordingtToFeedbackId(FeedBackTypeTable typeTable,
			HttpServletRequest request) {

		Session session = sessionFactory.openSession();
		HttpSession httpSession = request.getSession();
		UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
		UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");

		System.out.println("userModelFro  "+userModelFro.getUserId());
		System.out.println("userModelTo  "+userModelTo.getUserId());
		System.out.println("typeTable  "+typeTable.getId());
		
		Criteria criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
		criteria.add(Restrictions.eq("feedBackType", typeTable));
		criteria.add(Restrictions.eq("userModelFrom", userModelFro));
		criteria.add(Restrictions.eq("userModelTo", userModelTo));
		List<FeedBackSonumberSortingByDate> list = criteria.list();
		System.out.println(list.size() + " list.size()");

		criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
		criteria.add(Restrictions.eq("feedBackType", typeTable));
		criteria.add(Restrictions.eq("userModelFrom", userModelTo));
		criteria.add(Restrictions.eq("userModelTo", userModelFro));
		List<FeedBackSonumberSortingByDate> list1 = criteria.list();
		System.out.println(list1.size() + " list1.size()");
		try {
			list.addAll(list1);
		} catch (Exception e) {
			e.printStackTrace();
		}
		Collections.sort(list, new FeedBackSortingCompairator());
		// Collections.sort(list1, new FeedBackSortingCompairator());
		session.close();

		return list;
	}

	@Override
	public List<CompanyFeedBackTypeAssociation> getListOfFeedBackTypeAssociationWithCompanyAccordingToCompanyId(
			String companyId) {

		Company company = new Company();
		company.setCompanyId(Integer.parseInt(companyId));
		Session session = sessionFactory.openSession();

		Criteria criteria = session.createCriteria(CompanyFeedBackTypeAssociation.class);
		criteria.add(Restrictions.eq("company", company));
		List<CompanyFeedBackTypeAssociation> list = criteria.list();

		session.close();

		return list;

	}

	@Override
	public void deleteCompanyFeedBackTypeAssociationByCompany(Company company) {
		Session session = sessionFactory.openSession();
		try {

			Transaction tx = session.beginTransaction();
			String hql = "delete from CompanyAndFeedBackTypeAssociation where company= :company";
			Query query = session.createQuery(hql);
			query.setParameter("company", company);
			query.executeUpdate();
			tx.commit();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			session.close();
		}

	}

	@Override
	public void saveCompanyFeedBackTypeAssociation(CompanyFeedBackTypeAssociation companyFeedBackTypeAssociation) {
		// TODO Auto-generated method stub
		Session session = sessionFactory.openSession();
		Transaction tx = session.beginTransaction();
		session.save(companyFeedBackTypeAssociation);
		tx.commit();
		session.close();
	}

	@Override
	public List<CompanyFeedBackTypeAssociation> getListOfFeedBackTypeAccoringToCompany(Company company) {

		Session session = sessionFactory.openSession();
		try {
			Criteria criteria = session.createCriteria(CompanyFeedBackTypeAssociation.class);
			criteria.add(Restrictions.eq("company", company));

			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;

	}

	@Override
	public List<CompanyFeedBackTypeAssociation> getListOfAllCompanyFeedBackTypeAssociation() {
		Session session = sessionFactory.openSession();
		try {
			Criteria criteria = session.createCriteria(CompanyFeedBackTypeAssociation.class);
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	@Override
	public HashMap<FeedBackTypeTable, Long> getCountOfFeedbackSortingAccordingToFeedbackTypeAndLastVisitedDate(
			List<FeedBackTypeTable> feedBackTypeTables, Date lastVisitedDate, UserModel userFrom, UserModel userTo) {
		Session session = sessionFactory.openSession();

		Status status = new Status();
		status.setStatusId(1);

		HashMap<FeedBackTypeTable, Long> hashMapOfFeedbackTypeAndLastVisitedDate = new HashMap<FeedBackTypeTable, Long>();

		for (FeedBackTypeTable feedBackTypeTable : feedBackTypeTables) {
			if (lastVisitedDate != null) {
				Criteria criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
				criteria.createCriteria("feedback", "feed");
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
				criteria.add(Restrictions.eq("feed.userModelFrom", userFrom));
				criteria.add(Restrictions.eq("feed.userModelTo", userTo));
				criteria.add(Restrictions.gt("fdate", lastVisitedDate));

				List<FeedBackSonumberSortingByDate> list = criteria.list();

				criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
				criteria.createCriteria("feedback", "feed");
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
				criteria.add(Restrictions.eq("feed.userModelFrom", userTo));
				criteria.add(Restrictions.eq("feed.userModelTo", userFrom));
				criteria.add(Restrictions.gt("fdate", lastVisitedDate));
				List<FeedBackSonumberSortingByDate> list1 = criteria.list();

				try {
					list.addAll(list1);
					if (list.size() > 0) {
						hashMapOfFeedbackTypeAndLastVisitedDate.put(feedBackTypeTable, (long) list.size());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			} else {
				Criteria criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
				criteria.createCriteria("feedback", "feed");
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
				criteria.add(Restrictions.eq("feed.userModelFrom", userFrom));
				criteria.add(Restrictions.eq("feed.userModelTo", userTo));

				List<FeedBackSonumberSortingByDate> list = criteria.list();

				criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
				criteria.createCriteria("feedback", "feed");
				criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
				criteria.add(Restrictions.eq("feed.userModelFrom", userTo));
				criteria.add(Restrictions.eq("feed.userModelTo", userFrom));
				List<FeedBackSonumberSortingByDate> list1 = criteria.list();

				try {
					list.addAll(list1);
					if (list.size() > 0) {
						hashMapOfFeedbackTypeAndLastVisitedDate.put(feedBackTypeTable, (long) list.size());
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		}
		session.close();

		return hashMapOfFeedbackTypeAndLastVisitedDate;
	}

	@Override
	public String validateNewFeedComChatingForWebService(String soNumber, String feebackType, UserModel userFrom,
			UserModel userTo) {
		Session session = sessionFactory.openSession();
		try {

			FeedBackTypeTable typeTable = new FeedBackTypeTable();
			typeTable.setId(Integer.parseInt(feebackType));
			System.out.println(soNumber + " val  " + feebackType);
			String hql = "select max(f.feedbackCounter) from FeedBackChatingCounter f where f.so_number =:so_number AND f.feedBackType =:feedBackType "
					+ " AND f.userModelFrom =:userModelFrom AND f.userModelTo=:userModelTo";
			Query query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", userFrom);
			query.setParameter("userModelTo", userTo);
			List results = query.list();

			query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", userTo);
			query.setParameter("userModelTo", userFrom);
			List results1 = query.list();

			System.out.println(results + "   " + results1);
			if (results.get(0) == null && results1.get(0) == null) {
				System.out.println("sdfgsf");
				return "NotAvailable";
			}
			if (results.size() > 0) {
				return "alreadyAvailable";
			}
			if (results1.size() > 0) {
				return "alreadyAvailable";
			}
			return "NotAvailable";
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "";
	}

	@Override
	public String saveNewFeedComChatForWebService(String soNumber, String feebackType, String Chating, String operId,
			String attachment, String subject, final UserModel userFrom, final UserModel userTo, final UserModel userFeedback) {
		Session session = sessionFactory.openSession();
		try {

			Status status2 = new Status();
			status2.setStatusId(1);

			FeedBackTypeTable typeTable = new FeedBackTypeTable();
			typeTable.setId(Integer.parseInt(feebackType));

			FeedBackChatingCounter FeedchatingCount = new FeedBackChatingCounter();

			// FeedchatingCount.setFeedbackCounter(count);
			FeedchatingCount.setSo_number(soNumber);
			FeedchatingCount.setFeedBackType(typeTable);
			FeedchatingCount.setUserModelFrom(userFrom);
			FeedchatingCount.setUserModelTo(userTo);
			FeedchatingCount.setCount(1);

			Operator operator = new Operator();
			operator.setOperatorId(Integer.parseInt(operId));
			Transaction transaction = session.beginTransaction();
			session.save(FeedchatingCount);

			Feedback feedback = new Feedback();
			feedback.setFeedbackText(Chating);
			feedback.setCounter(FeedchatingCount);
			feedback.setSoNumber(soNumber);
			feedback.setFeedBackType(typeTable);
			feedback.setOperator(operator);
			feedback.setStatus(status2);
			feedback.setFdate(new Date());
			feedback.setAttachment(attachment);
			feedback.setEmailSubject(subject);

			feedback.setUserModelFrom(userFrom);
			feedback.setUserModelTo(userTo);
			feedback.setUserfeedback(userFeedback);
			session.save(feedback);
			
			final Feedback feedback2 =feedback;
			
			
			new Thread() {
				public void run() {
					 List<UserModel> userModels = loginService
							.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userFrom.getCompany());
					userModels.addAll(loginService.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userTo.getCompany()));
					userModels.remove(userFeedback);
					 FcmNotification fcmNotification = notificationService.getFcmNotification();
					notificationService.sendFeedbackNotification(feedback2, fcmNotification, userModels, servletRequest);
				
				}
			}.start();

			// save feedcom preferance accoding to date
			FeedBackSonumberSortingByDate sortingByDate = new FeedBackSonumberSortingByDate();
			sortingByDate.setFdate(feedback.getFdate());
			sortingByDate.setSoNumber(feedback.getSoNumber());
			sortingByDate.setFeedBackType(feedback.getFeedBackType());
			sortingByDate.setUserModelFrom(feedback.getUserModelFrom());
			sortingByDate.setUserModelTo(feedback.getUserModelTo());
			sortingByDate.setFeedback(feedback);
			session.save(sortingByDate);
			// end save feedcom preferance accoding to date

			transaction.commit();

			return feedback.getCounter().getFeedbackCounter() + "/" + feedback.getCounter().getCount();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "error";
	}

	@Override
	public List<Feedback> getListOfChatingUsingSoNumberAndFeedBackTypeForWebservice(String soNumber,
			String feedbackType, UserModel userFrom, UserModel userTo) {
		Session session = sessionFactory.openSession();
		try {

			Status status = new Status();
			status.setStatusId(1);

			FeedBackTypeTable feedBackTypeTable = new FeedBackTypeTable();
			feedBackTypeTable.setId(Integer.parseInt(feedbackType));

			Criteria criteria = session.createCriteria(Feedback.class);
			criteria.add(Restrictions.eq("soNumber", soNumber));
			criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
			criteria.add(Restrictions.eq("status", status));
			criteria.add(Restrictions.eq("userModelFrom", userFrom));
			criteria.add(Restrictions.eq("userModelTo", userTo));
			List<Feedback> list = criteria.list();

			criteria = session.createCriteria(Feedback.class);
			criteria.add(Restrictions.eq("soNumber", soNumber));
			criteria.add(Restrictions.eq("feedBackType", feedBackTypeTable));
			criteria.add(Restrictions.eq("status", status));
			criteria.add(Restrictions.eq("userModelFrom", userTo));
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
	public List<Object[]> getDistincYear(HttpServletRequest request, FeedBackTypeTable type) {
		Session session = sessionFactory.openSession();
		HttpSession httpSession = request.getSession();

		UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
		UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");

		String sql = "SELECT count(*), YEAR(date) FROM FeedBackSonumberSortingByDate where userModelFrom =:userModelFrom AND userModelTo =:userModelTo AND feedBackType =:feedBackType group by YEAR(date)";
		Query query = session.createQuery(sql);

		query.setParameter("userModelFrom", userModelFro);
		query.setParameter("userModelTo", userModelTo);
		query.setParameter("feedBackType", type);
		List<Object[]> dateList = query.list();

		query.setParameter("userModelFrom", userModelTo);
		query.setParameter("userModelTo", userModelFro);
		query.setParameter("feedBackType", type);
		List<Object[]> dateList1 = query.list();

		dateList.addAll(dateList1);

		return dateList;
	}

	@Override
	public HashMap<String, List<Object[]>> getMonthWiseOpenIssues(String year, String feedId,
			HttpServletRequest request) {

		int feedbackID = Integer.parseInt(feedId);

		FeedBackTypeTable type = new FeedBackTypeTable();
		type.setId(feedbackID);

		Session session = sessionFactory.openSession();
		HttpSession httpSession = request.getSession();

		UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
		UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");

		String sql = "select count(*),month(date) from FeedBackSonumberSortingByDate where feedback_close is null  and YEAR(date)=:year AND  userModelFrom =:userModelFrom AND userModelTo =:userModelTo AND feedBackType =:feedBackType group by month(date) order by month(date)";
		Query query = session.createQuery(sql);

		query.setParameter("year", Integer.parseInt(year));
		query.setParameter("userModelFrom", userModelFro);
		query.setParameter("userModelTo", userModelTo);
		query.setParameter("feedBackType", type);
		List<Object[]> dateList = query.list();

		query.setParameter("year", Integer.parseInt(year));
		query.setParameter("userModelFrom", userModelTo);
		query.setParameter("userModelTo", userModelFro);
		query.setParameter("feedBackType", type);
		List<Object[]> dateList1 = query.list();

		/*
		 * System.out.println("################################################"
		 * ); for (Object[] aRow : dateList1) { Long sum = (Long) aRow[0];
		 * Integer category = (Integer) aRow[1]; System.out.println(category +
		 * " - " + sum); }
		 */

		dateList.addAll(dateList1);

		sql = "select count(*),month(date) from FeedBackSonumberSortingByDate where feedback_close is not null  and YEAR(date)=:year AND  userModelFrom =:userModelFrom AND userModelTo =:userModelTo AND feedBackType =:feedBackType group by month(date) order by month(date)";
		query = session.createQuery(sql);

		query.setParameter("year", Integer.parseInt(year));
		query.setParameter("userModelFrom", userModelFro);
		query.setParameter("userModelTo", userModelTo);
		query.setParameter("feedBackType", type);
		List<Object[]> closeList = query.list();

		query.setParameter("year", Integer.parseInt(year));
		query.setParameter("userModelFrom", userModelTo);
		query.setParameter("userModelTo", userModelFro);
		query.setParameter("feedBackType", type);
		List<Object[]> closeList1 = query.list();

		closeList.addAll(closeList1);
		HashMap<String, List<Object[]>> result = new HashMap<String, List<Object[]>>();
		result.put("open", dateList);
		result.put("close", closeList);
		session.close();

		return result;
	}

	@Override
	public HashMap<String, List<Object[]>> getdatewiseOpenCloseIssues(String year, String month, String feedId,
			HttpServletRequest request) {

		int feedbackID = Integer.parseInt(feedId);

		FeedBackTypeTable type = new FeedBackTypeTable();
		type.setId(feedbackID);

		Session session = sessionFactory.openSession();
		HttpSession httpSession = request.getSession();

		UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
		UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");

		String sql = "select count(*),day(date) from FeedBackSonumberSortingByDate where feedback_close is null and Month(date) =:month and YEAR(date)=:year AND  userModelFrom =:userModelFrom AND userModelTo =:userModelTo AND feedBackType =:feedBackType group by day(date) order by day(date)";
		Query query = session.createQuery(sql);
		query.setParameter("month", Integer.parseInt(month));
		query.setParameter("year", Integer.parseInt(year));
		query.setParameter("userModelFrom", userModelFro);
		query.setParameter("userModelTo", userModelTo);
		query.setParameter("feedBackType", type);
		List<Object[]> openList = query.list();

		query.setParameter("month", Integer.parseInt(month));
		query.setParameter("year", Integer.parseInt(year));
		query.setParameter("userModelFrom", userModelTo);
		query.setParameter("userModelTo", userModelFro);
		query.setParameter("feedBackType", type);
		List<Object[]> openList1 = query.list();

		openList.addAll(openList1);

		sql = "select count(*),day(date) from FeedBackSonumberSortingByDate where feedback_close is not null and Month(date) =:month and YEAR(date)=:year AND  userModelFrom =:userModelFrom AND userModelTo =:userModelTo AND feedBackType =:feedBackType group by day(date) order by day(date)";
		query = session.createQuery(sql);
		query.setParameter("month", Integer.parseInt(month));
		query.setParameter("year", Integer.parseInt(year));
		query.setParameter("userModelFrom", userModelFro);
		query.setParameter("userModelTo", userModelTo);
		query.setParameter("feedBackType", type);
		List<Object[]> closeList = query.list();
		/*
		 * System.out.println(
		 * "################################################ closeList"); for
		 * (Object[] aRow : closeList) { Long sum = (Long) aRow[0]; Integer
		 * category = (Integer) aRow[1]; System.out.println(category + " - " +
		 * sum); }
		 */
		query.setParameter("month", Integer.parseInt(month));
		query.setParameter("year", Integer.parseInt(year));
		query.setParameter("userModelFrom", userModelTo);
		query.setParameter("userModelTo", userModelFro);
		query.setParameter("feedBackType", type);
		List<Object[]> closeList1 = query.list();

		closeList.addAll(closeList1);

		HashMap<String, List<Object[]>> result = new HashMap<String, List<Object[]>>();
		result.put("open", openList);
		result.put("close", closeList);
		session.close();
		return result;
	}

}