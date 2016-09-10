package com.xanadutec.coreflex.mom;

import java.awt.Dialog.ModalExclusionType;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.Format;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.avalon.framework.activity.Startable;
import org.apache.poi.hssf.model.Model;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xanadutec.coreflex.EmailPermission.EmailSendAndReceivePermissionService;
import com.xanadutec.coreflex.login.LoginService;
import com.xanadutec.coreflex.model.CompanyFeedBackTypeAssociation;
import com.xanadutec.coreflex.model.FcmNotification;
import com.xanadutec.coreflex.model.Mom;
import com.xanadutec.coreflex.model.UserModel;
import com.xanadutec.coreflex.notifiation.NotificationService;
import com.xanadutec.coreflex.userTask.UserService;

import antlr.collections.List;

@Controller
public class MomController {

	@Autowired
	private MomService momservice;
	@Autowired
	private UserService userService;
	@Autowired
	private LoginService loginService;
	@Autowired
	NotificationService notificationService;
	@Autowired
	EmailSendAndReceivePermissionService emailSendAndReceivePermissionService;

	@RequestMapping(value = "/momController", method = RequestMethod.GET)
	public ModelAndView getMomDetails(ModelMap model, HttpServletRequest request) {
		HttpSession httpSession = request.getSession();
		UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");

		if (userModelFro.getCompany().getCompanyName().equalsIgnoreCase("Coreflex")) {
			model.addAttribute("hidemenuSwitch", "switchCompany");
		}
		model.addAttribute("hidemenu", "hideAdmin");
		
		java.util.List<UserModel>userList=userService.listUser();
		model.addAttribute("userList",userList);
		return new ModelAndView("momHome");

	}
	
	@RequestMapping(value = "/getMomDateWise", method = RequestMethod.POST)
	@ResponseBody
	public String getMomDateWise(String date,String dateTo, HttpServletRequest request) {
		
		Date ActualdateFrom=null;
		Date ActualdateTo=null;
		SimpleDateFormat format= new SimpleDateFormat("MM/dd/yyyy");
		try {
			ActualdateFrom=format.parse(date);
			ActualdateTo=format.parse(dateTo);
			
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		java.util.List<Mom> moms =momservice.getListOfMomByDate(ActualdateFrom,ActualdateTo,request);
		JSONObject mainObj = new JSONObject();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = null;
		try {
			json = ow.writeValueAsString(moms);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}
	

	@RequestMapping(value = "/momview", method = RequestMethod.GET)
	public ModelAndView viewMom(ModelMap model, HttpServletRequest request, HttpServletResponse response) {

		java.util.List<Mom> list = momservice.viewMom();
		model.addAttribute("momList", list);
		
		HttpSession httpSession = request.getSession();
		UserModel userModelFrom = (UserModel) httpSession.getAttribute("UserMessage");

		UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");

		// this is for show or hide switchcompany menu
		if (userModelFro.getCompany().getCompanyName().equalsIgnoreCase("Coreflex")) {
			model.addAttribute("hidemenuSwitch", "switchCompany");
		}

		int id = userModelFrom.getUserId();
		String type = "mom";
		userService.SaveUserLastVisitedTime(id, type);
		model.addAttribute("hidemenu", "hideAdmin");
		return new ModelAndView("momview");
	}

	@RequestMapping(value = "/sendMomEmail", method = RequestMethod.POST)
	@ResponseBody
	public String sendMomEmail(@RequestParam("datepicker") String datepicker,
			@RequestParam("attendee") String attendee, @RequestParam("subject") String subject,
			@RequestParam("message") String message, @RequestParam("userId") String userId,
			final HttpServletRequest request, HttpServletResponse response) {
		
		HttpSession httpSession = request.getSession();
		UserModel userModelFrom = (UserModel) httpSession.getAttribute("UserFrom");

		long date1 = Date.parse(request.getParameter("datepicker"));

		final String attendee1 = request.getParameter("attendee");
		final String sub = request.getParameter("subject");
		final String msg = request.getParameter("message");
	
	String[] parts = userId.split(",");
	final java.util.List<String> emailList = new ArrayList<String>();
	UserModel model= null;
	for (String string : parts) {
		model=userService.getUserById(Integer.parseInt(string));
		emailList.add(model.getEmail());
	}

	Date date = new Date(date1);
	Format formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	final String dateTime = formatter.format(new Date());

	Format formatter1 = new SimpleDateFormat("yyyy-MM-dd");
	final String dateWithNoTime = formatter.format(date);
		
		final String creator=userModelFrom.getFirstName()+" "+ userModelFrom.getLastName();
		final ReplyToEmailWebMom replyToMom=new ReplyToEmailWebMom();
	
			Thread tt = new Thread(new Runnable() {
				
				public void run() {
					replyToMom.sendEmailReply(sub,emailList,attendee1, msg,dateTime,dateWithNoTime,creator,request,emailSendAndReceivePermissionService);
				}});
			tt.start();
		return "";
	
	}
	
	
	
	@RequestMapping(value = "/momhome", method = RequestMethod.POST)
	@ResponseBody
	public String getMomHomeDetails(@RequestParam("datepicker") String datepicker,
			@RequestParam("attendee") String attendee, @RequestParam("subject") String subject,
			@RequestParam("message") String message, @RequestParam("userId") String userId,
			final HttpServletRequest request, HttpServletResponse response) {

		
		new Thread() {
			public void run() {
				HttpSession httpSession = request.getSession();
				UserModel userModelFrom = (UserModel) httpSession.getAttribute("UserFrom");
				UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");
				UserModel userModelmsg = (UserModel) httpSession.getAttribute("UserMessage");

				long date1 = Date.parse(request.getParameter("datepicker"));

				 String attendee1 = request.getParameter("attendee");
				 String sub = request.getParameter("subject");
				 String msg = request.getParameter("message");

				 Mom mom = new Mom();
				Date date = new Date(date1);

				mom.setDate(date);
				mom.setDateTime(new Date());
				mom.setAttendee(attendee1); /* userModelFrom userModelFrom */
				mom.setUserModelFrom(userModelFrom);
				mom.setUserModelTo(userModelTo);
				mom.setUserfeedback(userModelmsg);
				mom.setSubject(sub);
				mom.setKeypoints(msg);
				System.out.println("mom save");
				momservice.saveMom(mom);
				 java.util.List<UserModel> userModels =loginService.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userModelFrom.getCompany());
					userModels.addAll(loginService.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userModelTo.getCompany()));
					 FcmNotification fcmNotification =notificationService.getFcmNotification();
				notificationService.sendMomFcmNotification(mom, fcmNotification,userModels,request);
			}
		}.start();
		
		return "";

	}

	@RequestMapping(value = "/getMomById", method = RequestMethod.POST)
	@ResponseBody
	public String showMom(@RequestParam("id") String id) {

		Mom mom = momservice.getMomById(Integer.parseInt(id));

		JSONObject mainObj = new JSONObject();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = null;
		try {
			json = ow.writeValueAsString(mom);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;

	}

}
