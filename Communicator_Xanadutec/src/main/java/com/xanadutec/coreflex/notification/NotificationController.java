package com.xanadutec.coreflex.notification;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xanadutec.coreflex.dropBox.DropBoxService;
import com.xanadutec.coreflex.feedback.FeedbackService;
import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.CompanyFeedBackTypeAssociation;
import com.xanadutec.coreflex.model.FeedBackTypeTable;
import com.xanadutec.coreflex.model.UserLastVisitDateTime;
import com.xanadutec.coreflex.model.UserModel;
import com.xanadutec.coreflex.mom.MomService;
import com.xanadutec.coreflex.userTask.UserService;

@Controller
public class NotificationController {

	@Autowired
	UserService userService;
	@Autowired
	FeedbackService feedbackService;
	@Autowired
	DropBoxService dropboxservice;
	@Autowired
	MomService momService;
	

	@RequestMapping(value = "/getNotificationList", method = RequestMethod.POST)
	@ResponseBody
	public String getAllNotification(HttpServletRequest request) {

		UserModel UserMessage = (UserModel) request.getSession().getAttribute("UserMessage");
		UserModel UserFrom = (UserModel) request.getSession().getAttribute("UserFrom");
		UserModel UserTo = (UserModel) request.getSession().getAttribute("UserTo");

		UserLastVisitDateTime lastVisitDateTime = userService.getLastVisitedObjDetails(UserMessage.getUserId());
		Date feedcomLastVisitDate=null;
		Date querycomLastVisitDate=null;
		Date momLastVisitDate=null;
		Date documentcomLastVisitDate=null;
		Date reportLastVisitDate=null;
		try{
			 feedcomLastVisitDate = lastVisitDateTime.getFeedCom();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	
		try{
			 momLastVisitDate = lastVisitDateTime.getMom();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			documentcomLastVisitDate = lastVisitDateTime.getDocument();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		try{
			 reportLastVisitDate = lastVisitDateTime.getReport();
			
		}catch(Exception e){
			e.printStackTrace();
		}
		

		// association of company feedback type
		List<CompanyFeedBackTypeAssociation> companyFeedBackTypeAssociation = null;
		if (!UserFrom.getCompany().getCompanyName().equalsIgnoreCase("Coreflex")) {
			companyFeedBackTypeAssociation = feedbackService
					.getListOfFeedBackTypeAccoringToCompany(UserFrom.getCompany());
		} else {
			companyFeedBackTypeAssociation = feedbackService
					.getListOfFeedBackTypeAccoringToCompany(UserTo.getCompany());
		}

		List<FeedBackTypeTable> feedBackTypeTable = new ArrayList<FeedBackTypeTable>();

		// insert feedbacktype in list
		for (CompanyFeedBackTypeAssociation companyFeedBackTypeAssociation2 : companyFeedBackTypeAssociation) {
			feedBackTypeTable.add(companyFeedBackTypeAssociation2.getFeedBackType());
		}

		// get feedback counter for notification
		HashMap<FeedBackTypeTable, Long> hashMapOfFeedbackTypeAndLastVisitedDateForFeedback = feedbackService
				.getCountOfFeedbackSortingAccordingToFeedbackTypeAndLastVisitedDate(feedBackTypeTable,
						feedcomLastVisitDate, UserFrom, UserTo);

		HashMap<String, Long> feedbackResponseList = new HashMap<String, Long>();

		Set<FeedBackTypeTable> backTypeTables = hashMapOfFeedbackTypeAndLastVisitedDateForFeedback.keySet();
		for (FeedBackTypeTable feedBackTypeTable2 : backTypeTables) {
			feedbackResponseList.put(feedBackTypeTable2.getFeedbackType(),
					hashMapOfFeedbackTypeAndLastVisitedDateForFeedback.get(feedBackTypeTable2));
		}


		// get report counter for notification
		List<Date> distinctDatesOfDropBoxlist = dropboxservice.getDistinctDropBoxList();
		Map<Date, Long> hashMapofReport = dropboxservice
				.getCountOfDistinctDropBoxListByDistinctDatesOfDropBoxlistAndUserLastVisitTime(
						distinctDatesOfDropBoxlist, reportLastVisitDate, UserFrom, UserTo);

		// get document counter for notification
		List<Date> distinctDatesOfDocumentlist = dropboxservice.getDistinctDocumentList();
		Map<Date, Long> hashMapofDocument = dropboxservice
				.getCountOfDistinctDocumentListByDistinctDatesOfDocuementAndUserLastVisitTime(
						distinctDatesOfDocumentlist, documentcomLastVisitDate, UserFrom, UserTo);

		// get mom counter for notification
		List<Date> distinctDatesOfMom = momService.getDistinctMomDates();
		Map<Date, Long> hashMapofMom = momService
				.getCountOfMomListByDistinctDatesOfMomAndUserLastVisitTime(
						distinctDatesOfMom, momLastVisitDate, UserFrom, UserTo, request);

		JSONObject jsonObject = new JSONObject();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = null;
		try {
			String feedcomList = ow.writeValueAsString(feedbackResponseList);
			String reportList=	ow.writeValueAsString(hashMapofReport);
			String documentList=	ow.writeValueAsString(hashMapofDocument);
			String momList=	ow.writeValueAsString(hashMapofMom);		
			
			jsonObject.put("FeedcomList", feedcomList);
			jsonObject.put("reportList", reportList);
			jsonObject.put("documentList", documentList);
			jsonObject.put("momList", momList);
			json = jsonObject.toString();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
}
