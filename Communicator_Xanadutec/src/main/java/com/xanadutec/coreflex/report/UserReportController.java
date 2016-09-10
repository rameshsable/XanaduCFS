package com.xanadutec.coreflex.report;


import java.util.Collections;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.xanadutec.coreflex.feedback.FeedBackCountComparator;
import com.xanadutec.coreflex.feedback.FeedbackService;
import com.xanadutec.coreflex.model.FeedBackCounterForHome;
import com.xanadutec.coreflex.model.FeedBackTypeTable;
import com.xanadutec.coreflex.model.Feedback;
import com.xanadutec.coreflex.model.Operator;
import com.xanadutec.coreflex.model.RedFlag;
import com.xanadutec.coreflex.model.UserModel;
import com.xanadutec.coreflex.operators.OperatorService;
@Controller
public class UserReportController {
	@Autowired
	FeedbackService feedbackservice; 
	@Autowired
	OperatorService operatorservice;
	
	@RequestMapping(value= "/feedcomReport",method=RequestMethod.GET)
	public ModelAndView feedcomreport(ModelMap uiModel,HttpServletRequest request)
	{
		 HttpSession httpSession=request.getSession();
		 UserModel userModelFrom= (UserModel) httpSession.getAttribute("UserFrom");
		 UserModel userModelTo= (UserModel) httpSession.getAttribute("UserTo");
	//	uiModel.addAttribute("forUser","");
		 
		 	List<FeedBackTypeTable> feedbackType=  	feedbackservice.getFeedbackList();
	    	List<FeedBackCounterForHome> feedBackCounterForHomes= feedbackservice.getfeedBackCounterForHome(feedbackType ,request);
	    	
	    	uiModel.addAttribute("FeedBackCount", feedBackCounterForHomes);
	    	String moderator=	(String) httpSession.getAttribute("moderator");
	    try{
	    if(moderator.equals(null) ||moderator.equals("")){
	    	
	    }else{
	    	uiModel.addAttribute("moderator", "moderator");
	    }
	    }catch(Exception e){
	    	
	    }
		
		return new ModelAndView("feedcomReport");
			
	}
	
	@RequestMapping(value= "/feedcomOpenCriteria",method=RequestMethod.GET)
	public ModelAndView feedcomOpenCriteria(@RequestParam("feedId") String feedId,ModelMap uiModel,HttpServletRequest request)
	{
		System.out.println(feedId);
		FeedBackTypeTable typeTable=new FeedBackTypeTable();
		 typeTable.setId(Integer.parseInt(feedId));
		 List<Feedback>feedbacks= feedbackservice.getfeedBackListForFeedBackId(typeTable ,request);
		uiModel.addAttribute("feedId",feedId);
		uiModel.addAttribute("FeedBackList",feedbacks);
		uiModel.addAttribute("feedBackTypeId",feedId);
	String feedBackName =	feedbackservice.getFeedBackNameUsingFeedBackId(Integer.parseInt(feedId));
		String mod=(String) request.getSession().getAttribute("moderator");
		uiModel.addAttribute("feedBackName", feedBackName);
		uiModel.addAttribute("moderator",mod);
		System.out.println("mod =  "+mod);
		 return new ModelAndView("feedcomOpenCriteriaFirstView");
		
		
	}
	
	
	 @RequestMapping(value = "/feedcomOpenCriteriaChat", method = RequestMethod.GET)
		public ModelAndView feedcomChat(@RequestParam("soNumber") String soNumber,@RequestParam("feedbackType") String feedbackType,Model uiModel,HttpServletRequest request)
	 {
		
		 System.out.println(feedbackType +" feed type "+soNumber +"soNumber");
	List<Feedback>feedbacks=feedbackservice.getListOfChatingUsingSoNumberAndFeedBackType(soNumber,feedbackType,request);
	Collections.sort(feedbacks,new FeedBackCountComparator());
	uiModel.addAttribute("feedbacks",feedbacks);
	UserModel userModel =(UserModel) request.getSession().getAttribute("UserFrom");
	uiModel.addAttribute("userFrom",userModel.getUserId());
	UserModel userModelmsg =(UserModel)request.getSession().getAttribute("UserMessage");
	uiModel.addAttribute("userfeedbackchat",userModelmsg.getUserName());
	if(feedbacks.size()>0){
		uiModel.addAttribute("FeedId",feedbacks.get(0).getFeedBackType().getId());
		uiModel.addAttribute("FeedbackType",feedbacks.get(0).getFeedBackType().getFeedbackType());
		uiModel.addAttribute("soNumber",feedbacks.get(0).getSoNumber());
		uiModel.addAttribute("status",feedbacks.get(0).getStatus().getStatusId());
		uiModel.addAttribute("operator",feedbacks.get(0).getOperator().getUserName());
		
		
	}
	return new ModelAndView("FeedComOpenCriteriaReport");
	 }
	
	
	@RequestMapping(value= "/feedcomCloseCriteria",method=RequestMethod.GET)
	public ModelAndView feedcomCloseCriteria(@RequestParam("feedId") String feedId,ModelMap uiModel,HttpServletRequest request)
	{
		FeedBackTypeTable typeTable=new FeedBackTypeTable();
		 typeTable.setId(Integer.parseInt(feedId));
	List<Feedback>feedbacks= feedbackservice.getClosedfeedBackListForFeedBackId(typeTable ,request);
		uiModel.addAttribute("feedId",feedId);
		uiModel.addAttribute("FeedBackList",feedbacks);
		uiModel.addAttribute("feedBackTypeId",feedId);
	String feedBackName =	feedbackservice.getFeedBackNameUsingFeedBackId(Integer.parseInt(feedId));
		String mod=(String) request.getSession().getAttribute("moderator");
		uiModel.addAttribute("feedBackName", feedBackName);
		uiModel.addAttribute("moderator",mod);
		System.out.println("mod =  "+mod);
		 return new ModelAndView("feedcomCloseCriteriaFirstView");
		
		
	}
	
	@RequestMapping(value= "/feedcomCloseCriteriachat",method=RequestMethod.GET)
	public ModelAndView feedcomCloseCriteriachat(@RequestParam("soNumber") String soNumber,@RequestParam("feedbackType") String feedbackType,Model uiModel,HttpServletRequest request)
	 {
		
		 System.out.println(feedbackType +" feed type "+soNumber +"soNumber");
		 									
	List<Feedback>feedbacks=feedbackservice.getClosedFeedComListOfChatingUsingSoNumberAndFeedBackType(soNumber,feedbackType,request);
	Collections.sort(feedbacks,new FeedBackCountComparator());
	uiModel.addAttribute("feedbacks",feedbacks);
	UserModel userModel =(UserModel) request.getSession().getAttribute("UserFrom");
	uiModel.addAttribute("userFrom",userModel.getUserId());
	UserModel userModelmsg =(UserModel)request.getSession().getAttribute("UserMessage");
	uiModel.addAttribute("userfeedbackchat",userModelmsg.getUserName());
	if(feedbacks.size()>0){
		uiModel.addAttribute("FeedId",feedbacks.get(0).getFeedBackType().getId());
		uiModel.addAttribute("FeedbackType",feedbacks.get(0).getFeedBackType().getFeedbackType());
		uiModel.addAttribute("soNumber",feedbacks.get(0).getSoNumber());
		uiModel.addAttribute("status",feedbacks.get(0).getStatus().getStatusId());
		uiModel.addAttribute("operator",feedbacks.get(0).getOperator().getUserName());	
		}
		return new ModelAndView("FeedComCloseCriteriaReport");
	 }
	
	
	@RequestMapping(value= "/feedcomOperatorCriteria",method=RequestMethod.POST)
	public ModelAndView feedcomOperatorCriteria(ModelMap model,HttpServletRequest request)
	{
		
		
		return new ModelAndView("feedcomReport");
		
	}
	@RequestMapping(value= "/feedcomsOpenShowDateWiseReport",method=RequestMethod.POST)
	public ModelAndView feedcomsOpenShowDateWiseReport(@RequestParam("date1") String date1,@RequestParam("date2") String date2,@RequestParam("feedbackType") String feedbackType,Model uiModel,HttpServletRequest request)
	 {
		
		FeedBackTypeTable typeTable =new FeedBackTypeTable();
		typeTable.setId(Integer.parseInt(feedbackType));
		uiModel.addAttribute("feedId",feedbackType);
		List<Feedback>  feedbacks=	feedbackservice.getfeedBackOpenIssueDateCriteriaReport(typeTable, request, date1, date2);
		System.out.println("List size "+feedbacks.size());
		uiModel.addAttribute("FeedBackList",feedbacks);
		return new ModelAndView("openIssueReportDate");
	 }
	
	@RequestMapping(value= "/feedcomscloseShowDateWiseReport",method=RequestMethod.POST)
	public ModelAndView feedcomscloseShowDateWiseReport(@RequestParam("date1") String date1,@RequestParam("date2") String date2,@RequestParam("feedbackType") String feedbackType,Model uiModel,HttpServletRequest request)
	 {
		
		FeedBackTypeTable typeTable=new FeedBackTypeTable();
		 typeTable.setId(Integer.parseInt(feedbackType));
	List<Feedback>feedbacks= feedbackservice.getfeedBackCloseIssueDateCriteriaReport(typeTable, request, date1, date2);
		uiModel.addAttribute("feedId",feedbackType);
		
		System.out.println("List size "+feedbacks.size());
		uiModel.addAttribute("FeedBackList",feedbacks);
		return new ModelAndView("closeIssueReportDate");
	 }
	
	
	
	
}
