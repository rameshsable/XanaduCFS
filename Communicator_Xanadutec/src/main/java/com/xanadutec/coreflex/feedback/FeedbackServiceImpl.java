package com.xanadutec.coreflex.feedback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.CompanyFeedBackTypeAssociation;
import com.xanadutec.coreflex.model.FeedBackCounterForHome;
import com.xanadutec.coreflex.model.FeedBackSonumberSortingByDate;
import com.xanadutec.coreflex.model.FeedBackTypeTable;
import com.xanadutec.coreflex.model.Feedback;
import com.xanadutec.coreflex.model.Operator;
import com.xanadutec.coreflex.model.RedFlag;
import com.xanadutec.coreflex.model.UserModel;

@Service("FeedbackService")
public class FeedbackServiceImpl implements FeedbackService {

	@Autowired
	private FeedbackDao feedbackDao;

	@Override
	public String addFeedbackTypes(FeedBackTypeTable feedBackTypeTable) {
		return feedbackDao.addFeedbackTypes(feedBackTypeTable);
	}

	public int getfeedBackCount() {

		return feedbackDao.getfeedBackCount();
	}

	public List<FeedBackTypeTable> getFeedbackList() {
		return feedbackDao.getFeedbackList();
	}

	public List<FeedBackCounterForHome> getfeedBackCounterForHome(List<FeedBackTypeTable> feedBackTypeTables,
			HttpServletRequest request) {
		return feedbackDao.getfeedBackCounterForHome(feedBackTypeTables, request);
	}

	@Override
	public List<Feedback> getfeedBackListForFeedBackId(FeedBackTypeTable feedBackTypeTables,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return feedbackDao.getfeedBackListForFeedBackId(feedBackTypeTables, request);
	}

	@Override
	public List<Feedback> getListOfChatingUsingSoNumberAndFeedBackType(String soNumber, String feedbackType,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return feedbackDao.getListOfChatingUsingSoNumberAndFeedBackType(soNumber, feedbackType, request);
	}

	@Override
	public String saveChattingForFeedCom(String soNumber, String status, String subject, String Attachment,
			String chatBox, String feedbackType, String operator) {
		// TODO Auto-generated method stub
		return feedbackDao.saveChattingForFeedCom(soNumber, status, subject, Attachment, chatBox, feedbackType,
				operator);

	}

	public String getMaxOfFeedBackChattingCounter(String soNumber, String feedBacktype) {

		return feedbackDao.getMaxOfFeedBackChattingCounter(soNumber, feedBacktype);
	}

	@Override
	public List<Feedback> getLiveChatUserTo(String soNumber, String feedbackType, String userToCounter,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return feedbackDao.getLiveChatUserTo(soNumber, feedbackType, userToCounter, request);
	}

	@Override
	public Feedback saveNewFeedComChat(String soNumber, String feebackType, String Chating, String operator,
			String attachment, String subject, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return feedbackDao.saveNewFeedComChat(soNumber, feebackType, Chating, operator, attachment, subject, request);
	}

	@Override
	public String validateNewFeedComChating(String soNumber, String feebackType, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return feedbackDao.validateNewFeedComChating(soNumber, feebackType, request);
	}

	@Override
	public void saveRedFlag(String soNumber, String feebackType, HttpServletRequest request) {
		// TODO Auto-generated method stub
		feedbackDao.saveRedFlag(soNumber, feebackType, request);
	}

	@Override
	public List<RedFlag> getListOfRedFlag(String feebackType, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return feedbackDao.getListOfRedFlag(feebackType, request);
	}

	@Override
	public String validateFlag(String soNumber, String feebackType, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return feedbackDao.validateFlag(soNumber, feebackType, request);
	}

	@Override
	public int getfeedBackTypeIdByFeedBackName(String feedbackName) {
		// TODO Auto-generated method stub
		return feedbackDao.getfeedBackTypeIdByFeedBackName(feedbackName);
	}

	@Override
	public void deleteFlag(String soNumber, String feebackType, HttpServletRequest request) {
		feedbackDao.deleteFlag(soNumber, feebackType, request);

	}

	@Override
	public String getFeedBackNameUsingFeedBackId(int id) {
		// TODO Auto-generated method stub
		return feedbackDao.getFeedBackNameUsingFeedBackId(id);
	}

	@Override
	public List<Feedback> getClosedfeedBackListForFeedBackId(FeedBackTypeTable feedBackTypeTables,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return feedbackDao.getClosedfeedBackListForFeedBackId(feedBackTypeTables, request);
	}

	@Override
	public List<Feedback> getClosedFeedComListOfChatingUsingSoNumberAndFeedBackType(String soNumber,
			String feedbackType, HttpServletRequest request) {

		return feedbackDao.getClosedFeedComListOfChatingUsingSoNumberAndFeedBackType(soNumber, feedbackType, request);
	}

	@Override
	public List<Feedback> getfeedBackOpenIssueDateCriteriaReport(FeedBackTypeTable feedBackTypeTables,
			HttpServletRequest request, String date1, String date2) {
		// TODO Auto-generated method stub
		return feedbackDao.getfeedBackOpenIssueDateCriteriaReport(feedBackTypeTables, request, date1, date2);
	}

	@Override
	public List<Feedback> getfeedBackCloseIssueDateCriteriaReport(FeedBackTypeTable feedBackTypeTables,
			HttpServletRequest request, String date1, String date2) {
		// TODO Auto-generated method stub
		return feedbackDao.getfeedBackCloseIssueDateCriteriaReport(feedBackTypeTables, request, date1, date2);
	}

	@Override
	public List<String> getListOfUserByCompanyId(Company company) {
		// TODO Auto-generated method stub
		return feedbackDao.getListOfUserByCompanyId(company);
	}

	@Override
	public List<Feedback> getLiveChatUserFrom(String soNumber, String feedbackType, String userFromCounter,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return feedbackDao.getLiveChatUserFrom(soNumber, feedbackType, userFromCounter, request);
	}

	@Override
	public List<Feedback> getListOfFeedBack(UserModel userFrom, UserModel userTo) {
		// TODO Auto-generated method stub
		return feedbackDao.getListOfFeedBack(userFrom, userTo);
	}

	@Override
	public List<Feedback> getListOfQueryForCoreflex() {
		// TODO Auto-generated method stub
		return feedbackDao.getListOfQueryForCoreflex();
	}

	@Override
	public List<FeedBackSonumberSortingByDate> getSortedFeedBackListAccordingtToFeedbackId(FeedBackTypeTable typeTable,
			HttpServletRequest request) {
		return feedbackDao.getSortedFeedBackListAccordingtToFeedbackId(typeTable, request);
	}

	@Override
	public List<CompanyFeedBackTypeAssociation> getListOfFeedBackTypeAssociationWithCompanyAccordingToCompanyId(
			String companyId) {
		// TODO Auto-generated method stub
		return feedbackDao.getListOfFeedBackTypeAssociationWithCompanyAccordingToCompanyId(companyId);
	}

	@Override
	public void deleteCompanyFeedBackTypeAssociationByCompany(Company company) {
		feedbackDao.deleteCompanyFeedBackTypeAssociationByCompany(company);

	}

	@Override
	public void saveCompanyFeedBackTypeAssociation(CompanyFeedBackTypeAssociation companyFeedBackTypeAssociation) {
		// TODO Auto-generated method stub
		feedbackDao.saveCompanyFeedBackTypeAssociation(companyFeedBackTypeAssociation);
	}

	@Override
	public List<CompanyFeedBackTypeAssociation> getListOfFeedBackTypeAccoringToCompany(Company company) {
		// TODO Auto-generated method stub
		return feedbackDao.getListOfFeedBackTypeAccoringToCompany(company);
	}

	@Override
	public List<CompanyFeedBackTypeAssociation> getListOfAllCompanyFeedBackTypeAssociation() {
		// TODO Auto-generated method stub
		return feedbackDao.getListOfAllCompanyFeedBackTypeAssociation();
	}

	@Override
	public HashMap<FeedBackTypeTable, Long> getCountOfFeedbackSortingAccordingToFeedbackTypeAndLastVisitedDate(
			List<FeedBackTypeTable> feedBackTypeTables, Date lastVisitedDate, UserModel userFrom, UserModel userTo) {
		// TODO Auto-generated method stub
		return feedbackDao.getCountOfFeedbackSortingAccordingToFeedbackTypeAndLastVisitedDate(feedBackTypeTables,
				lastVisitedDate, userFrom, userTo);
	}

	@Override
	public String validateNewFeedComChatingForWebService(String soNumber, String feebackType, UserModel userFrom,
			UserModel userTo) {
		// TODO Auto-generated method stub
		return feedbackDao.validateNewFeedComChatingForWebService(soNumber, feebackType, userFrom, userTo);
	}

	@Override
	public String saveNewFeedComChatForWebService(String soNumber, String feebackType, String Chating, String operator,
			String attachment, String subject, UserModel userFrom, UserModel userTo, UserModel userFeedback) {
		// TODO Auto-generated method stub
		return feedbackDao.saveNewFeedComChatForWebService(soNumber, feebackType, Chating, operator, attachment,
				subject, userFrom, userTo, userFeedback);
	}

	@Override
	public String saveChattingFeedComForWebService(String soNumber, String status, String subject, String Attachment,
			String chatBox, String feedbackType, String oper, UserModel userFrom, UserModel userTo,
			UserModel userFeedback) {
		return feedbackDao.saveChattingFeedComForWebService(soNumber, status, subject, Attachment, chatBox,
				feedbackType, oper, userFrom, userTo, userFeedback);
	}

	@Override
	public List<Feedback> getListOfChatingUsingSoNumberAndFeedBackTypeForWebservice(String soNumber,
			String feedbackType, UserModel userFrom, UserModel userTo) {
		// TODO Auto-generated method stub
		return feedbackDao.getListOfChatingUsingSoNumberAndFeedBackTypeForWebservice(soNumber, feedbackType, userFrom, userTo);
	}

	@Override
	public List<FeedBackCounterForHome> getfeedBackCounterForHomeByDate(List<FeedBackTypeTable> feedBackTypeTables,
			HttpServletRequest request , Date fromDate  , Date toDate){
		return feedbackDao.getfeedBackCounterForHomeByDate(feedBackTypeTables, request, fromDate, toDate);
	}

	@Override
	public 	List<Object[]> getDistincYear(HttpServletRequest request ,FeedBackTypeTable type) {
		// TODO Auto-generated method stub
		return feedbackDao.getDistincYear(request,type);
	}

	@Override
	public HashMap<String, List<Object[]>> getMonthWiseOpenIssues(String year, String feedId, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return feedbackDao.getMonthWiseOpenIssues(year, feedId, request);
	}

	@Override
	public HashMap<String, List<Object[]>> getdatewiseOpenCloseIssues(String year, String month, String feedId,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		return feedbackDao.getdatewiseOpenCloseIssues(year, month, feedId, request);
	}

	@Override
	public Operator getOperatorByUserName(String userName) {
		// TODO Auto-generated method stub
		return feedbackDao.getOperatorByUserName( userName);
	}
}
