package com.xanadutec.coreflex.EmailPermission;

import java.util.Enumeration;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xanadutec.coreflex.company.CompanyService;
import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.EmailSendReceivePermission;
import com.xanadutec.coreflex.model.UserModel;
import com.xanadutec.coreflex.userTask.UserService;

@Controller
public class EmailSendAndReceivePermissionController {

	@Autowired
	CompanyService compService;
	@Autowired
	UserService userService;
	@Autowired
	EmailSendAndReceivePermissionService emailPermissionService;
	
	@RequestMapping(value="/emailPermissions",method = RequestMethod.GET)
	 public ModelAndView emailPermissions(Model uiModel,HttpServletRequest request) 
		{
			List<Company> companies =compService.getListOfCompany();
			List<UserModel>UserList=userService.listUser();
			uiModel.addAttribute("companiesList",companies);
			uiModel.addAttribute("hidemenu","hide");
			uiModel.addAttribute("UserList","UserList");
			return 	new ModelAndView("emailPermissionNew");
		
		}
	
	
	@RequestMapping(value="/getListOfUserAccordingToCompanyIdForEmailPermission",method = RequestMethod.GET)
	@ResponseBody
	 public String getListOfUserAccordingToCompanyIdForEmailPermission(@RequestParam("companyId") String companyId,Model uiModel,HttpServletRequest request) 
		{
		List<UserModel> userModels=userService.getListOfUserByCompanyId(companyId);
		
		System.out.println(companyId+"    companyId    getListOfUserAccordingToCompanyIdForEmailPermission");
		
		List<EmailSendReceivePermission >emailPermissions=emailPermissionService.getEmailPermissionUserListAccordingToCompany(companyId);
		
		System.out.println(userModels.size()+"   user Size");
		System.out.println(emailPermissions.size()+" emailPermissions sizx");
		JSONObject mainObj = new JSONObject();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = null;
		try {
			
			JSONObject js = new JSONObject();
			js.put("userList", ow.writeValueAsString(userModels));
			js.put("AssignedPermissionList", ow.writeValueAsString(emailPermissions));
			json = js.toString();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
		
		}
	
	
	@RequestMapping(value = "/saveUserListWithCompanyIdForEmailPermission", method = RequestMethod.GET)
	@ResponseBody
	public String saveUserListWithCompanyIdForEmailPermission(Model model, HttpServletRequest request) {
		String companyId = request.getParameter("companyId");
		Company company = new Company();
		company.setCompanyId(Integer.parseInt(companyId));

		userService.deleteCompanyUSerListFromEmailPermission(company);

		Enumeration<String> s = request.getParameterNames();
		while (s.hasMoreElements()) {
			String name = s.nextElement();
			if (name.equalsIgnoreCase("companyId")) {

			} else if (request.getParameter(name).equalsIgnoreCase("on")) {

				EmailSendReceivePermission emailSendReceivePermission = new EmailSendReceivePermission();
				emailSendReceivePermission.setCompany(company);

				UserModel userModel = new UserModel();
				userModel.setUserId(Integer.parseInt(name));

				emailSendReceivePermission.setUserModel(userModel);
				userService.saveEmailSendReceivePermission(emailSendReceivePermission);

			}
		}
		return "success";
	}
	
	
	@RequestMapping(value = "/saveEmailPermissionByUserId", method = RequestMethod.POST)
	@ResponseBody
	public String saveEmailPermissionByUserId(@RequestParam("userId") String userId,
			@RequestParam("companyId") String companyId,Model model, HttpServletRequest request) {
		
		
		System.out.println("Welcome in save Permission controller");
		int id=Integer.parseInt(userId);
		int comid=Integer.parseInt(companyId);
		emailPermissionService.SaveEmailPermissionByUserId(id,comid);
		return "success";
	}
		
		
	}
	
	

