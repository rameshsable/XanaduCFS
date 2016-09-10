package com.xanadutec.coreflex.setup;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.xanadutec.coreflex.feedback.FeedbackService;
import com.xanadutec.coreflex.model.FeedBackTypeTable;
import com.xanadutec.coreflex.model.RoleModel;
import com.xanadutec.coreflex.model.Status;
import com.xanadutec.coreflex.model.UserModel;
import com.xanadutec.coreflex.userTask.UserService;


@Controller
public class CreateSetup 
{

	@Autowired
	SetupService setupService;
	@Autowired
	private SessionFactory sessionFactory;
	@Autowired
	private UserService userService;
	@Autowired
	private FeedbackService feedbackService; 
	public CreateSetup()
	{
		
	}
	public CreateSetup(HttpServletRequest request)
	{
		makeSetup(request);
	}
	public void makeSetup(HttpServletRequest request)
	{
		System.out.println("HHHHHHHHHHHHHHHHHHHHHHH");
		
		/*File tmpFile = new File(request.getServletContext().getRealPath("resources/profileImages"));

		if (!tmpFile.exists()) {
			try 
			{
				tmpFile.mkdirs();			
			} 
			catch (Exception e) {
				e.printStackTrace();
				
			}
		} */
//		setupService.addCityState();
//		setupService.addAllUnit();
		String pathact=request.getServletContext().getRealPath("/resources/CfsFiles");
		try{
		//	uploadDownloadService.saveUrlForFileUploadAndDownload(pathact);
		}catch(Exception e){
			e.printStackTrace();
		}
		
		if(feedbackService.getfeedBackCount()==0){
			
			FeedBackTypeTable feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(1);
			feedBackTypeTable.setFeedbackType("PO");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(2);
			feedBackTypeTable.setFeedbackType("Bill to Address");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(3);
			feedBackTypeTable.setFeedbackType("Ship to address");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(4);
			feedBackTypeTable.setFeedbackType("Price List");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(5);
			feedBackTypeTable.setFeedbackType("Delivery / Future ship dates");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(6);
			feedBackTypeTable.setFeedbackType("DFF Totals");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(7);
			feedBackTypeTable.setFeedbackType("Notes");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(8);
			feedBackTypeTable.setFeedbackType("Price Discrepancy");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(9);
			feedBackTypeTable.setFeedbackType("Bid Issues");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(10);
			feedBackTypeTable.setFeedbackType("Credit Card Information");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(11);
			feedBackTypeTable.setFeedbackType("Item Issues");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(12);
			feedBackTypeTable.setFeedbackType("Non Complete Report");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(13);
			feedBackTypeTable.setFeedbackType("Credit Reports");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(14);
			feedBackTypeTable.setFeedbackType("Invalid BB");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(15);
			feedBackTypeTable.setFeedbackType("Quantity Issues");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(16);
			feedBackTypeTable.setFeedbackType("Item Configuration");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(17);
			feedBackTypeTable.setFeedbackType("Ship to contact");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(18);
			feedBackTypeTable.setFeedbackType("Shipping Charges issues");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(19);
			feedBackTypeTable.setFeedbackType("Tax issue");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(20);
			feedBackTypeTable.setFeedbackType("Proposal");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(21);
			feedBackTypeTable.setFeedbackType("Discount Issues");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(22);
			feedBackTypeTable.setFeedbackType("Report");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
			
			feedBackTypeTable=new FeedBackTypeTable();
			feedBackTypeTable.setId(23);
			feedBackTypeTable.setFeedbackType("Other");
			feedbackService.addFeedbackTypes(feedBackTypeTable);
				
			}
		
		Session session=sessionFactory.openSession();
		RoleModel roleModel1=userService.userRoll(1);
		if(roleModel1==null)
		{
			RoleModel roleModel=new RoleModel();
			roleModel.setRole("admin");
			session.save(roleModel);
			session.flush();
			
			roleModel=new RoleModel();
			roleModel.setRole("moderator");
			session.save(roleModel);
			
			roleModel=userService.userRoll(1);
			UserModel userModel=new UserModel();
			userModel.setUserName("admin");
			userModel.setPassword("admin");
			userModel.setUserRoll(roleModel);
			session.save(userModel);
			
			roleModel=userService.userRoll(2);
			userModel=new UserModel();
			userModel.setUserName("coreflex");
			userModel.setPassword("coreflex");
			userModel.setUserRoll(roleModel);
			session.save(userModel);
			
			roleModel=userService.userRoll(2);
			userModel=new UserModel();
			userModel.setUserName("ssi");
			userModel.setPassword("ssi");
			userModel.setUserRoll(roleModel);
			session.save(userModel);
			
			Status status =new Status();
			status.setStatus("Open");
			session.save(status);
			
			status =new Status();
			status.setStatus("Close");
			session.save(status);
			
			
			
		}
	}
	
	
}
