package com.xanadutec.coreflex.feedcomWebServices;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import com.xanadutec.coreflex.EmailPermission.EmailSendAndReceivePermissionService;
import com.xanadutec.coreflex.FileUploadDownload.FileUploadService;
import com.xanadutec.coreflex.company.CompanyService;
import com.xanadutec.coreflex.dropBox.DropBoxService;
import com.xanadutec.coreflex.feedback.FeedbackService;
import com.xanadutec.coreflex.login.LoginService;
import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.CompanyFeedBackTypeAssociation;
import com.xanadutec.coreflex.model.Documents;
import com.xanadutec.coreflex.model.EmailSendReceivePermission;
import com.xanadutec.coreflex.model.FcmNotification;
import com.xanadutec.coreflex.model.FeedBackCounterForHome;
import com.xanadutec.coreflex.model.FeedBackSonumberSortingByDate;
import com.xanadutec.coreflex.model.FeedBackTypeTable;
import com.xanadutec.coreflex.model.Feedback;
import com.xanadutec.coreflex.model.FtpInfo;
import com.xanadutec.coreflex.model.Mom;
import com.xanadutec.coreflex.model.Report;
import com.xanadutec.coreflex.model.UserModel;
import com.xanadutec.coreflex.mom.MomService;
import com.xanadutec.coreflex.notifiation.NotificationService;
import com.xanadutec.coreflex.userTask.UserService;

@Controller
public class FeedcomWebServicesController {
	@Autowired
	UserService userService;
	@Autowired
	private LoginService loginService;
	@Autowired
	private FeedbackService feedbackservice;
	@Autowired
	private FeedcomWebServicesService feedcomWebServicesService;
	@Autowired
	CompanyService companyService;
	@Autowired
	DropBoxService dropBoxService;
	@Autowired
	MomService momService;
	@Autowired
	FileUploadService fileUploadService;
	@Autowired
	DropBoxService dropboxservice;
	@Autowired
	EmailSendAndReceivePermissionService emailSendAndReceivePermissionService;
	@Autowired
	NotificationService notificationService;

	@RequestMapping(value = "/feedcom/login", method = RequestMethod.POST)
	@ResponseBody
	public String login(HttpServletRequest request, @RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("deviceToken") String deviceToken) {

		UserModel userModel = loginService.getUserByUnamePassword(username, password);

		JSONObject js = new JSONObject();
		String json = null;
		if (userModel != null) {

			if (deviceToken != null) {
				userModel.setDeviceToken(deviceToken);
				userService.addUser(userModel);
			}

			List<UserModel> userModelList = new ArrayList<UserModel>();
			List<UserModel> userFrom = null;
			List<UserModel> userTo = null;
			List<CompanyFeedBackTypeAssociation> companyFeedBackTypeAssociation = null;
			String companyName = userModel.getCompany().getCompanyName();
			if (companyName.equalsIgnoreCase("Coreflex")) {
				userModelList = loginService.getListOfUserIfUnamePasswordIsValid(username, password);
				companyFeedBackTypeAssociation = feedbackservice.getListOfAllCompanyFeedBackTypeAssociation();

			} else {

				companyFeedBackTypeAssociation = feedbackservice
						.getListOfFeedBackTypeAccoringToCompany(userModel.getCompany());

				userFrom = loginService.getListOfUserAccordingToCompany(userModel.getCompany().getCompanyId());
				userTo = loginService.getListOfCoreflexUser();
				userModelList.addAll(userFrom);
				userModelList.addAll(userTo);

			}
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

			try {
				js.put("userList", ow.writeValueAsString(userModelList));
				js.put("companyFeedBackTypeAssociation", ow.writeValueAsString(companyFeedBackTypeAssociation));
				js.put("code", "1000");
				json = js.toString();
			} catch (Exception e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		} else {

			try {
				js.put("code", "1001");
				json = js.toString();
			} catch (JSONException e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		return json;
	}

	@RequestMapping(value = "/feedcom/logout", method = RequestMethod.POST)
	@ResponseBody
	public String logout(HttpServletRequest request, @RequestParam("username") String username,
			@RequestParam("password") String password) {

		UserModel userModel = loginService.getUserByUnamePassword(username, password);

		JSONObject js = new JSONObject();
		String json = null;
		if (userModel != null) {

			userModel.setDeviceToken(null);
			userService.addUser(userModel);

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

			try {
				js.put("Logout", "Success");
				js.put("code", "1000");
				json = js.toString();
			} catch (Exception e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		} else {

			try {
				js.put("code", "1001");
				json = js.toString();
			} catch (JSONException e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		return json;
	}

	@RequestMapping(value = "/feedcom/getcommunicationCounterForFeedComQueryCom", method = RequestMethod.POST)
	@ResponseBody
	public String getFeedComQueryComChattingCounter(HttpServletRequest request,

			@RequestParam("username") String username, @RequestParam("password") String password) throws JSONException {

		UserModel userModel = loginService.getUserByUnamePassword(username, password);
		JSONObject js = new JSONObject();
		System.out.println("nikhil");
		String json = null;

		System.out.println(userModel.getUserName() + "     " + userModel.getUserName());

		if (userModel != null) {
			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();

			List<FeedBackTypeTable> feedbackType = null;

			List<FeedBackCounterForHome> feedBackCounterForHomes = null;

			HashMap<String, List<FeedBackCounterForHome>> MapCompanyToFeedBackCounterForHomes = new HashMap<String, List<FeedBackCounterForHome>>();
			System.out.println("Coreflex");
			if (userModel.getCompany().getCompanyName().equalsIgnoreCase("Coreflex")) {

				List<Company> companies = companyService.getListOfCompany();
				for (Company company : companies) {
					if (company.getCompanyName().equalsIgnoreCase("Coreflex")) {
						companies.remove(company);
						break;
					}
				}

				UserModel userFrom = loginService.getCoreflexAdmin();

				System.out.println(companies.size() + "companies list");
				for (Company company : companies) {

					feedbackType = new ArrayList<FeedBackTypeTable>();

					List<CompanyFeedBackTypeAssociation> associations = feedbackservice
							.getListOfFeedBackTypeAccoringToCompany(company);
					for (CompanyFeedBackTypeAssociation companyFeedBackTypeAssociation : associations) {
						feedbackType.add(companyFeedBackTypeAssociation.getFeedBackType());
					}

					UserModel clientCompanyAdmin = loginService.getclientCompanyAdminById(company.getCompanyId());

					System.out.println(clientCompanyAdmin.getUserName() + "  user name");

					feedBackCounterForHomes = feedcomWebServicesService
							.webServiceGetfeedBackCounterForHome(feedbackType, userFrom, clientCompanyAdmin);

					try {
						MapCompanyToFeedBackCounterForHomes.put(ow.writeValueAsString(clientCompanyAdmin.getCompany()),
								feedBackCounterForHomes);
					} catch (Exception e) {
						js.put("code", "1001");
						js.put("ErrorMessage", e.getMessage());
						json = js.toString();
						e.printStackTrace();
					}

				}

			} else {

				System.out.println("else part");

				feedbackType = new ArrayList<FeedBackTypeTable>();

				UserModel clientCompanyAdmin = loginService
						.getclientCompanyAdminById(userModel.getCompany().getCompanyId());
				UserModel userTo = loginService.getCoreflexAdmin();

				List<CompanyFeedBackTypeAssociation> associations = feedbackservice
						.getListOfFeedBackTypeAccoringToCompany(clientCompanyAdmin.getCompany());
				for (CompanyFeedBackTypeAssociation companyFeedBackTypeAssociation : associations) {
					feedbackType.add(companyFeedBackTypeAssociation.getFeedBackType());
				}

				feedBackCounterForHomes = feedcomWebServicesService.webServiceGetfeedBackCounterForHome(feedbackType,
						clientCompanyAdmin, userTo);
				try {
					MapCompanyToFeedBackCounterForHomes.put(ow.writeValueAsString(clientCompanyAdmin.getCompany()),
							feedBackCounterForHomes);
				} catch (Exception e) {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
					e.printStackTrace();
				}
			}

			try {

				try {
					String feedcomCommunicationCounter = ow.writeValueAsString(MapCompanyToFeedBackCounterForHomes);

					js.put("feedcomCommunicationCounter", feedcomCommunicationCounter);
					js.put("code", "1000");
					json = js.toString();
				} catch (Exception e) {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
					e.printStackTrace();
				}
				System.out.println(json);
			} catch (Exception e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}

		} else {
			try {
				js.put("code", "1001");
				json = js.toString();
			} catch (JSONException e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				e.printStackTrace();
			}
		}
		return json;
	}

	@RequestMapping(value = "/feedcom/getListOfFeedcomAndQueryComForCommunication", method = RequestMethod.POST)
	@ResponseBody
	public String getlistOfFeedcomForCommunication(HttpServletRequest request,
			@RequestParam("username") String username, @RequestParam("password") String password) throws JSONException {

		// nikhil rathod and ramesh hiiiiiiiiiiiiiiii

		UserModel userModel = loginService.getUserByUnamePassword(username, password);
		JSONObject js = new JSONObject();
		String json = null;

		// in this hashmap key is company id value is another hashmap

		// in this hashmap key is company id value is another hashmap
		HashMap<String, HashMap<String, HashMap<String, List<Feedback>>>> finalHashmapForListOfFeedBackList = new HashMap<String, HashMap<String, HashMap<String, List<Feedback>>>>();

		if (userModel != null) {

			List<FeedBackTypeTable> feedbackType = new ArrayList<FeedBackTypeTable>();
			String companyName = userModel.getCompany().getCompanyName();
			UserModel userFrom = null, userTo = null;
			if (companyName.equalsIgnoreCase("Coreflex")) {

				List<Company> companies = companyService.getListOfCompany();
				for (Company company : companies) {
					if (company.getCompanyName().equalsIgnoreCase("Coreflex")) {
						companies.remove(company);
						break;
					}
				}
				userFrom = loginService.getCoreflexAdmin();

				for (Company company : companies) {
					UserModel clientCompanyAdmin = loginService.getclientCompanyAdminById(company.getCompanyId());

					// start of fetching feedcom 50 records

					// in this hashmap key is sonumber value is another hashmap
					HashMap<String, HashMap<String, List<Feedback>>> hashMapFor50RecordsOfFeedback = new HashMap<String, HashMap<String, List<Feedback>>>();

					// in this hashmap key is feedbacktypetable id value is List
					// Of
					// FeedBack

					List<FeedBackSonumberSortingByDate> feedbackswithSonumber = feedcomWebServicesService
							.getUpdatedSortedFeedBackList(userFrom, clientCompanyAdmin);
					try {
						int index = 0;
						for (FeedBackSonumberSortingByDate feedBacksort : feedbackswithSonumber) {
							HashMap<String, List<Feedback>> innerHashMapForActualFeedBack = new HashMap<String, List<Feedback>>();
							if (index <= 50) {

								List<Feedback> feedbacks = feedcomWebServicesService
										.getListOfChatingUsingSoNumberAndFeedBackType(feedBacksort.getSoNumber(),
												feedBacksort.getFeedBackType(), userFrom, clientCompanyAdmin);

								// key is feedbacktype table id
								innerHashMapForActualFeedBack.put(feedBacksort.getFeedBackType().getId() + "",
										feedbacks);

								// key is sonumber
								hashMapFor50RecordsOfFeedback.put(feedBacksort.getSoNumber(),
										innerHashMapForActualFeedBack);

							}
							index++;
						}
						finalHashmapForListOfFeedBackList.put(clientCompanyAdmin.getCompany().getCompanyId() + "",
								hashMapFor50RecordsOfFeedback);

					} catch (Exception e) {
						js.put("code", "1001");
						js.put("ErrorMessage", e.getMessage());
						json = js.toString();
						e.printStackTrace();
					}
					// End of fetching feedcom 50 records
				}

			} else {
				userFrom = userModel;
				userTo = loginService.getCoreflexAdmin();

				UserModel clientCompanyAdmin = loginService
						.getclientCompanyAdminById(userFrom.getCompany().getCompanyId());

				// start of fetching feedcom 50 records

				// in this hashmap key is sonumber value is another hashmap
				HashMap<String, HashMap<String, List<Feedback>>> hashMapFor50RecordsOfFeedback = new HashMap<String, HashMap<String, List<Feedback>>>();

				// in this hashmap key is feedbacktypetable id value is List Of
				// FeedBack

				List<FeedBackSonumberSortingByDate> feedbackswithSonumber = feedcomWebServicesService
						.getUpdatedSortedFeedBackList(userTo, clientCompanyAdmin);

				try {
					int index = 0;
					for (FeedBackSonumberSortingByDate feedcomSort : feedbackswithSonumber) {
						HashMap<String, List<Feedback>> innerHashMapForActualFeedBack = new HashMap<String, List<Feedback>>();
						if (index <= 50) {

							List<Feedback> feedbacks = feedcomWebServicesService
									.getListOfChatingUsingSoNumberAndFeedBackType(feedcomSort.getSoNumber(),
											feedcomSort.getFeedBackType(), userTo, clientCompanyAdmin);

							// key is feedbacktype table id
							innerHashMapForActualFeedBack.put(feedcomSort.getFeedBackType().getId() + "", feedbacks);

							// key is sonumber
							hashMapFor50RecordsOfFeedback.put(feedcomSort.getSoNumber(), innerHashMapForActualFeedBack);

						}
						index++;
					}
					finalHashmapForListOfFeedBackList.put(clientCompanyAdmin.getCompany().getCompanyId() + "",
							hashMapFor50RecordsOfFeedback);

				} catch (Exception e) {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
					e.printStackTrace();
				}
				// End of fetching feedcom 50 records

				// start of fetching querycom 50 records

				// in this hashmap key is feedbacktypetable id value is List Of
			}

			try {

				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				try {
					String ListOfFeedBack = ow.writeValueAsString(finalHashmapForListOfFeedBackList);
					js.put("ListOfFeedBack", ListOfFeedBack);
					js.put("code", "1000");
					json = js.toString();
				} catch (Exception e) {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
					e.printStackTrace();
				}

			} catch (Exception e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}

		} else {
			try {
				js.put("code", "1001");
				json = js.toString();
			} catch (JSONException e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				e.printStackTrace();
			}
		}
		return json;
	}

	@RequestMapping(value = "/feedcom/getListOfFeedcomForOldDataCommunication", method = RequestMethod.POST)
	@ResponseBody
	public String getListOfFeedcomForOldDataCommunication(HttpServletRequest request,
			@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("sonumber") String soNumber, @RequestParam("feedbacktype") String feedbackTypeTable,
			@RequestParam("userFrom") String fromUser, @RequestParam("userTo") String toUser) throws JSONException {

		UserModel userModel = loginService.getUserByUnamePassword(username, password);
		JSONObject js = new JSONObject();
		String json = null;

		// in this hashmap key is company id value is another hashmap
		HashMap<String, HashMap<String, HashMap<String, List<Feedback>>>> finalHashmapForListOfFeedBackList = new HashMap<String, HashMap<String, HashMap<String, List<Feedback>>>>();

		if (userModel != null) {

			ArrayList<String> soNumberList = null;

			soNumberList = new ArrayList(Arrays.asList(soNumber.split("\\|")));

			System.out.println("ArrayList: " + soNumberList);

			FeedBackTypeTable typeTable = new FeedBackTypeTable();
			typeTable.setId(Integer.parseInt(feedbackTypeTable));
			UserModel userFrom = userService.getUserById(Integer.parseInt(fromUser)),
					userTo = userService.getUserById(Integer.parseInt(toUser));
			UserModel userCoreflexAdmin = null;

			List<FeedBackTypeTable> feedbackType = new ArrayList<FeedBackTypeTable>();
			String companyName = userModel.getCompany().getCompanyName();

			if (companyName.equalsIgnoreCase("Coreflex")) {

				UserModel clientCompanyAdmin = null;
				if (!userFrom.getCompany().getCompanyName().equalsIgnoreCase("Coreflex")) {
					clientCompanyAdmin = loginService.getclientCompanyAdminById(userFrom.getCompany().getCompanyId());
					userCoreflexAdmin = loginService.getCoreflexAdmin();
				}
				if (!userTo.getCompany().getCompanyName().equalsIgnoreCase("Coreflex")) {
					clientCompanyAdmin = loginService.getclientCompanyAdminById(userTo.getCompany().getCompanyId());
					userCoreflexAdmin = loginService.getCoreflexAdmin();
				}

				// start of fetching feedcom 50 records
				// in this hashmap key is sonumber value is another hashmap
				HashMap<String, HashMap<String, List<Feedback>>> hashMapFor50RecordsOfFeedback = new HashMap<String, HashMap<String, List<Feedback>>>();

				List<FeedBackSonumberSortingByDate> feedbackswithSonumber = feedcomWebServicesService
						.getUpdatedSortedFeedBackListWithFeedbackType(userCoreflexAdmin, clientCompanyAdmin, typeTable);
				List<FeedBackSonumberSortingByDate> feedbackswithSonumberActual = feedbackswithSonumber;
				try {
					for (String soNum : soNumberList) {

						try {
							for (FeedBackSonumberSortingByDate feedBacksort : feedbackswithSonumber) {
								if (feedBacksort.getSoNumber().equals(soNum)) {
									feedbackswithSonumberActual.remove(feedBacksort);
								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}

				try {
					int index = 0;
					for (FeedBackSonumberSortingByDate feedBacksort : feedbackswithSonumberActual) {
						// in this hashmap key is feedbacktypetable id value is
						// List
						// Of FeedBack
						HashMap<String, List<Feedback>> innerHashMapForActualFeedBack = new HashMap<String, List<Feedback>>();

						if (index <= 50) {

							List<Feedback> feedbacks = feedcomWebServicesService
									.getListOfChatingUsingSoNumberAndFeedBackType(feedBacksort.getSoNumber(),
											feedBacksort.getFeedBackType(), userCoreflexAdmin, clientCompanyAdmin);
							// key is feedbacktype table id
							innerHashMapForActualFeedBack.put(feedBacksort.getFeedBackType().getId() + "", feedbacks);
							// key is sonumber
							hashMapFor50RecordsOfFeedback.put(feedBacksort.getSoNumber(),
									innerHashMapForActualFeedBack);
						}
						index++;
					}
					finalHashmapForListOfFeedBackList.put(clientCompanyAdmin.getCompany().getCompanyId() + "",
							hashMapFor50RecordsOfFeedback);

				} catch (Exception e) {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
					e.printStackTrace();
				}
				// End of fetching feedcom 50 records

			} else {
				userFrom = userModel;
				userTo = loginService.getCoreflexAdmin();

				UserModel clientCompanyAdmin = loginService
						.getclientCompanyAdminById(userFrom.getCompany().getCompanyId());

				// start of fetching feedcom 50 records

				// in this hashmap key is sonumber value is another hashmap
				HashMap<String, HashMap<String, List<Feedback>>> hashMapFor50RecordsOfFeedback = new HashMap<String, HashMap<String, List<Feedback>>>();

				List<FeedBackSonumberSortingByDate> feedbackswithSonumber = feedcomWebServicesService
						.getUpdatedSortedFeedBackListWithFeedbackType(userTo, clientCompanyAdmin, typeTable);

				List<FeedBackSonumberSortingByDate> feedbackswithSonumberActual = feedbackswithSonumber;

				System.out.println(feedbackswithSonumberActual.size() + "   feedbackswithSonumberActual.size()");
				try {
					for (String soNum : soNumberList) {

						try {
							for (FeedBackSonumberSortingByDate feedBacksort : feedbackswithSonumber) {
								if (feedBacksort.getSoNumber().equals(soNum)) {
									feedbackswithSonumberActual.remove(feedBacksort);

								}
							}
						} catch (Exception e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println(feedbackswithSonumberActual.size() + "   feedbackswithSonumberActual.size()");

				try {
					int index = 0;
					for (FeedBackSonumberSortingByDate feed : feedbackswithSonumberActual) {
						// in this hashmap key is feedbacktypetable id value is
						// List Of
						// FeedBack
						HashMap<String, List<Feedback>> innerHashMapForActualFeedBack = new HashMap<String, List<Feedback>>();
						if (index <= 50) {

							List<Feedback> feedbacks = feedcomWebServicesService
									.getListOfChatingUsingSoNumberAndFeedBackType(feed.getSoNumber(),
											feed.getFeedBackType(), userTo, clientCompanyAdmin);

							// key is feedbacktype table id
							innerHashMapForActualFeedBack.put(feed.getFeedBackType().getId() + "", feedbacks);

							// key is sonumber
							hashMapFor50RecordsOfFeedback.put(feed.getSoNumber(), innerHashMapForActualFeedBack);

						}
						index++;
					}
					finalHashmapForListOfFeedBackList.put(clientCompanyAdmin.getCompany().getCompanyId() + "",
							hashMapFor50RecordsOfFeedback);

				} catch (Exception e) {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
					e.printStackTrace();
				}
				// End of fetching feedcom 50 records
			}

			try {

				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				try {
					String ListOfFeedBack = ow.writeValueAsString(finalHashmapForListOfFeedBackList);
					js.put("ListOfFeedBack", ListOfFeedBack);
					js.put("code", "1000");
					json = js.toString();
				} catch (Exception e) {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
					e.printStackTrace();
				}

			} catch (Exception e) {

				e.printStackTrace();
			}

		} else {
			try {
				js.put("code", "1001");
				json = js.toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
		return json;
	}

	@RequestMapping(value = "/feedcom/updatedMobileFeedcomCommunication", method = RequestMethod.POST)
	@ResponseBody
	public String NewMobileFeedcomCommunication(final HttpServletRequest request,
			@RequestParam("username") String username, @RequestParam("password") String password,
			@RequestParam("flag") String flag, @RequestParam("feedcomDict") String dict) {

		JSONObject obj;
		JSONObject js = new JSONObject();
		String json = null;
		try {
			obj = new JSONObject(dict);

			final JSONObject feedcomObj = obj;
			final String feedbackTextJson = feedcomObj.getString("feedText");
			String feedbackTypeJson = feedcomObj.getString("feedbackType");
			String soNumberJson = feedcomObj.getString("soNumber");
			String userFeedbackJson = feedcomObj.getString("userFeedback");
			String userFromJson = feedcomObj.getString("userFrom");
			String userToJson = feedcomObj.getString("userTo");
			final String attachmentJson = feedcomObj.getString("attachment");
			final String subjectJson = feedcomObj.getString("emailSubject");
			String operatorJson = feedcomObj.getString("operatorId");

			String statusJson = "1";
			final UserModel userFrom = userService.getUserById(Integer.parseInt(userFromJson));
			final UserModel userTo = userService.getUserById(Integer.parseInt(userToJson));
			final UserModel userFeedback = userService.getUserById(Integer.parseInt(userFeedbackJson));

			String EmailStatus = "";

			try {
				feedbackservice.saveChattingFeedComForWebService(soNumberJson, statusJson, subjectJson, attachmentJson,
						feedbackTextJson, feedbackTypeJson, operatorJson, userFrom, userTo, userFeedback);
				EmailStatus = "Success";
			} catch (Exception e) {

			}
			List<Feedback> feedbacks = null;
			try {
				feedbacks = feedbackservice.getListOfChatingUsingSoNumberAndFeedBackTypeForWebservice(soNumberJson,
						feedbackTypeJson, userFrom, userTo);

			} catch (Exception e) {

			}

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String ListOffeedbacks;

			ListOffeedbacks = ow.writeValueAsString(feedbacks);
			try {
				js.put("ListOffeedbacks", ListOffeedbacks);
				js.put("code", "1000");

				json = js.toString();
				System.out.println(json);
			} catch (Exception e) {
				e.printStackTrace();
			}

			try {
				if (EmailStatus.equalsIgnoreCase("Success")) {
					new Thread() {
						public void run() {

							ReplyToEmailFeedComWebService replyToEmail = new ReplyToEmailFeedComWebService();

							/*
							 * UserModel userModel = (UserModel)
							 * httpSession.getAttribute("UserFrom"); UserModel
							 * userTo = (UserModel)
							 * httpSession.getAttribute("UserTo");
							 */
							// attachments

							if (userFrom.getCompany().getCompanyId() == 1) {

								List<String> cfsemailList = new ArrayList<String>();
								List<String> clientemailList = new ArrayList<String>();

								List<EmailSendReceivePermission> emailSendReceivePermissions = emailSendAndReceivePermissionService
										.getEmailPermissionUserListAccordingToCompany(
												userFrom.getCompany().getCompanyId() + "");
								List<EmailSendReceivePermission> emailSendReceivePermissionsTo = emailSendAndReceivePermissionService
										.getEmailPermissionUserListAccordingToCompany(
												userTo.getCompany().getCompanyId() + "");

								for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissions) {
									cfsemailList.add(emailSendReceivePermission.getUserModel().getEmail());
								}

								for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissionsTo) {
									clientemailList.add(emailSendReceivePermission.getUserModel().getEmail());
								}

								System.out.println("List Of emailers " + Arrays.toString(cfsemailList.toArray())
										+ "      second     lst   " + Arrays.toString(clientemailList.toArray()));
								System.out.println("emailSendAndReceivePermissionService   cfsemailList   "
										+ cfsemailList.size() + "  clientemailList    " + clientemailList.size());

								String userFirstAndLastName = "            Sender :  " + userFeedback.getFirstName()
										+ " " + userFeedback.getLastName();

								replyToEmail.sendEmailReply(subjectJson, clientemailList, cfsemailList, attachmentJson,
										feedbackTextJson + userFirstAndLastName, emailSendAndReceivePermissionService);

							} else {
								List<String> cfsemailList = new ArrayList<String>();
								List<String> clientemailList = new ArrayList<String>();

								List<EmailSendReceivePermission> emailSendReceivePermissions = emailSendAndReceivePermissionService
										.getEmailPermissionUserListAccordingToCompany(
												userFrom.getCompany().getCompanyId() + "");

								List<EmailSendReceivePermission> emailSendReceivePermissionsTo = emailSendAndReceivePermissionService
										.getEmailPermissionUserListAccordingToCompany(
												userTo.getCompany().getCompanyId() + "");

								for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissions) {
									clientemailList.add(emailSendReceivePermission.getUserModel().getEmail());
								}

								for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissionsTo) {
									cfsemailList.add(emailSendReceivePermission.getUserModel().getEmail());
								}

								String userFirstAndLastName = "            Sender :  " + userFeedback.getFirstName()
										+ " " + userFeedback.getLastName();

								replyToEmail.sendEmailReply(subjectJson, cfsemailList, clientemailList, attachmentJson,
										feedbackTextJson + userFirstAndLastName, emailSendAndReceivePermissionService);
							}
						}
					}.start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;
	}

	@RequestMapping(value = "/feedcom/newMobileFeedcomCommunication", method = RequestMethod.POST)
	@ResponseBody
	public String newMobileFeedcomCommunication(HttpServletRequest request, @RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("feedcomDict") String dict,
			@RequestParam("flag") String flag) {

		JSONObject obj;
		JSONObject js = new JSONObject();
		String json = null;

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			obj = new JSONObject(dict);
			final JSONObject feedbackObj = obj;
			final String attachment = feedbackObj.getString("attachment");
			String dateOfFeed = feedbackObj.getString("dateOfFeed");
			final String feedText = feedbackObj.getString("feedText");
			String feedfeedbackTypeJson = feedbackObj.getString("feedbackType");
			String operatorId = feedbackObj.getString("operatorId");
			final String soNumber = feedbackObj.getString("soNumber");
			String statusId = feedbackObj.getString("statusId");
			final String subject = feedbackObj.getString("subject");
			String userFeedbackJson = feedbackObj.getString("userFeedback");
			String userFromJson = feedbackObj.getString("userFrom");
			String userToJson = feedbackObj.getString("userTo");
			/*
			 * String feedbackCounter =
			 * feedbackObj.getString("feedbackCounter"); String feedbackId =
			 * feedbackObj.getString("feedbackId");
			 */

			System.out.println(userFromJson);
			System.out.println(userToJson);

			final UserModel userFrom = userService.getUserById(Integer.parseInt(userFromJson));
			final UserModel userTo = userService.getUserById(Integer.parseInt(userToJson));
			final UserModel userFeedback = userService.getUserById(Integer.parseInt(userFeedbackJson));

			String EmailStatus = "";
			String validate = feedbackservice.validateNewFeedComChatingForWebService(soNumber, feedfeedbackTypeJson,
					userFrom, userTo);

			if ("NotAvailable".equalsIgnoreCase(validate)) {

				System.out.println("NotAvailable");
				String status = feedbackservice.saveNewFeedComChatForWebService(soNumber, feedfeedbackTypeJson,
						feedText, operatorId, attachment, subject, userFrom, userTo, userFeedback);
				String feedbackCounterId = "";
				String feedbackCount = "";
				try {
					String[] feedbackIdAndCount = status.split("/");
					feedbackCounterId = feedbackIdAndCount[0];
					feedbackCount = feedbackIdAndCount[1];
					try {
						js.put("feedbackCounterId", feedbackCounterId);
						js.put("feedbackCount", feedbackCount);
						js.put("code", "1000");
						EmailStatus = "Success";
						json = js.toString();
						System.out.println(json);
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					js.put("code", "1001");
					json = js.toString();
					e.printStackTrace();
				}

			} else {
				js.put("Status", "AlreadyAvailable");
				js.put("code", "1000");
				json = js.toString();
				System.out.println(json);
			}

			try {
				if (EmailStatus.equalsIgnoreCase("Success")) {
					new Thread() {
						public void run() {

							String companyName = userFrom.getCompany().getCompanyName();
							List<String> clientCompanyemail = new ArrayList<String>();
							List<String> corflexemailIdList = new ArrayList<String>();

							if (companyName.equalsIgnoreCase("Coreflex")) {

								List<EmailSendReceivePermission> emailSendReceivePermissions = emailSendAndReceivePermissionService
										.getEmailPermissionUserListAccordingToCompany(
												userFrom.getCompany().getCompanyId() + "");
								List<EmailSendReceivePermission> emailSendReceivePermissionsTo = emailSendAndReceivePermissionService
										.getEmailPermissionUserListAccordingToCompany(
												userTo.getCompany().getCompanyId() + "");

								for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissions) {
									corflexemailIdList.add(emailSendReceivePermission.getUserModel().getEmail());
								}

								for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissionsTo) {
									clientCompanyemail.add(emailSendReceivePermission.getUserModel().getEmail());
								}

							} else {
								List<EmailSendReceivePermission> emailSendReceivePermissions = emailSendAndReceivePermissionService
										.getEmailPermissionUserListAccordingToCompany(
												userFrom.getCompany().getCompanyId() + "");
								List<EmailSendReceivePermission> emailSendReceivePermissionsTo = emailSendAndReceivePermissionService
										.getEmailPermissionUserListAccordingToCompany(
												userTo.getCompany().getCompanyId() + "");

								for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissionsTo) {
									corflexemailIdList.add(emailSendReceivePermission.getUserModel().getEmail());
								}

								for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissions) {
									clientCompanyemail.add(emailSendReceivePermission.getUserModel().getEmail());
								}
							}
							// String email="rameshsable15@gmail.com";
							String corflexemailIdListArray[] = corflexemailIdList
									.toArray(new String[corflexemailIdList.size()]);
							String[] clientCompanyemailArray = clientCompanyemail
									.toArray(new String[clientCompanyemail.size()]);

							String soNumberArray[] = soNumber.split("#@");
							String tempsonumber = soNumberArray[0];
							String avayNum = soNumberArray[1];
							String docNum = soNumberArray[2];

							String headerString = "SoNumber :-  " + tempsonumber + " Avaya Number :- " + avayNum
									+ "  Document Id :- " + docNum;

							String userFirstAndLastName = "            Sender :  " + userFeedback.getFirstName() + " "
									+ userFeedback.getLastName();

							String s = EmailUtilForWebService.sendEmail(attachment, clientCompanyemailArray,
									corflexemailIdListArray, subject,
									"<h3>" + headerString + "  </h3><br>" + feedText + userFirstAndLastName,
									emailSendAndReceivePermissionService);

						}
					}.start();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;

	}

	@RequestMapping(value = "/feedcom/getListOf50Reports", method = RequestMethod.POST)
	@ResponseBody
	public String getListOf50Reports(HttpServletRequest request, @RequestParam("username") String username,
			@RequestParam("password") String password) {

		// nikhil rathod and ramesh hiiiiiiiiiiiiiiii

		UserModel userModel = loginService.getUserByUnamePassword(username, password);
		JSONObject js = new JSONObject();
		String json = null;
		HashMap<String, List<Report>> hashMapFor50RecordsOfReport = new HashMap<String, List<Report>>();
		if (userModel != null) {

			String companyName = userModel.getCompany().getCompanyName();
			UserModel userFrom = null, userTo = null;
			if (companyName.equalsIgnoreCase("Coreflex")) {

				userFrom = loginService.getCoreflexAdmin();

				List<Company> companies = companyService.getListOfCompany();
				for (Company company : companies) {
					if (company.getCompanyName().equalsIgnoreCase("Coreflex")) {
						companies.remove(company);
						break;
					}
				}
				userFrom = loginService.getCoreflexAdmin();

				for (Company company : companies) {
					UserModel clientCompanyAdmin = loginService.getclientCompanyAdminById(company.getCompanyId());
					List<Report> reports = dropBoxService.getListOfupdated50Reports(userFrom, clientCompanyAdmin);

					hashMapFor50RecordsOfReport.put(clientCompanyAdmin.getCompany().getCompanyId() + "", reports);
				}
				// End of fetching querycom 50 records
			} else {
				userFrom = userModel;
				userTo = loginService.getCoreflexAdmin();

				UserModel clientCompanyAdmin = loginService
						.getclientCompanyAdminById(userFrom.getCompany().getCompanyId());

				List<Report> reports = dropBoxService.getListOfupdated50Reports(userTo, clientCompanyAdmin);

				hashMapFor50RecordsOfReport.put(clientCompanyAdmin.getCompany().getCompanyId() + "", reports);

			}

			try {

				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				try {
					String ListOfReports = ow.writeValueAsString(hashMapFor50RecordsOfReport);
					js.put("ListOfReports", ListOfReports);
					js.put("code", "1000");
					json = js.toString();
				} catch (Exception e) {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
					e.printStackTrace();
				}

			} catch (Exception e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}

		else {
			try {
				js.put("code", "1001");
				json = js.toString();
			} catch (JSONException e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				e.printStackTrace();
			}
		}
		return json;
	}

	@RequestMapping(value = "/feedcom/getListOf50Documents", method = RequestMethod.POST)
	@ResponseBody
	public String getListOf50Documents(HttpServletRequest request, @RequestParam("username") String username,
			@RequestParam("password") String password) {

		// nikhil rathod and ramesh hiiiiiiiiiiiiiiii

		UserModel userModel = loginService.getUserByUnamePassword(username, password);
		JSONObject js = new JSONObject();
		String json = null;
		HashMap<String, List<Documents>> hashMapFor50RecordsOfDocuments = new HashMap<String, List<Documents>>();
		if (userModel != null) {

			String companyName = userModel.getCompany().getCompanyName();
			UserModel userFrom = null, userTo = null;
			if (companyName.equalsIgnoreCase("Coreflex")) {

				userFrom = loginService.getCoreflexAdmin();

				List<Company> companies = companyService.getListOfCompany();
				for (Company company : companies) {
					if (company.getCompanyName().equalsIgnoreCase("Coreflex")) {
						companies.remove(company);
						break;
					}
				}
				userFrom = loginService.getCoreflexAdmin();

				for (Company company : companies) {
					UserModel clientCompanyAdmin = loginService.getclientCompanyAdminById(company.getCompanyId());
					List<Documents> documents = dropBoxService.getListOfupdated50Documents(userFrom,
							clientCompanyAdmin);

					hashMapFor50RecordsOfDocuments.put(clientCompanyAdmin.getCompany().getCompanyId() + "", documents);
				}
				// End of fetching querycom 50 records
			} else {
				userFrom = userModel;
				userTo = loginService.getCoreflexAdmin();

				UserModel clientCompanyAdmin = loginService
						.getclientCompanyAdminById(userFrom.getCompany().getCompanyId());

				List<Documents> documents = dropBoxService.getListOfupdated50Documents(userTo, clientCompanyAdmin);

				hashMapFor50RecordsOfDocuments.put(clientCompanyAdmin.getCompany().getCompanyId() + "", documents);

			}

			try {

				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				try {
					String ListOfDocuments = ow.writeValueAsString(hashMapFor50RecordsOfDocuments);
					js.put("ListOfDocuments", ListOfDocuments);
					js.put("code", "1000");
					json = js.toString();
				} catch (Exception e) {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
					e.printStackTrace();
				}

			} catch (Exception e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}

		else {
			try {
				js.put("code", "1001");
				json = js.toString();
			} catch (JSONException e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				e.printStackTrace();
			}
		}
		return json;
	}

	@RequestMapping(value = "/feedcom/getListOf50MOM", method = RequestMethod.POST)
	@ResponseBody
	public String getListOf50MoM(HttpServletRequest request, @RequestParam("username") String username,
			@RequestParam("password") String password) {

		// nikhil rathod and ramesh hiiiiiiiiiiiiiiii

		UserModel userModel = loginService.getUserByUnamePassword(username, password);
		JSONObject js = new JSONObject();
		String json = null;
		HashMap<String, List<Mom>> hashMapFor50RecordsOfMom = new HashMap<String, List<Mom>>();
		if (userModel != null) {

			String companyName = userModel.getCompany().getCompanyName();
			UserModel userFrom = null, userTo = null;
			if (companyName.equalsIgnoreCase("Coreflex")) {

				userFrom = loginService.getCoreflexAdmin();

				List<Company> companies = companyService.getListOfCompany();
				for (Company company : companies) {
					if (company.getCompanyName().equalsIgnoreCase("Coreflex")) {
						companies.remove(company);
						break;
					}
				}
				userFrom = loginService.getCoreflexAdmin();

				for (Company company : companies) {
					UserModel clientCompanyAdmin = loginService.getclientCompanyAdminById(company.getCompanyId());
					List<Mom> moms = momService.getListOfupdated50Mom(userFrom, clientCompanyAdmin);

					hashMapFor50RecordsOfMom.put(clientCompanyAdmin.getCompany().getCompanyId() + "", moms);
				}
				// End of fetching querycom 50 records
			} else {
				userFrom = userModel;
				userTo = loginService.getCoreflexAdmin();

				UserModel clientCompanyAdmin = loginService
						.getclientCompanyAdminById(userFrom.getCompany().getCompanyId());

				List<Mom> moms = momService.getListOfupdated50Mom(userTo, clientCompanyAdmin);

				hashMapFor50RecordsOfMom.put(clientCompanyAdmin.getCompany().getCompanyId() + "", moms);

			}

			try {

				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				try {
					String ListOfMOM = ow.writeValueAsString(hashMapFor50RecordsOfMom);
					js.put("ListOfMOM", ListOfMOM);
					js.put("code", "1000");
					json = js.toString();
				} catch (Exception e) {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
					e.printStackTrace();
				}

			} catch (Exception e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}

		else {
			try {
				js.put("code", "1001");
				json = js.toString();
			} catch (JSONException e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}

				e.printStackTrace();
			}
		}
		return json;
	}

	/*
	 * @RequestMapping(value = "/feedcom/uploadFileFromMobile", method =
	 * RequestMethod.POST, produces="application/json")
	 * 
	 * @ResponseBody public String uploadFileFromMobile(
	 * MultipartHttpServletRequest request ) throws Exception {
	 * 
	 * JSONObject js = new JSONObject(); String json = null;
	 * 
	 * String rootPath1 = System.getProperty("catalina.home"); File dir1 = new
	 * File(rootPath1 + File.separator + "CfsFiles"); if (!dir1.exists())
	 * dir1.mkdirs();
	 * 
	 * System.out.println("nikhi l  rathod"); String str = "";
	 * 
	 * try {
	 * 
	 * String fileName = new Date().getTime() +""; System.out.println(
	 * "File Name:" + fileName); byte[] bytes = "nikhil".getBytes(); File
	 * directory = new File(dir1.getAbsolutePath()); directory.mkdirs(); str =
	 * str + fileName;
	 * 
	 * File file = new File(dir1.getAbsolutePath() + File.separator + fileName);
	 * BufferedOutputStream stream = new BufferedOutputStream(new
	 * FileOutputStream(file)); stream.write(bytes); stream.close();
	 * ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
	 * try { String FileResponce = ow.writeValueAsString(str);
	 * js.put("fileName", FileResponce); js.put("code", "1000"); json =
	 * js.toString(); } catch (Exception e) { js.put("code", "1001");
	 * js.put("ErrorMessage", e.getMessage()); json = js.toString();
	 * e.printStackTrace(); } } catch (Exception e) { // TODO Auto-generated
	 * catch block e.printStackTrace(); throw new Exception(
	 * "Error while loading the file"); }
	 * 
	 * return json; }
	 */

	@RequestMapping(value = "/feedcom/uploadFileFromMobile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String uploadFile(final HttpServletRequest request) {

		Assert.state(request instanceof MultipartHttpServletRequest, "request !instanceof MultipartHttpServletRequest");
		final MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

		final Map files = multiRequest.getFileMap();
		JSONObject js = new JSONObject();
		String json = null;
		Iterator<String> itrator = multiRequest.getFileNames();
		String str = "";
		while (itrator.hasNext()) {

			System.out.println("count");
			MultipartFile multiFile = multiRequest.getFile(itrator.next());
			System.out.println("multiFile  " + multiFile.getName());
			String fileName = new Date().getTime() + multiFile.getOriginalFilename();

			System.out.println(fileName);

			try {

				// just to show that we have actually received the file

				byte[] bytes = multiFile.getBytes();
				str = str + fileName + "#@$";

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

					// APPROACH #2: uploads second file using an OutputStream
					String secondRemoteFile = "/TEST/" + fileName;
					InputStream inputStream = null;
					try {
						inputStream = new ByteArrayInputStream(bytes);
					} catch (Exception e) {
						e.printStackTrace();
					}

					OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
					byte[] bytesIn = new byte[4096];
					int read = 0;

					while ((read = inputStream.read(bytesIn)) != -1) {
						outputStream.write(bytesIn, 0, read);
					}
					inputStream.close();
					outputStream.close();

					boolean completed = ftpClient.completePendingCommand();
					if (completed) {
						System.out.println("The file is uploaded successfully.");
					}

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

				ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
				try {
					String FileResponce = ow.writeValueAsString(str);
					js.put("fileName", FileResponce);
					js.put("code", "1000");
					json = js.toString();
				} catch (Exception e) {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
					e.printStackTrace();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				try {
					throw new Exception("Error while loading the file");
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}

		}

		/*
		 * Assert.state(files.size() > 0, "0 files exist"); Set<MultipartFile
		 * >fileset=files.keySet();
		 */
		/*
		 * process files
		 */
		/*
		 * System.out.println(fileset.size());
		 */
		/*
		 * for (MultipartFile file : fileset) { file.transferTo(new File(new
		 * File("/Users/dhruba/tmp"), file.getOriginalFilename()));
		 * System.out.println(file.getOriginalFilename()); }
		 */

		return json;

	}

	@RequestMapping(value = "/feedcom/newMobileMOM", method = RequestMethod.POST)
	@ResponseBody
	public String newMobileMOM(final HttpServletRequest request, @RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("MomDict") String dict) {

		JSONObject obj;
		JSONObject js = new JSONObject();
		String json = null;

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {

			System.out.println(dict);

			obj = new JSONObject(dict);
			final JSONObject feedbackObj = obj;
			String createdDateJson = feedbackObj.getString("createdDate");
			String attendee = feedbackObj.getString("attendee");
			String keyPoints = feedbackObj.getString("keyPoints");
			String submittedDateTimeJson = feedbackObj.getString("submittedDateTime");
			String subject = feedbackObj.getString("subject");
			final String userFeedbackJson = feedbackObj.getString("userFeedback");
			final String userFromJson = feedbackObj.getString("userFrom");
			final String userToJson = feedbackObj.getString("userTo");

			String userId = feedbackObj.getString("userIds");

			String[] parts = userId.split(",");

			List<String> emailList = new ArrayList<String>();
			UserModel model = null;
			for (String string : parts) {

				model = userService.getUserById(Integer.parseInt(string));
				emailList.add(model.getEmail());

			}

			UserModel userFrom = userService.getUserById(Integer.parseInt(userFromJson));
			UserModel userTo = userService.getUserById(Integer.parseInt(userToJson));
			UserModel userFeedback = userService.getUserById(Integer.parseInt(userFeedbackJson));

			// Sending Email Code start

			String creator = userFrom.getFirstName() + " " + userFrom.getLastName();

			ReplyToEmailMom replyToMom = new ReplyToEmailMom();

			replyToMom.sendEmailReply(subject, emailList, attendee, keyPoints, createdDateJson, submittedDateTimeJson,
					creator, request, emailSendAndReceivePermissionService);

			String date = submittedDateTimeJson;
			DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			Date submittedDateTime = null;
			try {
				submittedDateTime = dateFormat.parse(date);
			} catch (ParseException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}

			// createdDate
			dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			Date createdDate = null;
			try {
				createdDate = dateFormat.parse(createdDateJson);
			} catch (ParseException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

			System.out.println(userFromJson);
			System.out.println(userToJson);

			Mom mom = new Mom();
			int momid = 0;
			try {
				mom.setAttendee(attendee);
				mom.setDate(createdDate);
				mom.setDateTime(submittedDateTime);
				mom.setUserModelFrom(userFrom);
				mom.setUserModelTo(userTo);
				mom.setUserfeedback(userFeedback);
				mom.setKeypoints(keyPoints);

				mom.setSubject(subject);

				momid = momService.saveMom(mom);
			} catch (Exception e) {
				e.printStackTrace();
			}

			final Mom mom2 = mom;
			new Thread() {
				public void run() {
					HttpSession httpSession = request.getSession();
					UserModel userModelFrom = userService.getUserById(Integer.parseInt(userFromJson));
					UserModel userModelTo = userService.getUserById(Integer.parseInt(userToJson));
					UserModel userModelmsg = userService.getUserById(Integer.parseInt(userFeedbackJson));
					java.util.List<UserModel> userModels = loginService
							.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userModelFrom.getCompany());
					userModels.addAll(
							loginService.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userModelTo.getCompany()));
					userModels.remove(userModelmsg);
					FcmNotification fcmNotification = notificationService.getFcmNotification();
					notificationService.sendMomFcmNotification(mom2, fcmNotification, userModels, request);
				}
			}.start();

			String MomId = momid + "";
			String feedbackCount = "";
			try {

				try {
					js.put("MomId", MomId);
					js.put("code", "1000");
					json = js.toString();
					System.out.println(json);
				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {
				js.put("code", "1001");
				json = js.toString();
				e.printStackTrace();
			}

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;

	}

	@RequestMapping(value = "/feedcom/saveNewReportAndDocument", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, method = RequestMethod.POST, produces = "application/json")
	@ResponseBody
	public String newReport(final HttpServletRequest request, @RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("feedcomDict") String dict,
			@RequestParam("flag") String flag) {

		JSONObject obj;
		JSONObject js = new JSONObject();
		String json = null;

		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			obj = new JSONObject(dict);
			final JSONObject feedbackObj = obj;

			final String userFeedbackJson = feedbackObj.getString("userFeedback");
			final String userFromJson = feedbackObj.getString("userFrom");
			final String userToJson = feedbackObj.getString("userTo");

			final UserModel userFrom = userService.getUserById(Integer.parseInt(userFromJson));
			final UserModel userTo = userService.getUserById(Integer.parseInt(userToJson));
			final UserModel userFeedback = userService.getUserById(Integer.parseInt(userFeedbackJson));

			Assert.state(request instanceof MultipartHttpServletRequest,
					"request !instanceof MultipartHttpServletRequest");
			final MultipartHttpServletRequest multiRequest = (MultipartHttpServletRequest) request;

			final Map files = multiRequest.getFileMap();
			Iterator<String> itrator = multiRequest.getFileNames();
			while (itrator.hasNext()) {

				System.out.println("count");
				MultipartFile multiFile = multiRequest.getFile(itrator.next());
				System.out.println("multiFile  " + multiFile.getName());
				String fileName = new Date().getTime() + multiFile.getOriginalFilename();

				Report report = null;
				Documents documents = null;

				if (flag.equalsIgnoreCase("1")) {
					report = new Report();
					report.setName(fileName);
					report.setDate(new Date());
					report.setUserModelFrom(userFrom);
					report.setUserModelTo(userTo);
					report.setUserfeedback(userFeedback);
					report.setDateTime(new Date());
					// box.setDescription(desc);
					dropboxservice.saveFIleDetails(report);

					final Report report2 = report;

					new Thread() {
						public void run() {
							UserModel userModelFrom = userService.getUserById(Integer.parseInt(userFromJson));
							UserModel userModelTo = userService.getUserById(Integer.parseInt(userToJson));
							UserModel userModelmsg = userService.getUserById(Integer.parseInt(userFeedbackJson));
							java.util.List<UserModel> userModels = loginService
									.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userModelFrom.getCompany());
							userModels.addAll(loginService
									.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userModelTo.getCompany()));
							userModels.remove(userModelmsg);
							FcmNotification fcmNotification = notificationService.getFcmNotification();
							notificationService.sendReportFcmNotification(report2, fcmNotification, userModels,
									request);

						}
					}.start();

				}
				if (flag.equalsIgnoreCase("2")) {
					documents = new Documents();
					documents.setName(fileName);
					documents.setDate(new Date());
					documents.setUserModelFrom(userFrom);
					documents.setUserModelTo(userTo);
					documents.setUserfeedback(userFeedback);
					documents.setDateTime(new Date());
					dropboxservice.saveDocumentFIleDetails(documents);

					final Documents documents2 = documents;

					new Thread() {
						public void run() {
							UserModel userModelFrom = userService.getUserById(Integer.parseInt(userFromJson));
							UserModel userModelTo = userService.getUserById(Integer.parseInt(userToJson));
							UserModel userModelmsg = userService.getUserById(Integer.parseInt(userFeedbackJson));
							java.util.List<UserModel> userModels = loginService
									.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userModelFrom.getCompany());
							userModels.addAll(loginService
									.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userModelTo.getCompany()));
							userModels.remove(userModelmsg);
							FcmNotification fcmNotification = notificationService.getFcmNotification();
							notificationService.sendDocumentFcmNotification(documents2, fcmNotification, userModels,
									request);

						}
					}.start();

				}

				System.out.println(fileName);

				try {

					// just to show that we have actually received the file

					byte[] bytes = multiFile.getBytes();

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

						// APPROACH #2: uploads second file using an
						// OutputStream
						String secondRemoteFile = "/TEST/" + fileName;
						InputStream inputStream = null;
						try {
							inputStream = new ByteArrayInputStream(bytes);
						} catch (Exception e) {
							e.printStackTrace();
						}

						OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
						byte[] bytesIn = new byte[4096];
						int read = 0;

						while ((read = inputStream.read(bytesIn)) != -1) {
							outputStream.write(bytesIn, 0, read);
						}
						inputStream.close();
						outputStream.close();

						boolean completed = ftpClient.completePendingCommand();
						if (completed) {
							System.out.println("The file is uploaded successfully.");
						}

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
					ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
					String FileResponce = "";
					try {
						if (flag.equalsIgnoreCase("1")) {
							FileResponce = ow.writeValueAsString(report);
						}
						if (flag.equalsIgnoreCase("2")) {
							FileResponce = ow.writeValueAsString(documents);
						}

						js.put("response", FileResponce);
						js.put("code", "1000");
						json = js.toString();
					} catch (Exception e) {
						js.put("code", "1001");
						js.put("ErrorMessage", e.getMessage());
						json = js.toString();
						e.printStackTrace();
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					try {
						throw new Exception("Error while loading the file");
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}

			}
			return json;

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return json;

	}

	@RequestMapping(value = "/feedcom/reportFcmNotification", method = RequestMethod.POST)
	@ResponseBody
	public String reportFcmNotification(HttpServletRequest request, @RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("reportId") String reportId) throws JSONException {

		System.out.println(username + "   " + password + "   " + reportId);
		UserModel userModel = loginService.getUserByUnamePassword(username, password);

		JSONObject js = new JSONObject();
		String json = null;
		if (userModel != null) {

			Report report = dropboxservice.getReportObjectByReportId(Integer.parseInt(reportId));

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			try {

				JSONObject bodyText = new JSONObject();
				bodyText.put("id", report.getId());
				bodyText.put("fileDate", report.getDate());
				bodyText.put("name", report.getName());
				bodyText.put("userModelFrom", report.getUserModelFrom().getUserId());
				bodyText.put("userModelTo", report.getUserModelTo().getUserId());
				bodyText.put("userfeedback", report.getUserfeedback().getUserId());
				bodyText.put("dateTime", report.getDateTime());

				System.out.println(bodyText);
				js.put("response", bodyText.toString());
				js.put("code", "1000");
				json = js.toString();

			} catch (Exception e) {
				js.put("code", "1001");
				js.put("ErrorMessage", e.getMessage());
				json = js.toString();
			}
		} else {

			try {
				js.put("code", "1001");
				json = js.toString();
			} catch (JSONException e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		return json;
	}

	@RequestMapping(value = "/feedcom/documentFcmNotification", method = RequestMethod.POST)
	@ResponseBody
	public String documentFcmNotification(HttpServletRequest request, @RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("documentId") String reportId)
			throws JSONException {

		UserModel userModel = loginService.getUserByUnamePassword(username, password);

		JSONObject js = new JSONObject();
		String json = null;
		if (userModel != null) {

			Documents documents = dropboxservice.getDocumentsObjectByDocumentsId(Integer.parseInt(reportId));

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			try {

				JSONObject bodyText = new JSONObject();
				bodyText.put("id", documents.getId());
				bodyText.put("fileDate", documents.getDate());
				bodyText.put("name", documents.getName());
				bodyText.put("userModelFrom", documents.getUserModelFrom().getUserId());
				bodyText.put("userModelTo", documents.getUserModelTo().getUserId());
				bodyText.put("userfeedback", documents.getUserfeedback().getUserId());
				bodyText.put("dateTime", documents.getDateTime());

				js.put("response", bodyText.toString());
				js.put("code", "1000");
				json = js.toString();

			} catch (Exception e) {

				js.put("code", "1001");
				js.put("ErrorMessage", e.getMessage());
				json = js.toString();

			}
		} else {

			try {
				js.put("code", "1001");
				json = js.toString();
			} catch (JSONException e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		return json;
	}

	@RequestMapping(value = "/feedcom/MomFcmNotification", method = RequestMethod.POST)
	@ResponseBody
	public String MomFcmNotification(HttpServletRequest request, @RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("MomId") String momId) throws JSONException {

		UserModel userModel = loginService.getUserByUnamePassword(username, password);

		JSONObject js = new JSONObject();
		String json = null;
		if (userModel != null) {

			Mom mom = momService.getMomById(Integer.parseInt(momId));

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			try {

				JSONObject bodyText = new JSONObject();
				bodyText.put("id", mom.getId());
				bodyText.put("Attendee", mom.getAttendee());
				bodyText.put("mom_date", mom.getDate());
				bodyText.put("mom_fill_date_time", mom.getDateTime());
				bodyText.put("userModelFrom", mom.getUserModelFrom().getUserId());
				bodyText.put("userModelTo", mom.getUserModelTo().getUserId());
				bodyText.put("userfeedback", mom.getUserfeedback().getUserId());
				bodyText.put("Keypoints", mom.getKeypoints());
				bodyText.put("Subject", mom.getSubject());

				js.put("response", bodyText.toString());
				js.put("code", "1000");
				json = js.toString();

			} catch (Exception e) {

				js.put("code", "1001");
				js.put("ErrorMessage", e.getMessage());
				json = js.toString();
			}
		} else {

			try {
				js.put("code", "1001");
				json = js.toString();
			} catch (JSONException e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		return json;
	}

	@RequestMapping(value = "/feedcom/IssueTypeFcmNotification", method = RequestMethod.POST)
	@ResponseBody
	public String IssueTypeFcmNotification(HttpServletRequest request, @RequestParam("username") String username,
			@RequestParam("password") String password, @RequestParam("soNumber") String soNumber,
			@RequestParam("feedbackType") String feedbackType, @RequestParam("userFrom") String userFrom,
			@RequestParam("userTo") String userTo) throws JSONException {

		UserModel userModel = loginService.getUserByUnamePassword(username, password);

		final UserModel userModelFrom = userService.getUserById(Integer.parseInt(userFrom));
		final UserModel userModelTo = userService.getUserById(Integer.parseInt(userTo));

		JSONObject js = new JSONObject();
		String json = null;
		if (userModel != null) {
			List<Feedback> feedbacks = null;
			try {
				feedbacks = feedbackservice.getListOfChatingUsingSoNumberAndFeedBackTypeForWebservice(soNumber,
						feedbackType, userModelFrom, userModelTo);

			} catch (Exception e) {

			}

			ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
			String ListOffeedbacks;

			try {
				ListOffeedbacks = ow.writeValueAsString(feedbacks);
				js.put("Feedbacks", ListOffeedbacks);
				js.put("code", "1000");
				json = js.toString();

			} catch (Exception e) {

				js.put("code", "1001");
				js.put("ErrorMessage", e.getMessage());
				json = js.toString();
			}
		} else {

			try {
				js.put("code", "1001");
				json = js.toString();
			} catch (JSONException e) {
				try {
					js.put("code", "1001");
					js.put("ErrorMessage", e.getMessage());
					json = js.toString();
				} catch (JSONException e1) {
					e1.printStackTrace();
				}
				e.printStackTrace();
			}
		}
		
		System.out.println("Kuldeep  "+json);
		return json;
	}

}
