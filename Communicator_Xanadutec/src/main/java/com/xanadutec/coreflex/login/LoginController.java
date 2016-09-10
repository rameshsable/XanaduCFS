package com.xanadutec.coreflex.login;



import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.poi.hpsf.extractor.HPSFPropertiesExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.xanadutec.coreflex.feedback.FeedbackService;
import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.FeedBackCounterForHome;
import com.xanadutec.coreflex.model.FeedBackTypeTable;
import com.xanadutec.coreflex.model.UserModel;
import com.xanadutec.coreflex.model.UserPermission;
import com.xanadutec.coreflex.setup.CreateSetup;
import com.xanadutec.coreflex.userTask.UserService;


// ramesh

@Controller
public class LoginController {

	@Autowired
	CreateSetup createSetup;
	@Autowired
	FeedbackService feedbackservice; 
	@Autowired
	private LoginService loginService;
	@Autowired
	private UserService userService;
	
	 private Logger logger = Logger.getLogger(LoginController.class);
	
	@RequestMapping(value = "/", method = RequestMethod.GET)
	public ModelAndView login(HttpServletRequest request) {
	//	createSetup.makeSetup(request);
	
		return new ModelAndView("Login");
		
	}
	
	  @RequestMapping(value = "/login", method = RequestMethod.GET)
	    public String login(ModelMap model,HttpServletRequest request) {
		//  createSetup.makeSetup(request);
		  //HttpSession session = request.getSession(true);
		  //session.setAttribute("userId", );
		  
		     return "Login";
	    }
	 
	    @RequestMapping(value = "/accessdenied", method = RequestMethod.GET)
	    public String loginerror(ModelMap model) {
	        model.addAttribute("error", "true");
	        model.addAttribute("errorMsg", "Unauthorized Access !! Please Enter Correct Username Password");
	        
	        return "Login";
	    }
	 
	    @RequestMapping(value = "/logout", method = RequestMethod.GET)
	    public String logout(ModelMap model) {
	    	return "logout";
	    }
	    @RequestMapping(value = "/home", method = RequestMethod.GET)
	  //  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
		public ModelAndView loginSuccess(ModelMap model,HttpServletRequest request) {
	    	
	    	
	    	UserDetails user = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        String name = user.getUsername(); //get logged in username
	        UserModel userModel= loginService.getUserByUname(name);
	        HttpSession httpSession=request.getSession();
	        
	        httpSession.setAttribute("ActUserName", userModel.getFirstName()+" "+userModel.getLastName());
	        httpSession.setAttribute("ActpassName", userModel.getPassword());
	        
	        if(userModel.getUserRoll().getRole().equalsIgnoreCase("Admin")&&userModel.getCompany().getCompanyName().equalsIgnoreCase("Coreflex")){
	        	httpSession.setAttribute("UserFrom",userModel);
	        	httpSession.setAttribute("UserMessage",userModel);
	        	List<Company> modelsn= userService.getAllModeratorClients();
	        	httpSession.setAttribute("admin","admin");
	        	model.addAttribute("models",modelsn);
	        	model.addAttribute("hidemenu","hide");
	        	model.addAttribute("hidemenuReport","hide");
	        	
	        	logger.info("Home Page Login by " +userModel.getFirstName() +" - "+userModel.getLastName());
	        //    logger.error("This is an error log entry   3");
	            
	        	return new ModelAndView("HomeForAdmin");	
	        }
	        else if(userModel.getUserRoll().getRole().equalsIgnoreCase("moderator")&&userModel.getCompany().getCompanyName().equalsIgnoreCase("Coreflex")){
	        	
	        	UserModel userModelForModerator= loginService.getCoreflexAdmin();
	        	httpSession.setAttribute("UserFrom",userModelForModerator);
	        	httpSession.setAttribute("UserMessage",userModel);
	        	httpSession.setAttribute("admin","admin");
	        	List<Company> modelsn= userService.getAllModeratorClients();
	        	httpSession.setAttribute("admin","admin");
	        	List<Company> companies= new ArrayList<Company>();
	        	List<UserPermission> userPermissions=	userService.getListOfUserPermission(userModel.getUserId());
	        	for(UserPermission permission:userPermissions){
	        		for(Company modelsnn :modelsn){
	        			if(permission.getCompany().getCompanyId()==modelsnn.getCompanyId()){
	        				companies.add(modelsnn);
		        		}	
	        		}
	        		
	        		UserModel userModel2 =loginService.getclientCompanyAdminById(Integer.parseInt(2+""));
	       		 
	       		 request.getSession().setAttribute("CompanyId",2);
	       		 httpSession=request.getSession();
	       		 httpSession.setAttribute("UserTo",userModel2);
	       		//uiModel.addAttribute("FeedBackList",feedbacks);
	       	//	 return new ModelAndView("redirect:/configureHome");
	       	//	return new ModelAndView("HomeForAdmin");	
	        	}
	        	UserModel userModel2 =loginService.getclientCompanyAdminById(Integer.parseInt(2+""));
	       		 
	       		 request.getSession().setAttribute("CompanyId",2);
	       		 httpSession=request.getSession();
	       		 httpSession.setAttribute("UserTo",userModel2);
	       		//uiModel.addAttribute("FeedBackList",feedbacks);
	       		
	      	
	        	model.addAttribute("models",companies);
	    		model.addAttribute("userPermissions",userPermissions);
	    		model.addAttribute("hidemenuModerator","hide");
	    
	            
	    		// return new ModelAndView("redirect:/configureHome");
	        	return new ModelAndView("HomeForAdmin");
	        	
	        }
	        else{
	        	 System.out.println("else");
	        	 
	        	 Company company= userModel.getCompany();
	        	 
	      UserModel usermodel2=	 loginService.getclientCompanyAdminById(company.getCompanyId());
	        	httpSession.setAttribute("UserFrom",usermodel2);
	        	UserModel userModel2 = loginService.getCoreflexAdmin();
	        	httpSession.setAttribute("UserTo",userModel2);
	        	httpSession.setAttribute("UserMessage",userModel);
	        	httpSession.setAttribute("moderator","Moderator");
	        	
	        	httpSession.setAttribute("CompanyId",usermodel2.getCompany().getCompanyId());
	        	
	        	
	        	return new ModelAndView("redirect:/configureHome");
	        	
	        }
	    }
	    @RequestMapping(value = "/homeurl", method = RequestMethod.GET)
		  //  @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_MODERATOR')")
			public ModelAndView homeurl(ModelMap model,HttpServletRequest request) {
	    	
	    	
	    	UserDetails user = (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	        String name = user.getUsername(); //get logged in username
	        System.out.println(user);
	        UserModel userModel= loginService.getUserByUname(name);
	        System.out.println(userModel.getUserRoll().getRole());
	        HttpSession httpSession=request.getSession();
	        
//	        httpSession.setAttribute("ActUserName", name);
	        httpSession.setAttribute("ActUserName", userModel.getFirstName()+" "+userModel.getLastName());        
	        if(userModel.getUserRoll().getRole().equalsIgnoreCase("Admin")&&userModel.getCompany().getCompanyName().equalsIgnoreCase("Coreflex")){
	        	httpSession.setAttribute("UserFrom",userModel);
	        	httpSession.setAttribute("UserMessage",userModel);
	        	List<Company> modelsn= userService.getAllModeratorClients();
	        	httpSession.setAttribute("admin","admin");
	        	model.addAttribute("models",modelsn);
	        	model.addAttribute("hidemenu","hide");
	        	model.addAttribute("hidemenuReport","hide");
	        	
	        	
	            
	        	return new ModelAndView("HomeForAdmin");	
	        }
	        else if(userModel.getUserRoll().getRole().equalsIgnoreCase("moderator")&&userModel.getCompany().getCompanyName().equalsIgnoreCase("Coreflex")){
	        	
	        	UserModel userModelForModerator= loginService.getCoreflexAdmin();
	        	httpSession.setAttribute("UserFrom",userModelForModerator);
	        	httpSession.setAttribute("UserMessage",userModel);
	        	httpSession.setAttribute("admin","admin");
	        	List<Company> modelsn= userService.getAllModeratorClients();
	        	httpSession.setAttribute("admin","admin");
	        	List<Company> companies= new ArrayList<Company>();
	        	List<UserPermission> userPermissions=	userService.getListOfUserPermission(userModel.getUserId());
	        	for(UserPermission permission:userPermissions){
	        		for(Company modelsnn :modelsn){
	        			if(permission.getCompany().getCompanyId()==modelsnn.getCompanyId()){
	        				companies.add(modelsnn);
		        		}	
	        		}
	        		
	        		UserModel userModel2 =loginService.getclientCompanyAdminById(Integer.parseInt(2+""));
	       		 
	       		 request.getSession().setAttribute("CompanyId",2);
	       		 httpSession=request.getSession();
	       		 httpSession.setAttribute("UserTo",userModel2);
	       		model.addAttribute("hidemenuModerator","hide");
	       		//uiModel.addAttribute("FeedBackList",feedbacks);
	       	//	 return new ModelAndView("redirect:/configureHome");
	        		
	        	}
	        	UserModel userModel2 =loginService.getclientCompanyAdminById(Integer.parseInt(2+""));
	       		 
	       		 request.getSession().setAttribute("CompanyId",2);
	       		 httpSession=request.getSession();
	       		 httpSession.setAttribute("UserTo",userModel2);
	       		//uiModel.addAttribute("FeedBackList",feedbacks);
	       		
	       	
	        	model.addAttribute("models",companies);
	    		model.addAttribute("userPermissions",userPermissions);
	    		model.addAttribute("hidemenuReport","hide");
	    	//	 return new ModelAndView("redirect:/configureHome");
	        	return new ModelAndView("HomeForAdmin");
	        	
	        }
	        else{
	        	 System.out.println("else");
	        	 
	        	 Company company= userModel.getCompany();
	        	 
	      UserModel usermodel2=	 loginService.getclientCompanyAdminById(company.getCompanyId());
	        	httpSession.setAttribute("UserFrom",usermodel2);
	        	UserModel userModel2 = loginService.getCoreflexAdmin();
	        	httpSession.setAttribute("UserTo",userModel2);
	        	httpSession.setAttribute("UserMessage",userModel);
	        	httpSession.setAttribute("moderator","Moderator");
	        	
	        	httpSession.setAttribute("CompanyId",usermodel2.getCompany().getCompanyId());
	        	
	        	model.addAttribute("hidemenu","hideAdmin");
	        	return new ModelAndView("redirect:/configureHome");
	        	
	        }
	    }
	    
}
