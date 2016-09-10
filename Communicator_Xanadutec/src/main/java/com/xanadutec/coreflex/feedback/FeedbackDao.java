package com.xanadutec.coreflex.feedback;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.CompanyFeedBackTypeAssociation;
import com.xanadutec.coreflex.model.FeedBackCounterForHome;
import com.xanadutec.coreflex.model.FeedBackSonumberSortingByDate;
import com.xanadutec.coreflex.model.FeedBackTypeTable;
import com.xanadutec.coreflex.model.Feedback;
import com.xanadutec.coreflex.model.Operator;
import com.xanadutec.coreflex.model.RedFlag;
import com.xanadutec.coreflex.model.UserModel;

public interface FeedbackDao {
	public String addFeedbackTypes(FeedBackTypeTable feedBackTypeTable);

	public int getfeedBackCount();

	public List<FeedBackTypeTable> getFeedbackList();

	public List<FeedBackCounterForHome> getfeedBackCounterForHome(List<FeedBackTypeTable> feedBackTypeTables,
			HttpServletRequest request);

	public List<Feedback> getfeedBackListForFeedBackId(FeedBackTypeTable feedBackTypeTables,
			HttpServletRequest request);

	public List<Feedback> getListOfChatingUsingSoNumberAndFeedBackType(String soNumber, String feedbackType,
			HttpServletRequest request);

	public String saveChattingForFeedCom(String soNumber, String status, String subject, String Attachment,
			String chatBox, String feedbackType, String operator);

	public String getMaxOfFeedBackChattingCounter(String soNumber, String feedBacktype);

	public List<Feedback> getLiveChatUserTo(String soNumber, String feedbackType, String userToCounter,
			HttpServletRequest request);

	public List<Feedback> getLiveChatUserFrom(String soNumber, String feedbackType, String userFromCounter,
			HttpServletRequest request);

	public Feedback saveNewFeedComChat(String soNumber, String feebackType, String Chating, String operator,
			String attachment, String subject, HttpServletRequest request);

	public String validateNewFeedComChating(String soNumber, String feebackType, HttpServletRequest request);

	public void saveRedFlag(String soNumber, String feebackType, HttpServletRequest request);

	public List<RedFlag> getListOfRedFlag(String feebackType, HttpServletRequest request);

	public String validateFlag(String soNumber, String feebackType, HttpServletRequest request);

	public int getfeedBackTypeIdByFeedBackName(String feedbackName);

	public void deleteFlag(String soNumber, String feebackType, HttpServletRequest request);

	public String getFeedBackNameUsingFeedBackId(int id);

	public List<Feedback> getClosedfeedBackListForFeedBackId(FeedBackTypeTable feedBackTypeTables,
			HttpServletRequest request);

	public List<Feedback> getClosedFeedComListOfChatingUsingSoNumberAndFeedBackType(String soNumber,
			String feedbackType, HttpServletRequest request);

	public List<Feedback> getfeedBackOpenIssueDateCriteriaReport(FeedBackTypeTable feedBackTypeTables,
			HttpServletRequest request, String date1, String date2);

	List<Feedback> getfeedBackCloseIssueDateCriteriaReport(FeedBackTypeTable feedBackTypeTables,
			HttpServletRequest request, String date1, String date2);

	public List<String> getListOfUserByCompanyId(Company company);

	public List<Feedback> getListOfFeedBack(UserModel userFrom, UserModel userTo);

	List<Feedback> getListOfQueryForCoreflex();

	public List<FeedBackSonumberSortingByDate> getSortedFeedBackListAccordingtToFeedbackId(FeedBackTypeTable typeTable,
			HttpServletRequest request);

	public List<CompanyFeedBackTypeAssociation> getListOfFeedBackTypeAssociationWithCompanyAccordingToCompanyId(
			String companyId);
	
	public void deleteCompanyFeedBackTypeAssociationByCompany(Company company);

	public void saveCompanyFeedBackTypeAssociation(CompanyFeedBackTypeAssociation companyFeedBackTypeAssociation);
	
	public List<CompanyFeedBackTypeAssociation> getListOfFeedBackTypeAccoringToCompany(Company company) ;
	
	public List<CompanyFeedBackTypeAssociation> getListOfAllCompanyFeedBackTypeAssociation();
	
	public HashMap<FeedBackTypeTable, Long> getCountOfFeedbackSortingAccordingToFeedbackTypeAndLastVisitedDate(
			List<FeedBackTypeTable> feedBackTypeTables, Date lastVisitedDate, UserModel userFrom, UserModel userTo);

	public String validateNewFeedComChatingForWebService(String soNumber, String feebackType, UserModel userFrom, UserModel userTo);
	
	public String saveNewFeedComChatForWebService(String soNumber, String feebackType, String Chating, String operator,
			String attachment, String subject, UserModel userFrom, UserModel userTo,UserModel userFeedback);

	public String saveChattingFeedComForWebService(String soNumber, String status, String subject, String Attachment,
			String chatBox, String feedbackType, String oper, UserModel userFrom, UserModel userTo,
			UserModel userFeedback);

	public List<Feedback> getListOfChatingUsingSoNumberAndFeedBackTypeForWebservice(String soNumber, String feedbackType,
			UserModel userFrom, UserModel userTo);
	
	public List<FeedBackCounterForHome> getfeedBackCounterForHomeByDate(List<FeedBackTypeTable> feedBackTypeTables,
			HttpServletRequest request , Date fromDate  , Date toDate) ;
	

	List<Object[]> getDistincYear(HttpServletRequest request, FeedBackTypeTable type);
	
	public HashMap<String, List<Object[]>> getMonthWiseOpenIssues(String year,String feedId,HttpServletRequest request);
	
	
	public HashMap<String, List<Object[]>> getdatewiseOpenCloseIssues(String year,String month,String feedId,HttpServletRequest request);
	
	public Operator getOperatorByUserName(String userName);
	
	
	
	
	

}
