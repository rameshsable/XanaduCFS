package com.xanadutec.coreflex.dropBox;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.ObjectWriter;
import org.hibernate.loader.custom.Return;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import com.xanadutec.coreflex.FileUploadDownload.FileUploadService;
import com.xanadutec.coreflex.login.LoginService;
import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.Documents;
import com.xanadutec.coreflex.model.FcmNotification;
import com.xanadutec.coreflex.model.FtpInfo;
import com.xanadutec.coreflex.model.Report;
import com.xanadutec.coreflex.model.UserModel;
import com.xanadutec.coreflex.notifiation.NotificationService;
import com.xanadutec.coreflex.userTask.UserService;

@Controller
public class dropBoxControler {
	@Autowired
	DropBoxService dropboxservice;
	@Autowired
	private UserService userService;
	@Autowired
	private LoginService loginService;
	@Autowired
	FileUploadService fileUploadService;
	@Autowired
	NotificationService notificationService;

	@RequestMapping(value = "/SaveOrUpdateUserLastVisitedTimeForReport", method = RequestMethod.GET)
	@ResponseBody
	public String SaveOrUpdateUserLastVisitedTime(HttpServletRequest request) {
		UserModel userModelUser = (UserModel) request.getSession().getAttribute("UserMessage");
		int id = userModelUser.getUserId();
		String type = "Report";
		userService.SaveUserLastVisitedTime(id, type);
		return "Success";
	}

	@RequestMapping(value = "/SaveOrUpdateUserLastVisitedTimeForDocument", method = RequestMethod.GET)
	@ResponseBody
	public String SaveOrUpdateUserLastVisitedTimeForDocument(HttpServletRequest request) {
		UserModel userModelUser = (UserModel) request.getSession().getAttribute("UserMessage");
		int id = userModelUser.getUserId();
		String type = "Document";
		userService.SaveUserLastVisitedTime(id, type);
		return "Success";
	}

	@RequestMapping(value = "/dropBox", method = RequestMethod.GET)
	public ModelAndView companyRegistration(ModelMap model, HttpServletRequest request) {

		HttpSession httpSession = request.getSession();
		UserModel userModelFro = (UserModel) httpSession.getAttribute("UserFrom");
		UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");

		if (userModelFro.getCompany().getCompanyName().equalsIgnoreCase("Coreflex")) {
			model.addAttribute("hidemenuSwitch", "switchCompany");
		}

		int userFromId = userModelFro.getUserId();
		int userToId = userModelTo.getUserId();
		List<Date> distinctDatesOfDropBoxlist = dropboxservice.getDistinctDropBoxList();
		List<Date> distinctDatesOfDocumentlist = dropboxservice.getDistinctDocumentList();

		Map<Date, Map<Integer, List<Report>>> mapCountOfDistinctReportByDate = dropboxservice
				.getCountOfDistinctDropBoxListByDistinctDatesOfDropBoxlist(distinctDatesOfDropBoxlist, request);
		Map<Date, Map<Integer, List<Documents>>> mapCountOfDistinctDocumentListByDate = dropboxservice
				.getCountOfDistinctDocumentListByDistinctDatesOfDocuement(distinctDatesOfDocumentlist, request);

		model.addAttribute("mapCountOfDistinctReportByDate", mapCountOfDistinctReportByDate);

		model.addAttribute("mapCountOfDistinctDocumentListByDate", mapCountOfDistinctDocumentListByDate);

		model.addAttribute("userModelFro", userFromId);
		model.addAttribute("userModelTo", userToId);
		model.addAttribute("hidemenu", "hideAdmin");
		return new ModelAndView("DropBox");
	}

	@RequestMapping(value = "/uploadDropBoxFile", method = RequestMethod.POST)
	@ResponseBody
	public String handleFileUpload(MultipartHttpServletRequest request) throws Exception {

		Iterator<String> itrator = request.getFileNames();
		String str = "";
		while (itrator.hasNext()) {

			MultipartFile multiFile = request.getFile(itrator.next());
			try {
				// just to show that we have actually received the file
				String fileName = new Date().getTime() + multiFile.getOriginalFilename();

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

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception("Error while loading the file");
			}

		}

		return toJson(str);
	}

	@RequestMapping(value = "/uploadDocumentFile", method = RequestMethod.POST)
	@ResponseBody
	public String handleDocFileUpload(MultipartHttpServletRequest request) throws Exception {

		Iterator<String> itrator = request.getFileNames();
		String str = "";
		while (itrator.hasNext()) {

			MultipartFile multiFile = request.getFile(itrator.next());
			try {
				// just to show that we have actually received the file
				String fileName = new Date().getTime() + multiFile.getOriginalFilename();

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

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception("Error while loading the file");
			}

		}

		return toJson(str);
	}

	public String toJson(Object data) {
		ObjectMapper mapper = new ObjectMapper();
		StringBuilder builder = new StringBuilder();
		try {
			builder.append(mapper.writeValueAsString(data));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.toString();
	}

	@RequestMapping(value = "/DropBoxFileDetail", method = RequestMethod.POST)
	@ResponseBody
	public String SaveUploadInfo(@RequestParam("name") final String name, final HttpServletRequest request) {

		System.out.println("drop box detail ramesh ");
		System.out.println();
		

		new Thread() {
			public void run() {
				HttpSession httpSession = request.getSession();
				UserModel userModelFrom = (UserModel) httpSession.getAttribute("UserFrom");
				UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");
				UserModel userModelmsg = (UserModel) httpSession.getAttribute("UserMessage");
				final Report box = new Report();
				box.setName(name);
				box.setDate(new Date());
				box.setUserModelFrom(userModelFrom);
				box.setUserModelTo(userModelTo);
				box.setUserfeedback(userModelmsg);
				box.setDateTime(new Date());
				// box.setDescription(desc);
				dropboxservice.saveFIleDetails(box);
				try {
					int id = userModelmsg.getUserId();
					String type = "Report";
					userService.SaveUserLastVisitedTime(id, type);
				} catch (Exception e) {
					e.printStackTrace();
				}
				 List<UserModel> userModels = loginService
						.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userModelFrom.getCompany());
				userModels.addAll(loginService.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userModelTo.getCompany()));
				 FcmNotification fcmNotification = notificationService.getFcmNotification();
				notificationService.sendReportFcmNotification(box, fcmNotification, userModels, request);
			}
		}.start();
		
		return "";
	}

	@RequestMapping(value = "/DocumentFileDetail", method = RequestMethod.POST)
	@ResponseBody
	public String SaveDocumentUploadInfo(@RequestParam("name") final String name, final HttpServletRequest request) {

		System.out.println("drop box document  detail ramesh ");

		try {

			new Thread() {
				public void run() {
					HttpSession httpSession = request.getSession();
					UserModel userModelFrom = (UserModel) httpSession.getAttribute("UserFrom");
					UserModel userModelTo = (UserModel) httpSession.getAttribute("UserTo");
					UserModel userModelmsg = (UserModel) httpSession.getAttribute("UserMessage");
					Documents doc = new Documents();
					doc.setName(name);
					doc.setDate(new Date());
					doc.setUserModelFrom(userModelFrom);
					doc.setUserModelTo(userModelTo);
					doc.setUserfeedback(userModelmsg);
					doc.setDateTime(new Date());
					dropboxservice.saveDocumentFIleDetails(doc);
					try {
						int id = userModelmsg.getUserId();
						String type = "Document";
						userService.SaveUserLastVisitedTime(id, type);
					} catch (Exception e) {
						e.printStackTrace();
					}
					List<UserModel> userModels = loginService
							.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userModelFrom.getCompany());
					userModels.addAll(
							loginService.getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(userModelTo.getCompany()));
					FcmNotification fcmNotification = notificationService.getFcmNotification();
					notificationService.sendDocumentFcmNotification(doc, fcmNotification, userModels, request);
				}
			}.start();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}

	@RequestMapping(value = "/reportDownload", method = RequestMethod.GET)
	public void downloadFiles(@RequestParam("path") String filename, HttpServletResponse response,
			HttpServletRequest request) throws IOException {

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

	@RequestMapping(value = "/reportDelete", method = RequestMethod.GET)
	@ResponseBody
	public void deleteFiles(@RequestParam("path") String filename, HttpServletResponse response,
			HttpServletRequest request) throws IOException {
		/*
		 * 
		 * ServletContext context = request.getServletContext();
		 * 
		 * String name = filename; String rootPath1 =
		 * System.getProperty("catalina.home"); File dir1 = new File(rootPath1 +
		 * File.separator + "CfsFiles");
		 * 
		 * String AlldestFilePath = dir1.getAbsolutePath() + File.separator +
		 * filename; String path = AlldestFilePath; File file = new File(path);
		 * 
		 * 
		 * 
		 * if (file.delete()) { System.out.println(file.getName() +
		 * " is deleted!"); } else { System.out.println(
		 * "Delete operation is failed."); }
		 * 
		 * //String str = name + "#@$"; String str = name;
		 */
		dropboxservice.deleteFileByName(filename);

	}

	@RequestMapping(value = "/SearchReport", method = RequestMethod.POST)
	@ResponseBody
	public String sendEmailOrSms(@RequestParam("date") String date, @RequestParam("dateTo") String dateTo,
			@RequestParam("FileName") String FileName, @RequestParam("Criteria") String Criteria,
			@RequestParam("report") String[] report, @RequestParam("Document") String[] doc,
			HttpServletRequest request) {

		System.out.println("Arrays.asList(report)  " + Arrays.asList(report));
		System.out.println("Arrays.asList(doc)   " + Arrays.asList(doc));

		JSONObject js = new JSONObject();
		String json = null;

		List<Report> reports = new ArrayList<Report>();
		List<Documents> documents = new ArrayList<Documents>();
		if (Criteria.equalsIgnoreCase("1")) {

			int rflag = 0, dflag = 0;
			List<String> reportArray = Arrays.asList(report);
			for (String string : reportArray) {
				if (string.equalsIgnoreCase("on")) {
					rflag = 1;
					break;
				}
			}
			List<String> DocArray = Arrays.asList(doc);
			for (String string : DocArray) {
				if (string.equalsIgnoreCase("on")) {
					dflag = 1;
					break;
				}
			}

			if (rflag == 1) {

				Date ActualdateFrom = null;
				Date ActualdateTo = null;
				SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
				try {
					ActualdateFrom = format.parse(date);
					ActualdateTo = format.parse(dateTo);

				} catch (ParseException e) {
					e.printStackTrace();
				}

				List<Report> list = dropboxservice.getListOfDropBoxByDate(ActualdateFrom, ActualdateTo, request);
				reports.addAll(list);
				System.out.println("list report" + Arrays.toString(list.toArray()));

			}
			if (dflag == 1) {
				Date ActualdateFrom = null;
				Date ActualdateTo = null;
				SimpleDateFormat format = new SimpleDateFormat("MM/dd/yyyy");
				try {
					ActualdateFrom = format.parse(date);
					ActualdateTo = format.parse(dateTo);

				} catch (ParseException e) {
					e.printStackTrace();
				}
				List<Documents> list = dropboxservice.getListOfDocumentByDate(ActualdateFrom, ActualdateTo, request);
				documents.addAll(list);

				System.out.println("list doc" + Arrays.toString(list.toArray()));

			}

		} else {

			System.out.println("drop box else");
			int rflag = 0, dflag = 0;
			List<String> reportArray = Arrays.asList(report);
			for (String string : reportArray) {
				if (string.equalsIgnoreCase("on")) {
					rflag = 1;
					break;
				}
			}
			List<String> DocArray = Arrays.asList(doc);
			for (String string : DocArray) {
				if (string.equalsIgnoreCase("on")) {
					dflag = 1;
					break;
				}
			}

			if (rflag == 1) {

				List<Report> list = dropboxservice.getListOfDropBoxByFileName(FileName, request);
				reports.addAll(list);
				System.out.println("list report" + Arrays.toString(list.toArray()));
			}
			if (dflag == 1) {
				List<Documents> list = dropboxservice.getListOfDocumentByFileName(FileName, request);
				System.out.println("list doc" + Arrays.toString(list.toArray()));
				documents.addAll(list);
			}

			// FileName
		}
		ObjectWriter ow = new ObjectMapper().writer().withDefaultPrettyPrinter();
		try {
			js.put("report", ow.writeValueAsString(reports));
			js.put("document", ow.writeValueAsString(documents));
		} catch (Exception e) {
			e.printStackTrace();
		}

		json = js.toString();

		return json;
	}

}
