package com.xanadutec.coreflex.userTask;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xanadutec.coreflex.company.CompanyService;
import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.UserModel;
import com.xanadutec.coreflex.model.UserPermission;

@Controller

public class UserPermissionController {

	@Autowired
	private UserService userservice;

	@Autowired
	private CompanyService companyservice;

	 @RequestMapping(value = "/userPermission", method = RequestMethod.GET)
	 public ModelAndView userPermissionForAdmin(ModelMap model,HttpServletRequest request) {
			
		 List<UserModel> userModels=userservice.getAllCoreFlexModerators();
			List<Company>companies= companyservice.getListOfCompany();
			for(Company company:companies){
				if(company.getCompanyName().equalsIgnoreCase("Coreflex")){
					companies.remove(company);
					break;
				}
			}
			System.out.println(userModels.size()+"size");
			 model.addAttribute("userModels",userModels);
			 model.addAttribute("companies",companies);
				model.addAttribute("hidemenu","hide");
			     return new ModelAndView("AssignAndViewUserPermission");
		    }
	
	@RequestMapping(value="/getUserPermission",method = RequestMethod.POST)
	public ModelAndView getDepartmeentModule(Model model,@RequestParam("userId") int userId)
	{
		
		List<Company>companies=companyservice.getListOfCompany();
		for(Company company:companies){
			if(company.getCompanyName().equalsIgnoreCase("Coreflex")){
				companies.remove(company);
				break;
			}
		}
		List<UserPermission> userPermissions=	userservice.getListOfUserPermission(userId);
		model.addAttribute("userPermissions",userPermissions);
		model.addAttribute("companies",companies);
		
		return new ModelAndView("User_Permission_Table");
	}
	
	@RequestMapping(value="/saveUserPermissiontable",method = RequestMethod.POST)
	@ResponseBody
	public String saveUserPermissiontable(Model model,HttpServletRequest request)
	{
		String userId=request.getParameter("userId");
		userservice.deleteUSerPermission(Integer.parseInt(userId));
		Enumeration<String> s=request.getParameterNames();
		while(s.hasMoreElements()){
		String name=s.nextElement();
		if(name.equalsIgnoreCase("userId")){
			
		}else if(request.getParameter(name).equalsIgnoreCase("on")){
			
			int companyid=Integer.parseInt(name);
			UserModel userModel=new UserModel();
			userModel.setUserId(Integer.parseInt(userId));
			Company company =new Company();
			company.setCompanyId(companyid);
			UserPermission userPermission =new UserPermission();
			userPermission.setCompany(company);
			userPermission.setUserModel(userModel);
			userservice.saveUserpermission(userPermission);
		}
		}
		return "success";
	}
	
	
	
	
}
