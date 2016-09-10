package com.xanadutec.coreflex.registration;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.xanadutec.coreflex.company.CompanyService;
import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.RoleModel;
import com.xanadutec.coreflex.model.UserModel;
import com.xanadutec.coreflex.userTask.UserService;

@Controller
public class UserCompanyRegistration {
	@Autowired
	CompanyService companyservice;
	@Autowired
	UserService userservice;

	@RequestMapping(value = "/companyRegistration", method = RequestMethod.GET)
	public ModelAndView companyRegistration(ModelMap model, HttpServletRequest request) {

		List<Company> companies = companyservice.getListOfCompany();
		List<UserModel>adminList=userservice.getAllAdminUser();
		
		model.addAttribute("companies", companies);
		model.addAttribute("adminList",adminList);
		
		System.out.println("list of addmion");
		for (UserModel userModel : adminList) {
			System.out.println(userModel.getFirstName());
		}
		model.addAttribute("hidemenu","hide");
		return new ModelAndView("CompanyRegistration");
	}

	@RequestMapping(value = "/userRegistration", method = RequestMethod.GET)
	public String userRegistration(ModelMap model, HttpServletRequest request) {
		List<Company> companies = companyservice.getListOfCompany();
		model.addAttribute("companies", companies);
		List<UserModel> userModels = userservice.listUser();
		model.addAttribute("userlist", userModels);
		model.addAttribute("hidemenu","hide");
		return "userRegistration";
	}

	@RequestMapping(value = "/saveCompany", method = RequestMethod.POST)
	@ResponseBody
	public String saveCompany(Model uiModel, HttpServletRequest request) {
		String name = request.getParameter("companyname");
		String contact = request.getParameter("contact");
		String email = request.getParameter("email");
		String address = request.getParameter("address");

		String fname = request.getParameter("fname");
		String lname = request.getParameter("lname");
		String uname = request.getParameter("uname");
		String password = request.getParameter("password");
		String Email = request.getParameter("Email");
		String mno = request.getParameter("mno");

		Company company = new Company();
		company.setCompanyName(name);
		company.setCompanyContact(contact);
		company.setCompanyEmail(email);
		company.setCompanyAddress(address);
		   companyservice.saveCompany(company);
		RoleModel rmodel = new RoleModel();
		rmodel.setId(1);

		UserModel user = new UserModel();
		user.setFirstName(fname);
		user.setLastName(lname);
		user.setUserName(uname);
		user.setPassword(password);
		user.setEmail(Email);
		user.setMobileNo(mno);
		user.setUserRoll(rmodel);
		user.setCompany(company);
		userservice.addUser(user);
	
		

		return "success";
	}

	@RequestMapping(value = "/updateCompany", method = RequestMethod.POST)
	@ResponseBody
	public String updateCompany(@RequestParam("id") String id, @RequestParam("name") String name,
			@RequestParam("contact") String contact, @RequestParam("email") String email,
			@RequestParam("address") String address, @RequestParam("fname") String fname,
			@RequestParam("lname") String lname, @RequestParam("uname") String uname,
			@RequestParam("Email") String Email, @RequestParam("mno") String mno,
			@RequestParam("password") String password, @RequestParam("userid") String userid, Model uiModel,
			HttpServletRequest request) {

		RoleModel rmodel = new RoleModel();
		rmodel.setId(1);

		Company company = new Company();
		company.setCompanyId(Integer.parseInt(id));
		company.setCompanyAddress(address);
		company.setCompanyEmail(email);
		company.setCompanyContact(contact);
		company.setCompanyName(name);
		companyservice.updateCompany(company);
		
		UserModel user = new UserModel();
		user.setUserId(Integer.parseInt(userid));
		user.setCompany(company);
		user.setFirstName(fname);
		user.setLastName(lname);
		user.setUserName(uname);
		user.setEmail(Email);
		user.setPassword(password);
		user.setMobileNo(mno);
		user.setMobileNo(mno);
		user.setUserRoll(rmodel);

		userservice.updateUser(user);

		

		return "success";

	}

}
