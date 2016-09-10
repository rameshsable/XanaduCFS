package com.xanadutec.coreflex.feedback;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.TreeSet;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import com.xanadutec.coreflex.EmailPermission.EmailSendAndReceivePermissionService;
import com.xanadutec.coreflex.FileUploadDownload.FileUploadService;
import com.xanadutec.coreflex.Timerclass.ReplyToEmailFeedCom;
import com.xanadutec.coreflex.login.LoginService;
import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.CompanyFeedBackTypeAssociation;
import com.xanadutec.coreflex.model.EmailSendReceivePermission;
import com.xanadutec.coreflex.model.FcmNotification;
import com.xanadutec.coreflex.model.FeedBackCounterForHome;
import com.xanadutec.coreflex.model.FeedBackSonumberSortingByDate;
import com.xanadutec.coreflex.model.FeedBackTypeTable;
import com.xanadutec.coreflex.model.Feedback;
import com.xanadutec.coreflex.model.FtpInfo;
import com.xanadutec.coreflex.model.Operator;
import com.xanadutec.coreflex.model.RedFlag;
import com.xanadutec.coreflex.model.Report;
import com.xanadutec.coreflex.model.UserModel;
import com.xanadutec.coreflex.notifiation.NotificationService;
import com.xanadutec.coreflex.operators.OperatorService;
import com.xanadutec.coreflex.query.EmailUtil;
import com.xanadutec.coreflex.userTask.UserService;

@Controller
public class FeedbackController {

	@Autowired
	FeedbackService feedbackservice;
	@Autowired
	OperatorService operatorservice;
	@Autowired
	private LoginService loginService;
	@Autowired
	private UserService userService;
	@Autowired
	private NotificationService notificationService;
	@Autowired
	EmailSendAndReceivePermissionService emailSendAndReceivePermissionService;
	@Autowired
	FileUploadService fileUploadService;

	@RequestMapping(value = "/feedbackcom", method = RequestMethod.GET)
	public String login(ModelMap model, HttpServletRequest request) {

		return "home";
	}

	@RequestMapping(value = "/createAndAssignFeedbackType", method = RequestMethod.GET)
	public String CreateAndAssignFeedbackType(ModelMap model, Model uiModel, HttpServletRequest request) {

		List<FeedBackTypeTable> feedbacks = feedbackservice.getFeedbackList();
		uiModel.addAttribute("FeedBackList", feedbacks);
		List<Company> companies = userService.getAllModeratorClients();
		model.addAttribute("hidemenu", "hide");
		model.addAttribute("companiesList", companies);
		return "CreateAndAssignFeedbackType";
	}

	@RequestMapping(value = "/getFeedbackCompanyAssociation", method = RequestMethod.GET)
	@ResponseBody
	public String getFeedbackCompanyAssociation(ModelMap model, @RequestParam("companyId") String companyId,
			Model uiModel, HttpServletRequest request) {
		// Thread.sleep(2000);
		List<CompanyFeedBackTypeAssociation> companyFeedBackTypeAssociations = feedbackservice
				.getListOfFeedBackTypeAssociationWithCompanyAccordingToCompanyId(companyId);
		JSONObject mainObj = new JSONObject();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = null;
		try {
			json = ow.writeValueAsString(companyFeedBackTypeAssociations);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping(value = "/saveCompanyFeedBackTypeAssociation", method = RequestMethod.GET)
	@ResponseBody
	public String saveCompanyFeedBackTypeAssociation(Model model, HttpServletRequest request) {
		String companyId = request.getParameter("companyId");
		Company company = new Company();
		company.setCompanyId(Integer.parseInt(companyId));

		feedbackservice.deleteCompanyFeedBackTypeAssociationByCompany(company);

		Enumeration<String> s = request.getParameterNames();
		while (s.hasMoreElements()) {
			String name = s.nextElement();
			if (name.equalsIgnoreCase("companyId")) {

			} else if (request.getParameter(name).equalsIgnoreCase("on")) {

				CompanyFeedBackTypeAssociation companyFeedBackTypeAssociation = new CompanyFeedBackTypeAssociation();
				companyFeedBackTypeAssociation.setCompany(company);

				FeedBackTypeTable feedBackTypeTable = new FeedBackTypeTable();
				feedBackTypeTable.setId(Integer.parseInt(name));

				companyFeedBackTypeAssociation.setCompany(company);
				companyFeedBackTypeAssociation.setFeedBackType(feedBackTypeTable);
				feedbackservice.saveCompanyFeedBackTypeAssociation(companyFeedBackTypeAssociation);

			}
		}
		return "success";
	}

	@RequestMapping(value = "/saveFeedBackTypeTable", method = RequestMethod.GET)
	@ResponseBody
	public String saveFeedBackTypeTable(@RequestParam("feedbackType") String feedbackType, ModelMap model,
			Model uiModel, HttpServletRequest request) {

		System.out.println("nikhil");
		List<FeedBackTypeTable> feedbacks = feedbackservice.getFeedbackList();

		FeedBackTypeTable feedBackTypeTable = new FeedBackTypeTable();
		feedBackTypeTable.setFeedbackType(feedbackType);

		String id = feedbackservice.addFeedbackTypes(feedBackTypeTable);
		return id;

	}

	@RequestMapping(value = "/homeCounterByDate", method = RequestMethod.POST)
	@ResponseBody
	public String homeCounterByDate(@RequestParam("fromDate") String fromDate, @RequestParam("toDate") String toDate,
			HttpServletRequest request, Model uiModel) {

		HttpSession httpSession = request.getSession();
		UserModel userModelFrom = (UserModel) httpSession.getAttribute("UserFrom");
		UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");

		List<FeedBackTypeTable> feedbackType = new ArrayList<FeedBackTypeTable>();

		List<CompanyFeedBackTypeAssociation> companyFeedBackTypeAssociations = null;

		if (userModelFrom.getCompany().getCompanyName().equalsIgnoreCase("Coreflex")) {
			uiModel.addAttribute("hidemenuSwitch", "switchCompany");
			companyFeedBackTypeAssociations = feedbackservice
					.getListOfFeedBackTypeAccoringToCompany(userModelTo.getCompany());
		} else if (userModelTo.getCompany().getCompanyName().equalsIgnoreCase("Coreflex")) {

			companyFeedBackTypeAssociations = feedbackservice
					.getListOfFeedBackTypeAccoringToCompany(userModelFrom.getCompany());
		}

		System.out.println(companyFeedBackTypeAssociations.size() + "        companyFeedBackTypeAssociations");
		for (CompanyFeedBackTypeAssociation association : companyFeedBackTypeAssociations) {
			feedbackType.add(association.getFeedBackType());
		}
		List<FeedBackCounterForHome> feedBackCounterForHomes = null;
		try {
			String[] fromSplit = fromDate.split("/");
			String[] toSplit = toDate.split("/");

			String fromSplitDate = fromSplit[2] + "-" + fromSplit[1] + "-" + fromSplit[0];
			String toSplitDate = toSplit[2] + "-" + toSplit[1] + "-" + toSplit[0];

			Date actFromDate = null;
			Date actToDate = null;

			SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				actFromDate = df.parse(fromSplitDate + " 00:00:00");
				actToDate = df.parse(toSplitDate + " 23:59:59");

			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			System.out.println(actFromDate + "   actFromDate");

			feedBackCounterForHomes = feedbackservice.getfeedBackCounterForHomeByDate(feedbackType, request,
					actFromDate, actToDate);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {

			JSONObject js = new JSONObject();
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			js.put("counterList", ow.writeValueAsString(feedBackCounterForHomes));
			String json = js.toString();
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@RequestMapping(value = "/feedbackcoms", method = RequestMethod.GET)
	public ModelAndView loadFeedCom(@RequestParam("feedId") String feedId, Model uiModel, HttpServletRequest request) {

		FeedBackTypeTable typeTable = new FeedBackTypeTable();
		typeTable.setId(Integer.parseInt(feedId));
		uiModel.addAttribute("feedId", feedId);

		List<FeedBackSonumberSortingByDate> feedbacks = null;

		feedbacks = feedbackservice.getSortedFeedBackListAccordingtToFeedbackId(typeTable, request);
		uiModel.addAttribute("FeedBackList", feedbacks);
		
		UserModel userMessage = (UserModel) request.getSession().getAttribute("UserMessage");

		List<Operator> operators = operatorservice.getListOfActiveOperators();
		uiModel.addAttribute("feedBackTypeId", feedId);
		uiModel.addAttribute("operatorlist", operators);
		String feedBackName = feedbackservice.getFeedBackNameUsingFeedBackId(Integer.parseInt(feedId));//// thiss
		String mod = (String) request.getSession().getAttribute("moderator");
		// List<RedFlag> flags = feedbackservice.getListOfRedFlag(feedId,
		// request);
		/*
		 * uiModel.addAttribute("flags", flags); System.out.println(flags.size()
		 * + "asdfasfasffasf");
		 */

		UserModel userModel = (UserModel) request.getSession().getAttribute("UserFrom");
		String CompanyName = userModel.getCompany().getCompanyName();

		if (CompanyName.equalsIgnoreCase("Coreflex")) {
			uiModel.addAttribute("hidemenuSwitch", "switchCompany");
		}

		List<Object[]> yearList = feedbackservice.getDistincYear(request, typeTable);

		Set<Integer> yearListInteger = new HashSet<Integer>();

		for (Object[] aRow : yearList) {
			/*
			 * Long sum = (Long) aRow[0]; Integer category = (Integer) aRow[1];
			 * System.out.println(category + " - " + sum);
			 */
			yearListInteger.add((Integer) aRow[1]);
		}

		TreeMap<Integer, Long> finalResultOfYearWithTotalIssueCount = new TreeMap<Integer, Long>();

		for (Integer year : yearListInteger) {
			for (Object[] aRow : yearList) {
				try {
					Long l = finalResultOfYearWithTotalIssueCount.get(year);
					if ((aRow[1] + "").equalsIgnoreCase(year + "")) {
						if (l == null) {
							finalResultOfYearWithTotalIssueCount.put(year, (Long) aRow[0]);
						} else if (l != null) {
							finalResultOfYearWithTotalIssueCount.put(year, (Long) aRow[0] + l);
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		
		uiModel.addAttribute("userMessageForAssinee", userMessage.getFirstName()+" - "+userMessage.getLastName()+" / "+userMessage.getCompany().getCompanyName());
		uiModel.addAttribute("hidemenu", "hideAdmin");
		uiModel.addAttribute("feedBackName", feedBackName);
		uiModel.addAttribute("moderator", mod);
		uiModel.addAttribute("DistincYearList", yearList);

		uiModel.addAttribute("YearWithTotalIssueCount", finalResultOfYearWithTotalIssueCount);

		System.out.println("mod =  " + mod);
		return new ModelAndView("FeedcomFirstView");
	}

	@RequestMapping(value = "/getCountOfMonthAccordingToYear", method = RequestMethod.POST)
	@ResponseBody
	public String getCountOfMonthAccordingToYear(@RequestParam("year") String year,
			@RequestParam("feedId") String feedId, HttpServletRequest request) {

		JSONObject mainObj = new JSONObject();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = null;
		try {

			JSONObject js = new JSONObject();
			HashMap<String, List<Object[]>> result = feedbackservice.getMonthWiseOpenIssues(year, feedId, request);
			List<Object[]> open = result.get("open");
			List<Object[]> close = result.get("close");

			Set<Integer> openMonth = new HashSet<Integer>();
			Set<Integer> closeMonth = new HashSet<Integer>();

			for (Object[] aRow : open) {

				openMonth.add((Integer) aRow[1]);
			}

			for (Object[] aRow : close) {

				closeMonth.add((Integer) aRow[1]);
			}

			TreeMap<Integer, Long> finalResultOfOpenMonth = new TreeMap<Integer, Long>();

			for (Integer month : openMonth) {
				for (Object[] aRow : open) {
					try {
						Long l = finalResultOfOpenMonth.get(month);
						if ((aRow[1] + "").equalsIgnoreCase(month + "")) {
							if (l == null) {
								finalResultOfOpenMonth.put(month, (Long) aRow[0]);
							} else if (l != null) {
								finalResultOfOpenMonth.put(month, (Long) aRow[0] + l);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			TreeMap<Integer, Long> finalResultOfCloseMonth = new TreeMap<Integer, Long>();

			for (Integer month : closeMonth) {
				for (Object[] aRow : close) {
					try {
						Long l = finalResultOfCloseMonth.get(month);
						if ((aRow[1] + "").equalsIgnoreCase(month + "")) {
							if (l == null) {
								finalResultOfCloseMonth.put(month, (Long) aRow[0]);
							} else if (l != null) {

								System.out.println("else if block");
								finalResultOfCloseMonth.put(month, (Long) aRow[0] + l);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			Set<Integer> commonMonthListclose = finalResultOfCloseMonth.keySet();
			Set<Integer> commonMonthListopen = finalResultOfOpenMonth.keySet();

			TreeSet<Integer> commonMonthList = new TreeSet<Integer>();
			commonMonthList.addAll(commonMonthListopen);
			commonMonthList.addAll(commonMonthListclose);

			LinkedHashMap<String, HashMap<Long, Long>> hashMap = new LinkedHashMap<String, HashMap<Long, Long>>();

			for (Integer integer : commonMonthList) {
				HashMap<Long, Long> innercount = new HashMap<Long, Long>();
				String month = "";

				switch (integer) {
				case 1:
					month = "January";
					break;
				case 2:
					month = "February";
					break;
				case 3:
					month = "March";
					break;
				case 4:
					month = "April";
					break;
				case 5:
					month = "May";
					break;
				case 6:
					month = "June";
					break;
				case 7:
					month = "July";
					break;
				case 8:
					month = "August";
					break;
				case 9:
					month = "September";
					break;
				case 10:
					month = "October";
					break;
				case 11:
					month = "November";
					break;
				case 12:
					month = "December";
					break;
				}

				Long closemonth = finalResultOfCloseMonth.get(integer);
				if (closemonth == null) {
					closemonth = (long) 0;
				}

				Long openmonth = finalResultOfOpenMonth.get(integer);
				if (openmonth == null) {
					openmonth = (long) 0;
				}

				innercount.put(openmonth, closemonth);
				hashMap.put(month, innercount);
			}

			Set<String> monthName = hashMap.keySet();
			List<Long> openList = new ArrayList<Long>();
			List<Long> closeList = new ArrayList<Long>();

			for (String string : monthName) {

				HashMap<Long, Long> list = hashMap.get(string);
				Set<Long> resultLong = list.keySet();
				for (Long long1 : resultLong) {
					openList.add(long1);
					closeList.add(list.get(long1));
				}

			}

			System.out.println(hashMap + "   hashMap");

			// System.out.println(finalResultOfOpenMonth+"
			// finalResultOfOpenMonth");
			// System.out.println(finalResultOfCloseMonth+"
			// finalResultOfCloseMonth");

			js.put("monthName", ow.writeValueAsString(monthName));
			js.put("openCount", ow.writeValueAsString(openList));
			js.put("closeCount", ow.writeValueAsString(closeList));
			json = js.toString();

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;

	}

	//

	@RequestMapping(value = "/getCountOfDayAccordingToMonthYear", method = RequestMethod.POST)
	@ResponseBody
	public String getCountOfDayAccordingToMonthYear(@RequestParam("year") String year,
			@RequestParam("month") String month, @RequestParam("feedId") String feedId, HttpServletRequest request) {

		System.out.println("Welcome");
		int monthInInt = 0;
		switch (month) {
		case "January":
			monthInInt = 1;
			break;
		case "February":
			monthInInt = 2;
			break;
		case "March":
			monthInInt = 3;
			break;
		case "April":
			monthInInt = 4;
			break;
		case "May":
			monthInInt = 5;
			break;
		case "June":
			monthInInt = 6;
			break;
		case "July":
			monthInInt = 7;
			break;
		case "August":
			monthInInt = 8;
			break;
		case "September":
			monthInInt = 9;
			break;
		case "October":
			monthInInt = 10;
			break;
		case "November":
			monthInInt = 11;
			break;
		case "December":
			monthInInt = 12;
			break;
		}

		System.out.println(year + "year " + month + "month");
		JSONObject mainObj = new JSONObject();
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		String json = null;
		try {

			JSONObject js = new JSONObject();
			HashMap<String, List<Object[]>> result = feedbackservice.getdatewiseOpenCloseIssues(year, monthInInt + "",
					feedId, request);

			List<Object[]> open = result.get("open");
			List<Object[]> close = result.get("close");

			System.out.println(result + "  nik resulr=t");

			Set<Integer> openDay = new HashSet<Integer>();
			Set<Integer> closeDay = new HashSet<Integer>();

			for (Object[] aRow : open) {

				openDay.add((Integer) aRow[1]);
			}

			for (Object[] aRow : close) {

				closeDay.add((Integer) aRow[1]);
			}

			TreeMap<Integer, Long> finalResultOfOpenDay = new TreeMap<Integer, Long>();

			for (Integer day : openDay) {
				for (Object[] aRow : open) {
					try {
						Long l = finalResultOfOpenDay.get(day);
						if ((aRow[1] + "").equalsIgnoreCase(day + "")) {
							if (l == null) {
								finalResultOfOpenDay.put(day, (Long) aRow[0]);
							} else if (l != null) {
								finalResultOfOpenDay.put(day, (Long) aRow[0] + l);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			TreeMap<Integer, Long> finalResultOfCloseDay = new TreeMap<Integer, Long>();

			for (Integer day : closeDay) {
				for (Object[] aRow : close) {
					try {
						Long l = finalResultOfCloseDay.get(day);
						if ((aRow[1] + "").equalsIgnoreCase(day + "")) {
							if (l == null) {
								finalResultOfCloseDay.put(day, (Long) aRow[0]);
							} else if (l != null) {

								System.out.println("else if block");
								finalResultOfCloseDay.put(day, (Long) aRow[0] + l);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

			Set<Integer> commonMonthListclose = finalResultOfOpenDay.keySet();
			Set<Integer> commonMonthListopen = finalResultOfCloseDay.keySet();

			Set<Integer> commonMonthList = new HashSet<Integer>();
			commonMonthList.addAll(commonMonthListopen);
			commonMonthList.addAll(commonMonthListclose);

			System.out.println(commonMonthList + "common lisrt");

			TreeMap<String, HashMap<Long, Long>> hashMap = new TreeMap<String, HashMap<Long, Long>>();

			for (Integer integer : commonMonthList) {
				HashMap<Long, Long> innercount = new HashMap<Long, Long>();

				Long closeday = finalResultOfCloseDay.get(integer);
				if (closeday == null) {
					closeday = (long) 0;
				}

				Long openday = finalResultOfOpenDay.get(integer);
				if (openday == null) {
					openday = (long) 0;
				}

				innercount.put(openday, closeday);
				hashMap.put(integer + "", innercount);
			}

			Set<String> day = hashMap.keySet();
			List<Long> openList = new ArrayList<Long>();
			List<Long> closeList = new ArrayList<Long>();

			for (String string : day) {

				HashMap<Long, Long> list = hashMap.get(string);
				Set<Long> resultLong = list.keySet();
				for (Long long1 : resultLong) {
					openList.add(long1);
					closeList.add(list.get(long1));
				}

			}
			js.put("day", ow.writeValueAsString(day));
			js.put("openCount", ow.writeValueAsString(openList));
			js.put("closeCount", ow.writeValueAsString(closeList));
			json = js.toString();
			return json;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";

	}

	@RequestMapping(value = "/adminfeedbackcom", method = RequestMethod.GET)
	public ModelAndView loadadminfeedbackcom(@RequestParam("userId") String CompanyUserId, Model uiModel,
			HttpServletRequest request) {
		System.out.println("coming admin" + CompanyUserId);
		UserModel userModel2 = loginService.getclientCompanyAdminById(Integer.parseInt(CompanyUserId));

		request.getSession().setAttribute("CompanyId", CompanyUserId);
		HttpSession httpSession = request.getSession();
		httpSession.setAttribute("UserTo", userModel2);
		// uiModel.addAttribute("FeedBackList",feedbacks);
		return new ModelAndView("redirect:/configureHome");
	}

	@RequestMapping(value = "/demo", method = RequestMethod.GET)
	public void dosomething(@RequestParam("path") String filename, HttpServletResponse response,
			HttpServletRequest request) {

		ServletContext context = request.getServletContext();

		String pathact = request.getServletContext().getRealPath("/resources/CfsFiles");
		try {

		} catch (Exception e) {
			// uploadDownloadService.saveUrlForFileUploadAndDownload(pathact);
		}
		try {

			FtpInfo ftpInfo = fileUploadService.getFtpInfo();

			String server = ftpInfo.getServerName();
			int port = ftpInfo.getPort();

			String user = ftpInfo.getUserName();
			String pass = ftpInfo.getPassword();

			FTPClient ftpClient = new FTPClient();
			try {

				ftpClient.connect(server, port);
				ftpClient.login(user, pass);
				ftpClient.enterLocalPassiveMode();
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				InputStream inputStream = null;
				System.out.println(filename + "              filename");
				inputStream = ftpClient.retrieveFileStream("/TEST/" + filename);

				response.setContentType("application/octet-stream");
				// Response header
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + filename.substring(13, filename.length()) + "\"");
				// Read from the file and write into the response
				System.out.println("done");
				OutputStream os = response.getOutputStream();
				byte[] buffer = new byte[1024];
				int len;
				while ((len = inputStream.read(buffer)) != -1) {
					os.write(buffer, 0, len);
				}
				os.flush();
				os.close();

				inputStream.close();

			} catch (IOException ex) {
				System.out.println("Error: " + ex.getMessage());
				ex.printStackTrace();
			} finally {
				try {
					if (ftpClient.isConnected()) {
						ftpClient.logout();
						ftpClient.disconnect();
					}
				} catch (IOException ex) {
					ex.printStackTrace();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@RequestMapping(value = "/configureHome", method = RequestMethod.GET)
	public ModelAndView configureHome(Model uiModel, HttpServletRequest request) {

		HttpSession httpSession = request.getSession();
		UserModel userModelFrom = (UserModel) httpSession.getAttribute("UserFrom");
		UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");
		// uiModel.addAttribute("forUser","");

		// List<FeedBackTypeTable> feedbackType =
		// feedbackservice.getFeedbackList();

		List<FeedBackTypeTable> feedbackType = new ArrayList<FeedBackTypeTable>();
		List<CompanyFeedBackTypeAssociation> companyFeedBackTypeAssociations = null;
		if (userModelFrom.getCompany().getCompanyName().equalsIgnoreCase("Coreflex")) {
			uiModel.addAttribute("hidemenuSwitch", "switchCompany");
			companyFeedBackTypeAssociations = feedbackservice
					.getListOfFeedBackTypeAccoringToCompany(userModelTo.getCompany());
		} else if (userModelTo.getCompany().getCompanyName().equalsIgnoreCase("Coreflex")) {

			companyFeedBackTypeAssociations = feedbackservice
					.getListOfFeedBackTypeAccoringToCompany(userModelFrom.getCompany());
		}

		System.out.println(companyFeedBackTypeAssociations.size() + "        companyFeedBackTypeAssociations");
		for (CompanyFeedBackTypeAssociation association : companyFeedBackTypeAssociations) {
			feedbackType.add(association.getFeedBackType());
		}
		List<FeedBackCounterForHome> feedBackCounterForHomes = null;
		try {

			feedBackCounterForHomes = feedbackservice.getfeedBackCounterForHome(feedbackType, request);
		} catch (Exception e) {
			e.printStackTrace();
		}

		uiModel.addAttribute("FeedBackCount", feedBackCounterForHomes);

		uiModel.addAttribute("FeedBackName", feedbackType);

		String moderator = (String) httpSession.getAttribute("moderator");
		try {
			if (moderator.equals(null) || moderator.equals("")) {

			} else {
				uiModel.addAttribute("moderator", "moderator");
			}
		} catch (Exception e) {

		}
		uiModel.addAttribute("hidemenu", "hideAdmin");

		return new ModelAndView("Home");
	}

	@RequestMapping(value = "/feedcomChat", method = RequestMethod.GET)
	public ModelAndView feedcomChat(@RequestParam("soNumber") String soNumber,
			@RequestParam("feedbackType") String feedbackType, Model uiModel, HttpServletRequest request) {
		
		soNumber=soNumber.trim();

		System.out.println(feedbackType + " feed type " + soNumber + "soNumber");
		List<Feedback> feedbacks = feedbackservice.getListOfChatingUsingSoNumberAndFeedBackType(soNumber, feedbackType,
				request);
		Collections.sort(feedbacks, new FeedBackCountComparator());
		uiModel.addAttribute("feedbacks", feedbacks);
		UserModel userModelUser = (UserModel) request.getSession().getAttribute("UserMessage");
		uiModel.addAttribute("usermsgId", userModelUser.getUserId());
		uiModel.addAttribute("usermsgcompany", userModelUser.getCompany().getCompanyId());

		UserModel userModel = (UserModel) request.getSession().getAttribute("UserFrom");
		String CompanyName = userModel.getCompany().getCompanyName();
		int id = userModelUser.getUserId();

		if (CompanyName.equalsIgnoreCase("Coreflex")) {
			uiModel.addAttribute("hidemenuSwitch", "switchCompany");
		} else {
			uiModel.addAttribute("ShowStatus", "true");
		}

		uiModel.addAttribute("userFrom", userModel.getUserId());

		UserModel userModelmsg = (UserModel) request.getSession().getAttribute("UserMessage");
		uiModel.addAttribute("userfeedbackchat", userModelmsg.getFirstName() + " " + userModelmsg.getLastName());

		String countForChatingFromTo = feedbackservice.getMaxOfFeedBackChattingCounter(soNumber, feedbackType);
		String[] s = countForChatingFromTo.split("#");
		int chatto = 0;
		int chatfor = 0;
		try {
			chatfor = Integer.parseInt(s[0]);
		} catch (Exception e) {

		}
		try {
			chatto = Integer.parseInt(s[1]);
		} catch (Exception e) {

		}
		// String feedBackName =
		// feedbackservice.getFeedBackNameUsingFeedBackId(Integer.parseInt(feedId));
		uiModel.addAttribute("userFromCounter", chatfor);
		uiModel.addAttribute("userToCounter", chatto);
		String rflag = feedbackservice.validateFlag(soNumber, feedbackType, request);
		uiModel.addAttribute("rflag", rflag);
		if (feedbacks.size() > 0) {
			uiModel.addAttribute("FeedId", feedbacks.get(0).getFeedBackType().getId());
			uiModel.addAttribute("FeedbackType", feedbacks.get(0).getFeedBackType().getFeedbackType());
			uiModel.addAttribute("soNumber", feedbacks.get(0).getSoNumber());
			uiModel.addAttribute("status", feedbacks.get(0).getStatus().getStatusId());
			uiModel.addAttribute("operator", feedbacks.get(0).getOperator().getUserName());
			
			System.out.println("operator from controller     "+feedbacks.get(0).getOperator().getUserName());
			uiModel.addAttribute("subject", feedbacks.get(0).getEmailSubject());

			ServletContext context = request.getServletContext();

			String pathact = request.getServletContext().getRealPath("/resources/CfsFiles");

			try {
				// uploadDownloadService.saveUrlForFileUploadAndDownload(pathact);
			} catch (Exception e) {
				e.printStackTrace();
			}

			File dir1 = new File(pathact);
			if (!dir1.exists())
				dir1.mkdirs();
			String AlldestFilePath = dir1.getAbsolutePath() + File.separator;

			uiModel.addAttribute("AlldestFilePath", AlldestFilePath);
			String type = "Feedback";
			userService.SaveUserLastVisitedTime(id, type);
			uiModel.addAttribute("hidemenu", "hideAdmin");
			return new ModelAndView("feedcomChatting");
		}

		FeedBackTypeTable typeTable = new FeedBackTypeTable();
		typeTable.setId(Integer.parseInt(feedbackType));
		List<Feedback> feedbacks1 = feedbackservice.getfeedBackListForFeedBackId(typeTable, request);
		uiModel.addAttribute("feedId", feedbackType);
		uiModel.addAttribute("FeedBackList", feedbacks1);
		List<Operator> operators = operatorservice.getListOfActiveOperators();
		uiModel.addAttribute("feedBackTypeId", feedbackType);
		uiModel.addAttribute("operatorlist", operators);
		String feedBackName = feedbackservice.getFeedBackNameUsingFeedBackId(Integer.parseInt(feedbackType));
		String mod = (String) request.getSession().getAttribute("moderator");
		List<RedFlag> flags = feedbackservice.getListOfRedFlag(feedbackType, request);
		uiModel.addAttribute("flags", flags);
		System.out.println(flags.size() + "asdfasfasffasf");
		uiModel.addAttribute("feedBackName", feedBackName);
		uiModel.addAttribute("moderator", mod);
		System.out.println("mod =  " + mod);
		uiModel.addAttribute("hidemenu", "hideAdmin");
		return new ModelAndView("FeedcomFirstView");

	}

	@RequestMapping(value = "/saveChatForfeedbackcom", method = RequestMethod.POST)
	@ResponseBody
	public String saveChatForfeedbackcom(@RequestParam("soNumber") String soNumber,
			@RequestParam("status") String status, @RequestParam("chatBox") String chatBox,
			@RequestParam("feedbackType") String feedbackType, @RequestParam("operator") String operator,
			@RequestParam("redflag") String redflag, @RequestParam("subject") String subject,
			@RequestParam("attachment") String attachment, Model uiModel, HttpServletRequest request) {
		
		
			soNumber=soNumber.trim();
		String chattingcounterFromTo = feedbackservice.saveChattingForFeedCom(soNumber, status, subject, attachment,
				request.getParameter("chatBox"), feedbackType, operator);
		if (status.equalsIgnoreCase("2")) {
			System.out.println("delete");
			int feedid = feedbackservice.getfeedBackTypeIdByFeedBackName(feedbackType);
			String val = feedbackservice.validateFlag(soNumber, feedid + "", request);
			System.out.println("validate flag " + feedid + "  " + val);

			feedbackservice.deleteFlag(soNumber, feedid + "", request);
		} else if (redflag.equalsIgnoreCase("Active")) {
			int feedid = feedbackservice.getfeedBackTypeIdByFeedBackName(feedbackType);
			String val = feedbackservice.validateFlag(soNumber, feedid + "", request);
			if (val.equalsIgnoreCase("NotAvailable")) {
				feedbackservice.saveRedFlag(soNumber, feedid + "", request);
			}
		} else {

		}

		return chattingcounterFromTo;
	}

	@RequestMapping(value = "/livechat", method = RequestMethod.POST)
	@ResponseBody
	public String livechat(@RequestParam("soNumber") String soNumber, @RequestParam("feedbackType") String feedbackType,
			@RequestParam("userToCounter") String userToCounter, Model uiModel, HttpServletRequest request)
			throws JsonGenerationException, JsonMappingException, IOException {

		soNumber=soNumber.trim();
		try {
			// Thread.sleep(2000);
			List<Feedback> feedbacks = feedbackservice.getLiveChatUserTo(soNumber, feedbackType, userToCounter,
					request);
			ArrayList<String> newList = new ArrayList<String>();
			JSONObject mainObj = new JSONObject();
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(feedbacks);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	@RequestMapping(value = "/livechatUserFrom", method = RequestMethod.POST)
	@ResponseBody
	public String livechatUserFrom(@RequestParam("soNumber") String soNumber,
			@RequestParam("feedbackType") String feedbackType, @RequestParam("userFromCounter") String userFromCounter,
			Model uiModel, HttpServletRequest request)
			throws JsonGenerationException, JsonMappingException, IOException {
		
		soNumber=soNumber.trim();

		try {
			// Thread.sleep(2000);

			List<Feedback> feedbacks = feedbackservice.getLiveChatUserFrom(soNumber, feedbackType, userFromCounter,
					request);
			ArrayList<String> newList = new ArrayList<String>();
			JSONObject mainObj = new JSONObject();
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String json = ow.writeValueAsString(feedbacks);
			return json;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

	//

	@RequestMapping(value = "/Newlivechat", method = RequestMethod.POST)
	@ResponseBody
	public String NewlivechatFeedCom(@RequestParam("soNumber") String soNumber,
			@RequestParam("feedbackType") String feedbackType, @RequestParam("Operator") String operator,
			@RequestParam("chatBox") String chatingText, @RequestParam("attachement") String attachement,
			@RequestParam("subject") String subject,

			Model uiModel, final HttpServletRequest request)
			throws JsonGenerationException, JsonMappingException, IOException {

		System.out.println("data ramesh");
		System.out.println("file" + attachement);
		System.out.println(subject);
		soNumber=soNumber.trim();

		try {
			String validate = feedbackservice.validateNewFeedComChating(soNumber, feedbackType, request);
			if ("NotAvailable".equalsIgnoreCase(validate)) {

				final Feedback feedback = feedbackservice.saveNewFeedComChat(soNumber, feedbackType, chatingText, operator,
						attachement, subject, request);

				new Thread() {
					public void run() {
						HttpSession httpSession = request.getSession();
						UserModel userModelFrom = (UserModel) httpSession.getAttribute("UserFrom");
						UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");
						UserModel userModelmsg = (UserModel) httpSession.getAttribute("UserMessage");
					
						 List<UserModel> userModels = loginService
								.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userModelFrom.getCompany());
						userModels.addAll(loginService.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userModelTo.getCompany()));
						 FcmNotification fcmNotification = notificationService.getFcmNotification();
						 
						 System.out.println("notifiaction start");
						notificationService.sendFeedbackNotification(feedback, fcmNotification, userModels, request);
						System.out.println("notifiaction end");
					}
				}.start();
				

			} else {
				System.out.println("data again");
				return "AlreadyAvailable";
			}

		} catch (Exception e) {

		}
		return "";
	}

	@RequestMapping(value = "/saveRedFlag", method = RequestMethod.POST)
	public ModelAndView saveRedFlag(@RequestParam("soNumber") String soNumber,
			@RequestParam("feedbackType") String feedbackType, Model uiModel, HttpServletRequest request) {
		soNumber=soNumber.trim();
		feedbackservice.saveRedFlag(soNumber, feedbackType, request);
		return null;

	}

	@RequestMapping(value = "/replyemailfeedbackcom", method = RequestMethod.POST)
	@ResponseBody
	public String replyemailfeedbackcom(@RequestParam("subject") String subject,
			@RequestParam("chatBox") String chatBox, @RequestParam("attachment") String attachment, Model uiModel,
			HttpServletRequest request) {
		ReplyToEmailFeedCom replyToEmail = new ReplyToEmailFeedCom();
		HttpSession httpSession = request.getSession();
		UserModel userModel = (UserModel) httpSession.getAttribute("UserFrom");
		UserModel userTo = (UserModel) httpSession.getAttribute("UserTo");
		// attachments

		if (userModel.getCompany().getCompanyId() == 1) {

			List<String> cfsemailList = new ArrayList<String>();
			List<String> clientemailList = new ArrayList<String>();

			List<EmailSendReceivePermission> emailSendReceivePermissions = emailSendAndReceivePermissionService
					.getEmailPermissionUserListAccordingToCompany(userModel.getCompany().getCompanyId() + "");
			List<EmailSendReceivePermission> emailSendReceivePermissionsTo = emailSendAndReceivePermissionService
					.getEmailPermissionUserListAccordingToCompany(userTo.getCompany().getCompanyId() + "");

			for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissions) {
				cfsemailList.add(emailSendReceivePermission.getUserModel().getEmail());
			}

			for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissionsTo) {
				clientemailList.add(emailSendReceivePermission.getUserModel().getEmail());
			}

			System.out.println("List Of emailers " + Arrays.toString(cfsemailList.toArray()) + "      second     lst   "
					+ Arrays.toString(clientemailList.toArray()));
			System.out.println("emailSendAndReceivePermissionService   cfsemailList   " + cfsemailList.size()
					+ "  clientemailList    " + clientemailList.size());

			UserModel usermsg = (UserModel) httpSession.getAttribute("UserMessage");

			String userFirstAndLastName = "            Sender :  " + usermsg.getFirstName() + " "
					+ usermsg.getLastName();
			try {
				replyToEmail.sendEmailReply(subject, clientemailList, cfsemailList, attachment,
						chatBox + userFirstAndLastName, request, fileUploadService ,emailSendAndReceivePermissionService);
				
			} catch (Exception e) {
				e.printStackTrace();
			}

		} else {
			List<String> cfsemailList = new ArrayList<String>();
			List<String> clientemailList = new ArrayList<String>();

			List<EmailSendReceivePermission> emailSendReceivePermissions = emailSendAndReceivePermissionService
					.getEmailPermissionUserListAccordingToCompany(userModel.getCompany().getCompanyId() + "");

			List<EmailSendReceivePermission> emailSendReceivePermissionsTo = emailSendAndReceivePermissionService
					.getEmailPermissionUserListAccordingToCompany(userTo.getCompany().getCompanyId() + "");

			for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissions) {
				clientemailList.add(emailSendReceivePermission.getUserModel().getEmail());
			}

			for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissionsTo) {
				cfsemailList.add(emailSendReceivePermission.getUserModel().getEmail());
			}

			/*
			 * System.out.println("List Of emailers "
			 * +Arrays.toString(cfsemailList.toArray())+
			 * "      second     lst   "
			 * +Arrays.toString(clientemailList.toArray())); System.out.println(
			 * "emailSendAndReceivePermissionServi   cedsfsfs   cfsemailList   "
			 * +cfsemailList.size() + "  clientemailList    "
			 * +clientemailList.size());
			 */

			UserModel usermsg = (UserModel) httpSession.getAttribute("UserMessage");

			String userFirstAndLastName = "            Sender :  " + usermsg.getFirstName() + " "
					+ usermsg.getLastName();

			replyToEmail.sendEmailReply(subject, cfsemailList, clientemailList, attachment,
					chatBox + userFirstAndLastName, request, fileUploadService,emailSendAndReceivePermissionService);
		}

		return "success";

	}

	@RequestMapping(value = "/sendFeedBackEmail", method = RequestMethod.POST)
	@ResponseBody
	public String SendFirstQueryEmail(@RequestParam("tempsonumber") String tempsonumber,
			@RequestParam("avayNum") String avayNum, @RequestParam("docNum") String docNum,
			@RequestParam("subject") String subject, @RequestParam("chatBox") String chatBox,
			@RequestParam("feedBackName") String feedBackName, @RequestParam("attachment") String attachment,
			Model uiModel, HttpServletRequest request) throws MessagingException {

		HttpSession httpSession = request.getSession();
		UserModel userModel = (UserModel) httpSession.getAttribute("UserFrom");
		UserModel userTo = (UserModel) httpSession.getAttribute("UserTo");

		String companyName = userModel.getCompany().getCompanyName();
		List<String> clientCompanyemail = new ArrayList<String>();
		List<String> corflexemailIdList = new ArrayList<String>();

		if (companyName.equalsIgnoreCase("Coreflex")) {

			List<EmailSendReceivePermission> emailSendReceivePermissions = emailSendAndReceivePermissionService
					.getEmailPermissionUserListAccordingToCompany(userModel.getCompany().getCompanyId() + "");
			List<EmailSendReceivePermission> emailSendReceivePermissionsTo = emailSendAndReceivePermissionService
					.getEmailPermissionUserListAccordingToCompany(userTo.getCompany().getCompanyId() + "");

			for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissions) {
				corflexemailIdList.add(emailSendReceivePermission.getUserModel().getEmail());
			}

			for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissionsTo) {
				clientCompanyemail.add(emailSendReceivePermission.getUserModel().getEmail());
			}

		} else {
			List<EmailSendReceivePermission> emailSendReceivePermissions = emailSendAndReceivePermissionService
					.getEmailPermissionUserListAccordingToCompany(userModel.getCompany().getCompanyId() + "");
			List<EmailSendReceivePermission> emailSendReceivePermissionsTo = emailSendAndReceivePermissionService
					.getEmailPermissionUserListAccordingToCompany(userTo.getCompany().getCompanyId() + "");

			for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissionsTo) {
				corflexemailIdList.add(emailSendReceivePermission.getUserModel().getEmail());
			}

			for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissions) {
				clientCompanyemail.add(emailSendReceivePermission.getUserModel().getEmail());
			}
		}
		// String email="rameshsable15@gmail.com";
		String corflexemailIdListArray[] = corflexemailIdList.toArray(new String[corflexemailIdList.size()]);
		String[] clientCompanyemailArray = clientCompanyemail.toArray(new String[clientCompanyemail.size()]);

		/*
		 * Properties properties = new Properties();
		 * 
		 * properties.setProperty("mail.host", "mail.coreflexsolutions.com");
		 * properties.setProperty("mail.port", "110");
		 * properties.setProperty("mail.transport.protocol", "imaps");
		 * 
		 * // properties.put("mail.pop3.starttls.enable", "true");
		 * properties.put("mail.smtp.auth", "true");
		 * properties.put("mail.smtp.starttls.enable", "true");
		 * properties.put("mail.smtp.host", "mail.coreflexsolutions.com");
		 * properties.put("mail.smtp.port", "25");
		 * 
		 * Session session = Session.getInstance(properties, new
		 * javax.mail.Authenticator() { protected PasswordAuthentication
		 * getPasswordAuthentication() { return new
		 * PasswordAuthentication("cfscommunicator@coreflexsolutions.com",
		 * "Asdf@123"); } });
		 */

		String headerString = "SoNumber :-  " + tempsonumber + " Avaya Number :- " + avayNum + "  Document Id :- "
				+ docNum;

		UserModel usermsg = (UserModel) httpSession.getAttribute("UserMessage");

		String userFirstAndLastName = "            Sender :  " + usermsg.getFirstName() + " " + usermsg.getLastName();

		String s = EmailUtil.sendEmail(attachment, clientCompanyemailArray, corflexemailIdListArray, subject,
				"<h3>" + headerString + "  </h3><br>" + chatBox + userFirstAndLastName, request, fileUploadService,emailSendAndReceivePermissionService);

		return s;

	}

}
