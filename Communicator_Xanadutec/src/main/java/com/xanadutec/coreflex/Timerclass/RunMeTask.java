package com.xanadutec.coreflex.Timerclass;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONObject;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.Part;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.Flags.Flag;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.search.FlagTerm;
import javax.mail.util.ByteArrayDataSource;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;
import com.xanadutec.coreflex.feedback.FeedBackCountComparator;
import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.Documents;
import com.xanadutec.coreflex.model.EmailSendReceivePermission;
import com.xanadutec.coreflex.model.FcmNotification;
import com.xanadutec.coreflex.model.FeedBackChatingCounter;
import com.xanadutec.coreflex.model.FeedBackSonumberSortingByDate;
import com.xanadutec.coreflex.model.FeedBackTypeTable;
import com.xanadutec.coreflex.model.Feedback;
import com.xanadutec.coreflex.model.FtpInfo;
import com.xanadutec.coreflex.model.Operator;
import com.xanadutec.coreflex.model.Report;
import com.xanadutec.coreflex.model.RoleModel;
import com.xanadutec.coreflex.model.ServerInfo;
import com.xanadutec.coreflex.model.Status;
import com.xanadutec.coreflex.model.UrlForFileUploadDownload;
import com.xanadutec.coreflex.model.UserModel;

public class RunMeTask {

	@Autowired
	private SessionFactory sessionFactory;

	public void printMe() {
		System.out.println("Run Me ~");
		readMails();
	}

	Properties properties = null;
	private Session session = null;
	private Store store = null;
	private Folder inbox = null;

	public void readMails() {

		final ServerInfo serverInfo = getServerInfo();
		properties = new Properties();
		properties.setProperty("mail.host", serverInfo.getMail_host());
		properties.setProperty("mail.port", serverInfo.getMail_port());
		properties.setProperty("mail.transport.protocol", "imaps");
		session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(serverInfo.getMail_serverName(), serverInfo.getMail_password());
			}
		});
		try {
			store = session.getStore("imaps");
			store.connect();
			inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);
			Message messages[] = inbox.search(new FlagTerm(new Flags(Flag.SEEN), false));

			System.out.println("Number of mails = " + messages.length);
			for (int i = 0; i < messages.length; i++) {
				Message message = messages[i];
				// email id of sender
				Address[] froms = message.getFrom();
				String fromEmailID = froms == null ? null : ((InternetAddress) froms[0]).getAddress();

				Address[] from = message.getFrom();
				System.out.println("-------------------------------");
				System.out.println("Date : " + message.getSentDate());
				System.out.println("From : " + from[0]);

				StringBuilder builderskip = new StringBuilder(from[0] + "");
				int start = builderskip.indexOf("<");
				int end = builderskip.indexOf(">");
				String emailIdSkip = "";
				if (start == end) {
					System.out.println("nikhil rathod demo application");
					try {
						emailIdSkip = builderskip.toString();
					} catch (Exception e) {
						e.printStackTrace();
					}
				} else {

					try {
						emailIdSkip = builderskip.substring(start + 1, end);
					} catch (Exception e) {
						e.printStackTrace();
					}

				}

				// save email id in email permission if exists then update
				try {
					SaveEmailPermissionByUserId(getUserModelByEmailId(emailIdSkip).getUserId());
				} catch (Exception e) {
					e.printStackTrace();
				}

				System.out.println(emailIdSkip + "        emailIdSkip");

				System.out.println("Subject: " + message.getSubject());
				String messageSubject = message.getSubject();
				Date messagedate = message.getSentDate();
				System.out.println(message.getReceivedDate());
				System.out.println("Content :");
				cnt = 0;
				// get actual current text contents form email
				String contents = processMessageBody(message);
				System.out.println("++ " + contents + " ++");
				System.out.println(contents.indexOf(">:"));
				int ind = contents.indexOf(">:");

				String FeedBackFileNames = "";

				ArrayList<String> allfileNames = new ArrayList<String>();
				ArrayList<String> forSaveAllfileNames = new ArrayList<String>();
				try {

					String AlldestFilePath;
					Multipart multipart1 = (Multipart) messages[i].getContent();

					for (int i11 = 0; i11 < multipart1.getCount(); i11++) {

						MimeBodyPart part1 = (MimeBodyPart) multipart1.getBodyPart(i11);
						if (Part.ATTACHMENT.equalsIgnoreCase(part1.getDisposition())) {

							AlldestFilePath = part1.getFileName();
							InputStream input1 = part1.getInputStream();

							// Create the file on server
							String tempFileName = new Date().getTime() + part1.getFileName();
							forSaveAllfileNames.add(tempFileName);
							FileOutputStream output1 = new FileOutputStream(tempFileName);
							String fileName1 = part1.getFileName();

							allfileNames.add(AlldestFilePath);

							FeedBackFileNames = FeedBackFileNames + AlldestFilePath;

							// just to show that we have actually received the
							// file

							FtpInfo ftpInfo = getFtpInfo();

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
								String secondRemoteFile = "/TEST/" + tempFileName;

								OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
								byte[] bytesIn = new byte[4096];
								int read = 0;

								while ((read = input1.read(bytesIn)) != -1) {
									outputStream.write(bytesIn, 0, read);
								}
								input1.close();
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
						}
					}
				} catch (Exception e) {
					e.printStackTrace();
					// TODO: handle exception
				}

				String sonumber = "", operatorName = "", initiateMessage = "", aveyaNo = "", documentNo = "",
						communicationType = "", MasterKey = "",
						ClientCompanyNameInEmail = "";/* ,other="",; */

				/*
				 * if (message.getSubject().startsWith("Fwd:") ||
				 * message.getSubject().startsWith("Fwd:") &&(!
				 * message.getSubject().startsWith("Re:") ||
				 * !message.getSubject().startsWith("RE:"))) {
				 * 
				 * } else
				 */
				if (message.getSubject().startsWith("Re:") || message.getSubject().startsWith("RE:")) {

					System.out.println("reply  122");
					System.out.println(" " + contents);

					StringBuilder builder = new StringBuilder(contents);

					String actual = contents;
					int fromflag = 0;
					try {
						/*
						 * int index = builder.indexOf("From:"); if (fromflag ==
						 * 0) {
						 * 
						 * if (index != -1) { actual = builder.substring(0,
						 * index); fromflag = 1; } } index = builder.indexOf(
						 * "> wrote:"); System.out.println(index +
						 * "  index  for new user"); if (fromflag == 0) {
						 * 
						 * if (index == -1) {
						 * 
						 * actual = contents; } else { actual =
						 * builder.substring(0, index); int lst =
						 * actual.lastIndexOf("<"); actual = actual.substring(0,
						 * lst); lst = actual.lastIndexOf(","); actual =
						 * actual.substring(0, lst);
						 * 
						 * // from[0] is from gmail account then remove On, int
						 * lstIndex = actual.lastIndexOf("On"); actual =
						 * actual.substring(0, lstIndex);
						 * 
						 * } }
						 * 
						 */} catch (Exception e) {
						e.printStackTrace();
					}
					System.out.println("......................");
					System.out.println(actual);
					System.out.println("......................");

					UserModel userModelMessage = getUserModelByEmailId(emailIdSkip);
					UserModel coreflexuserModel = getclientCompanyAdminById(1);
					UserModel ssiuserModel = getclientCompanyAdminByEmailOfClientUser(emailIdSkip);

					if (ssiuserModel.getCompany().getCompanyId() == 1) {

						System.out.println("admin company");
						int id1 = 0;
						Feedback actualFeedback = null;
						try {
							actualFeedback = getFeedbackObjectFromFeedbackSubject(message.getSubject());
						} catch (Exception e) {
							e.printStackTrace();
						}

						if (actualFeedback != null) {
							id1 = actualFeedback.getUserModelFrom().getCompany().getCompanyId();
							if (id1 == 1) {
								ssiuserModel = getclientCompanyAdminByEmailOfClientUser(
										actualFeedback.getUserModelTo().getEmail());
								System.out.println("Id == 1");
							} else {
								ssiuserModel = getclientCompanyAdminByEmailOfClientUser(
										actualFeedback.getUserModelFrom().getEmail());
								System.out.println("Id == else");

							}
						}

					}

					List<UserModel> cfsUserList = getListOfUserByCompanyId(1);
					List<UserModel> ssiUserList = getListOfuserByCompanyEmails(
							ssiuserModel.getCompany().getCompanyId() + "");

					Company company = new Company();
					company.setCompanyId(1);
					List<String> cfsemailList = new ArrayList<String>();
					List<String> ssiemailList = new ArrayList<String>();

					// get email permission according to company outcome data is
					// in list<String>
					List<EmailSendReceivePermission> emailSendReceivePermissions = getEmailPermissionUserListAccordingToCompany(
							company.getCompanyId() + "");

					for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissions) {
						cfsemailList.add(emailSendReceivePermission.getUserModel().getEmail());
					}

					List<EmailSendReceivePermission> emailSendReceivePermissionsClient = getEmailPermissionUserListAccordingToCompany(
							ssiuserModel.getCompany().getCompanyId() + "");

					for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissionsClient) {
						ssiemailList.add(emailSendReceivePermission.getUserModel().getEmail());
					}

					String msg = "";
					// find email sender
					if (userModelMessage.getCompany().getCompanyName().equalsIgnoreCase("Coreflex")) {
						msg = "cfs";
					}

					if (!msg.equalsIgnoreCase("cfs")) {
						for (UserModel us : ssiUserList) {
							System.out.println(us.getEmail() + " ....  inside email 2 .....");
							if (fromEmailID.equalsIgnoreCase(us.getEmail())) {

								userModelMessage = us;
								msg = "ssi";
								break;
							}
						}
					}

					if (msg.equalsIgnoreCase("cfs")) {

						System.out.println("Inside cfs");
						// 1) get sonumber from [feedback] subject if not found
						// 2) then get sonumber from [Query] subject
						Feedback actualFeedback = getFeedbackObjectFromFeedbackSubject(message.getSubject());
						// checking for feedback subject if found then save the
						// contents in feedback table
						if (actualFeedback != null) {

							System.out.println("Actualsonumber  feedback  " + actualFeedback.getSoNumber());

							if (userModelMessage.getCompany().getCompanyId() == 1) {
								if (actualFeedback.getUserModelTo().getCompany().getCompanyId() == 1) {
									saveReplyedChattingForFeedCom(actualFeedback.getSoNumber(),
											actualFeedback.getEmailSubject(), forSaveAllfileNames, actual,
											actualFeedback.getFeedBackType().getFeedbackType(),
											actualFeedback.getOperator().getUserName(), actualFeedback.getUserModelTo(),
											actualFeedback.getUserModelFrom(), userModelMessage);
								} else {
									saveReplyedChattingForFeedCom(actualFeedback.getSoNumber(),
											actualFeedback.getEmailSubject(), forSaveAllfileNames, actual,
											actualFeedback.getFeedBackType().getFeedbackType(),
											actualFeedback.getOperator().getUserName(),
											actualFeedback.getUserModelFrom(), actualFeedback.getUserModelTo(),
											userModelMessage);
								}

							} else {

								if (userModelMessage.getCompany().getCompanyId() == actualFeedback.getUserModelFrom()
										.getCompany().getCompanyId()) {
									saveReplyedChattingForFeedCom(actualFeedback.getSoNumber(),
											actualFeedback.getEmailSubject(), forSaveAllfileNames, actual,
											actualFeedback.getFeedBackType().getFeedbackType(),
											actualFeedback.getOperator().getUserName(),
											actualFeedback.getUserModelFrom(), actualFeedback.getUserModelTo(),
											userModelMessage);
								} else {
									saveReplyedChattingForFeedCom(actualFeedback.getSoNumber(),
											actualFeedback.getEmailSubject(), forSaveAllfileNames, actual,
											actualFeedback.getFeedBackType().getFeedbackType(),
											actualFeedback.getOperator().getUserName(), actualFeedback.getUserModelTo(),
											actualFeedback.getUserModelFrom(), userModelMessage);
								}

								System.out.println("nikhil   Actualsonumber");
							}

							// send email reply
							/*
							 * cfsemailList.remove(emailIdSkip);
							 * ssiemailList.remove(emailIdSkip);
							 */

							ForwardEmailToCoreFlex(message, ssiemailList, cfsemailList, userModelMessage);

						}

					} else if (msg.equalsIgnoreCase("ssi")) {
						// 1) get sonumber from [feedback] subject if not found
						// 2) then get sonumber from [Query] subject

						System.out.println("Inside ssi");

						Feedback actualFeedback = getFeedbackObjectFromFeedbackSubject(message.getSubject());
						System.out.println(actualFeedback + "  actualFeedback");
						// checking for feedback subject if found then save the
						// contents in feedback table
						if (actualFeedback != null) {

							if (userModelMessage.getCompany().getCompanyId() == ssiuserModel.getCompany()
									.getCompanyId()) {

								saveReplyedChattingForFeedCom(actualFeedback.getSoNumber(),
										actualFeedback.getEmailSubject(), forSaveAllfileNames, actual,
										actualFeedback.getFeedBackType().getFeedbackType(),
										actualFeedback.getOperator().getUserName(), ssiuserModel, coreflexuserModel,
										userModelMessage);
							} else {
								saveReplyedChattingForFeedCom(actualFeedback.getSoNumber(),
										actualFeedback.getEmailSubject(), forSaveAllfileNames, actual,
										actualFeedback.getFeedBackType().getFeedbackType(),
										actualFeedback.getOperator().getUserName(), coreflexuserModel, ssiuserModel,
										userModelMessage);
							}
							ForwardEmailToCoreFlex(message, cfsemailList, ssiemailList, userModelMessage);
							System.out.println("Actualsonumber  feedback ssi " + actualFeedback.getSoNumber());

						}
					}

				} else {

					System.out.println("Forward 1");
					ArrayList<String> allMsgContents = new ArrayList<String>();

					Document doc = Jsoup.parse(contents.toString());

					String plainText = doc.body().text();

					StringTokenizer st = new StringTokenizer(plainText, "|");
					while (st.hasMoreTokens()) {
						String firstToken = st.nextToken();
						allMsgContents.add(firstToken);
						/*
						 * StringTokenizer tokenizer = new
						 * StringTokenizer(firstToken, ":"); while
						 * (tokenizer.hasMoreElements()) { String secondToken =
						 * tokenizer.nextToken(); }
						 */
					}

					System.out.println("xlsl");
					String feedbackTypeTable = "";

					try {
						for (String msg : allMsgContents) {

							String[] msgcont = msg.split(":");
							if (msgcont[0].trim().equalsIgnoreCase("SONUMBER")) {
								try {
									String soSplit[] = msg.split(":");
									sonumber = soSplit[1];
								} catch (Exception e) {
									sonumber = 0 + "";
								}
							}

							if (msgcont[0].trim().equalsIgnoreCase("AVAYANUMBER")) {
								try {
									String avSplit[] = msg.split(":");
									aveyaNo = avSplit[1];
								} catch (Exception e) {
									aveyaNo = 0 + "";
								}
							}

							if (msgcont[0].trim().equalsIgnoreCase("DOCUMENTID")) {
								try {
									String docSplit[] = msg.split(":");
									documentNo = docSplit[1];

								} catch (Exception e) {
									documentNo = 0 + "";
								}
							}

							if (msgcont[0].trim().equalsIgnoreCase("OPERATORID")) {
								try {
									String opeSplit[] = msg.split(":");
									operatorName = opeSplit[1];
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							if (msgcont[0].trim().equalsIgnoreCase("ISSUETYPE")) {
								try {
									String feedSplit[] = msg.split(":");
									feedbackTypeTable = feedSplit[1];
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
							if (msgcont[0].trim().equalsIgnoreCase("COMPANY")) {
								try {
									String compSplit[] = msg.split(":");
									ClientCompanyNameInEmail = compSplit[1];
								} catch (Exception e) {
									e.printStackTrace();
								}
							}
						}
						List<FeedBackTypeTable> feebackTypelist = getFeedbackList();
						String feebackType = "";

						for (FeedBackTypeTable feedTypeTable : feebackTypelist) {
							feedbackTypeTable = feedbackTypeTable.trim();
							feedbackTypeTable = feedbackTypeTable.replace("\\s", "");
							feedbackTypeTable = feedbackTypeTable.trim();
							if (feedTypeTable.getFeedbackType().trim().equalsIgnoreCase(feedbackTypeTable)) {
								feebackType = feedTypeTable.getId() + "";
								break;
							}

						}
						String operatorId = null;
						// List<Operator> operators =
						// getListOfActiveOperators();
						try {

							try {
								operatorName = operatorName.trim();
							} catch (Exception ee) {
								operatorName = "Nill";
							}
							if (operatorName.equalsIgnoreCase("")) {
								operatorName = "Nill";
								Operator operator = getOperatorByUserName(operatorName);
								operatorId = operator.getOperatorId() + "";
							} else {

								Operator operator = getOperatorByUserName(operatorName);

								operatorId = operator.getOperatorId() + "";
							}

						} catch (Exception e) {
							e.printStackTrace();
						}
						double dblso = 0;
						double dbldoc = 0;
						double dblav = 0;
						// StringBuilder

						try {
							dblso = Double.parseDouble(sonumber);
						} catch (Exception e) {

						}
						try {
							dblav = Double.parseDouble(aveyaNo);
						} catch (Exception e) {

						}
						try {
							dbldoc = Double.parseDouble(documentNo);
						} catch (Exception e) {

						}

						String revisedEmailsubject = messageSubject + " " + new Date().getTime();

						// remove file email and database from array
						// and this file will not visible to client
						// or admin company

						// save email in feedcom

						System.out.println(contents + "");
						System.out.println("---------------========================");

						if (dblso == 0 && dbldoc == 0 && dblav == 0) {
							saveNewFeedComChat(
									(int) dblso + "-" + new Date().getTime() + "#@" + (int) dblav + "#@" + (int) dbldoc,
									feebackType, revisedEmailsubject, forSaveAllfileNames, contents, operatorId,
									messagedate, emailIdSkip, ClientCompanyNameInEmail);
						} else {

							saveNewFeedComChat((int) dblso + "#@" + (int) dblav + "#@" + (int) dbldoc, feebackType,
									revisedEmailsubject, forSaveAllfileNames, contents, operatorId, messagedate,
									emailIdSkip, ClientCompanyNameInEmail);
						}

						// test email demo
						try {

							UserModel ssiuserModel = getclientCompanyAdminByEmailOfClientUser(emailIdSkip);

							UserModel usermsg = getclientCompanyAdminByEmailOfClientUser(emailIdSkip);

							UserModel userEmailSender = getUserModelByEmailId(emailIdSkip);

							String userFirstAndLastName = "            Sender :  " + userEmailSender.getFirstName()
									+ " " + userEmailSender.getLastName();

							List<UserModel> cfsUserList = getListOfUserByCompanyId(1);
							List<UserModel> ssiUserList = getListOfuserByCompanyEmails(
									ssiuserModel.getCompany().getCompanyId() + "");

							Company company = new Company();
							company.setCompanyId(1);

							List<String> cfsemailList = new ArrayList<String>();
							List<String> ssiemailList = new ArrayList<String>();

							// get email permission according to
							// company
							// outcome data is
							// in list<String>

							// change this according to requirement
							// save new feedback code

							// if communicationType is feedcom

							if (communicationType.equalsIgnoreCase("FeedCom")) {
								List<EmailSendReceivePermission> emailSendReceivePermissions = getEmailPermissionUserListAccordingToCompany(
										company.getCompanyId() + "");

								for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissions) {
									cfsemailList.add(emailSendReceivePermission.getUserModel().getEmail());
								}

								List<EmailSendReceivePermission> emailSendReceivePermissionsClient = getEmailPermissionUserListAccordingToCompany(
										ssiuserModel.getCompany().getCompanyId() + "");

								for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissionsClient) {
									ssiemailList.add(emailSendReceivePermission.getUserModel().getEmail());
								}
								sendNewEmail(forSaveAllfileNames, cfsemailList, ssiemailList, revisedEmailsubject,
										contents + "<br>" + System.getProperty("line.separator") + "<br>"
												+ initiateMessage + userFirstAndLastName,
										"");
							} else {

								Company companyname = null;

								companyname = getClientCompanyObjectCompanyName(ClientCompanyNameInEmail);

								List<EmailSendReceivePermission> emailSendReceivePermissions = getEmailPermissionUserListAccordingToCompany(
										company.getCompanyId() + "");

								for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissions) {
									cfsemailList.add(emailSendReceivePermission.getUserModel().getEmail());
								}

								List<EmailSendReceivePermission> emailSendReceivePermissionsClient = getEmailPermissionUserListAccordingToCompany(
										companyname.getCompanyId() + "");

								for (EmailSendReceivePermission emailSendReceivePermission : emailSendReceivePermissionsClient) {
									ssiemailList.add(emailSendReceivePermission.getUserModel().getEmail());
								}

								sendNewEmail(forSaveAllfileNames, ssiemailList, cfsemailList, revisedEmailsubject,
										contents + "<br>" + System.getProperty("line.separator") + "<br>"
												+ initiateMessage + userFirstAndLastName,
										"");
							}
							/*
							 * * ForwardEmailToCoreFlex(message, cfsemailList,
							 * ssiemailList);
							 */

						} catch (Exception e) {
							e.printStackTrace();
						}

					} catch (Exception e) {
						e.printStackTrace();
					}

				}
				try {

					Multipart multipart = (Multipart) messages[i].getContent();

					int flagOfMasterKey = 0;
					if (message.getSubject().startsWith("Fwd:")) {
					}

					if (flagOfMasterKey == 0) {
						// allfileNames;
					}
					BodyPart bp = multipart.getBodyPart(0);
					Document doc = Jsoup.parse(bp.toString());

					String plainText = doc.body().text();
				} catch (Exception e) {
					e.printStackTrace();
				}
				message.setFlag(Flags.Flag.SEEN, true);
				message.setFlag(Flags.Flag.DELETED, true);
			}

			inbox.close(true);
			store.close();
		} catch (

		NoSuchProviderException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	private Feedback getFeedbackObjectFromFeedbackSubject(String subject) {
		// TODO Auto-generated method stub

		System.out.println(subject + " email subject");
		org.hibernate.Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Feedback.class);

		Feedback feedback = null;
		try {

			String str = "";
			if (subject.startsWith("Re:") || subject.startsWith("RE:")) {
				str = subject.substring(4, subject.length());

				System.out.println(str + " str");
				criteria.add(Restrictions.eq("emailSubject", str));
				List<Feedback> list = criteria.list();
				Collections.sort(list, new FeedBackCountComparator());
				feedback = (Feedback) list.get(0);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (feedback == null) {
				int reverselen = subject.length();
				int len = reverselen - 13;
				String actsub = subject.substring(len, reverselen);
				criteria = session.createCriteria(Feedback.class);
				criteria.add(Restrictions.ilike("emailSubject", "%" + actsub + "%"));
				List<Feedback> list = criteria.list();
				Collections.sort(list, new FeedBackCountComparator());
				feedback = (Feedback) list.get(0);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		session.close();
		return feedback;
	}

	public List<Operator> getListOfActiveOperators() {
		org.hibernate.Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(Operator.class);
		criteria.add(Restrictions.eq("status", "Active"));
		List<Operator> operators = criteria.list();
		session.close();
		return operators;
	}

	public String processMessageBody(Message message) {
		String cont = "";
		try {
			Object content = message.getContent();
			// check for string
			// then check for multipart
			if (content instanceof String) {

				cont = conte + (String) content;
				Document doc = Jsoup.parse(content.toString());

				String plainText = doc.body().text();
				cont = plainText;

				// System.out.println(content);
			} else if (content instanceof Multipart) {
				Multipart multiPart = (Multipart) content;
				cont = procesMultiPart(multiPart);
			} else if (content instanceof InputStream) {
				InputStream inStream = (InputStream) content;
				int ch;
				while ((ch = inStream.read()) != -1) {
					// System.out.write(ch);
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return cont;
	}

	static int cnt = 0;
	String conte = "";

	public String procesMultiPart(Multipart content) {
		conte = "";
		try {
			int multiPartCount = content.getCount();
			System.out.println(multiPartCount + "    demo");
			for (int i = 0; i < multiPartCount; i++) {

				BodyPart bodyPart = content.getBodyPart(i);
				Object o;

				o = bodyPart.getContent();
				if (o instanceof String) {
					conte = (String) o;
					System.out.println(o + "nikhil is in ................!!!!!!!!");
					/*
					 * Document doc = Jsoup.parse(o.toString());
					 * 
					 * String plainText = doc.body().text(); conte = plainText;
					 */
				} else if (o instanceof Multipart) {
					Document doc = Jsoup.parse(o.toString());

					String plainText = doc.body().text();

					System.out.println(plainText + " cnt  .................  ?????? ..........." + cnt);
					System.out.println("m part" + o);
					procesMultiPart((Multipart) o);
				}
				cnt++;

			}
		} catch (IOException e) {
			e.printStackTrace();
		} catch (MessagingException e) {
			e.printStackTrace();
		}
		return conte;

	}

	/*
	 * public static void main(String[] args) { ReadMailSample sample = new
	 * ReadMailSample(); sample.readMails(); }
	 */

	public String saveNewFeedComChat(String soNumber, String feebackType, String messageSubject,
			ArrayList<String> allfileNames, String Chating, String operId, Date msgdate, String emailIdofClient,
			String companyName) {
		org.hibernate.Session session = sessionFactory.openSession();
		try {

			Status status2 = new Status();
			status2.setStatusId(1);

			FeedBackTypeTable typeTable = new FeedBackTypeTable();
			typeTable.setId(Integer.parseInt(feebackType));

			// if feedback type is report then save a copy of it in Reports
			// section

			UserModel to = null;
			UserModel from = null;
			UserModel userMessage = getUserModelByEmailId(emailIdofClient);
			UserModel client = null;

			try {
				Company companyClient = getClientCompanyObjectCompanyName(companyName);
				if (companyClient != null) {
					System.out.println("Not null");
				}
				client = getClientCompanyAdminByCompany(companyClient);

				System.out.println(client.getFirstName() + "     client.getFirstName()");
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (userMessage.getCompany().getCompanyId() == 1) {
				from = getclientCompanyAdminById(1);
				to = getClientCompanyAdminByCompany(client.getCompany());
			} else {
				to = getclientCompanyAdminById(1);
				from = getclientCompanyAdminByEmailOfClientUser(emailIdofClient);
			}

			FeedBackTypeTable feedBackType = getFeedBackTypeObjectByFeedBackId(Integer.parseInt(feebackType));
			if (feedBackType.getFeedbackType().equalsIgnoreCase("Report")) {
				// check allfileNames not null
				if (allfileNames != null) {
					for (String fileName : allfileNames) {

						Report box = new Report();
						box.setName(fileName);
						box.setUserModelFrom(from);
						box.setUserModelTo(to);
						box.setUserfeedback(userMessage);
						box.setDate(new Date());
						saveFIleDetails(box);

					}
				}
			}
			if (feedBackType.getFeedbackType().equalsIgnoreCase("Document")) {
				// check allfileNames not null
				if (allfileNames != null) {
					for (String fileName : allfileNames) {

						Documents box = new Documents();
						box.setName(fileName);
						box.setDate(new Date());
						box.setUserModelFrom(from);
						box.setUserModelTo(to);
						box.setUserfeedback(userMessage);

						saveFIleDetails(box);

					}
				}
			}

			FeedBackChatingCounter FeedchatingCount = new FeedBackChatingCounter();

			// FeedchatingCount.setFeedbackCounter(count);

			FeedchatingCount.setSo_number(soNumber);
			FeedchatingCount.setFeedBackType(typeTable);
			FeedchatingCount.setUserModelFrom(from);
			FeedchatingCount.setUserModelTo(to);
			FeedchatingCount.setCount(1);

			Operator operator = new Operator();
			try {
				System.out.println(operId + "  operId-------------");
				operator = (Operator) session.get(Operator.class, Integer.parseInt(operId));
			} catch (Exception e) {
				e.printStackTrace();

			}

			Transaction transaction = session.beginTransaction();
			session.save(FeedchatingCount);

			final Feedback feedback = new Feedback();
			feedback.setEmailSubject(messageSubject);

			String s = "";
			for (String string : allfileNames) {

				s = s + string + "#@$";
			}

			feedback.setAttachment(s);
			feedback.setFeedbackText(Chating);
			feedback.setCounter(FeedchatingCount);
			feedback.setSoNumber(soNumber);
			feedback.setFeedBackType(typeTable);
			try {
				if (operator == null) {

				} else {
					feedback.setOperator(operator);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}

			feedback.setStatus(status2);
			feedback.setFdate(msgdate);

			feedback.setUserModelFrom(from);
			feedback.setUserModelTo(to);
			feedback.setUserfeedback(userMessage);

			session.save(feedback);
			transaction.commit();

			// save preference according to date

			saveFeedBackSonumberSortingByDate(feedback);

			new Thread() {
				public void run() {
					sendFcmNotification(feedback);

				}
			}.start();

			return feedback.getFeedbackId() + "";
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return "";
	}

	public void sendFcmNotification(Feedback feedback) {

		List<UserModel> userFromIds = null, userToIds = null;
		// https://firebase.google.com/docs/cloud-messaging/http-server-ref
		try {
			userFromIds = getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(
					feedback.getUserModelFrom().getCompany());
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			userToIds = getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(
					feedback.getUserModelTo().getCompany());
		} catch (Exception e) {
			e.printStackTrace();
		}

		userFromIds.addAll(userToIds);

		System.out.println(userFromIds.size()+"   size...............");
		FcmNotification fcmNotification = getFcmNotification();

		try {

			for (UserModel userModel : userFromIds) {
				try {
					JSONObject body = new JSONObject();
					String CLIENT_TOKEN = userModel.getDeviceToken();// "cVVPt5vJizI:APA91bFOgGjOd1gDaU7GEyftM5P-FP7wekoMXgh0ODJqlc_SxZGulwCwAk6C6iyLaXp5TTWWqdo2B12ps7zvNMP6JU4pknn_MATPPiiA-zNNNlMF6jg_hJQ_FNObzRxT5LySKzeFamB5";
																		// //
																		// https://developers.google.com/instance-id/
					body.put("to", CLIENT_TOKEN);
					body.put("priority", "high");
				//	body.put("click_action", "OPEN_ACTIVITY_1");
					body.put("click_action", "NotificationMessage.java");

					JSONObject bodyText = new JSONObject();
					bodyText.put("CompanyFrom", feedback.getUserModelFrom());
					bodyText.put("CompanyTo", feedback.getUserModelTo());
					bodyText.put("IssueType", feedback.getFeedBackType().getId());
					bodyText.put("SoNumber", feedback.getSoNumber());

					JSONObject notification = new JSONObject();
					notification.put("body", bodyText.toString());
					notification.put("title", "CoreFlex Communicator");
					notification.put("sound", "default");
					notification.put("priority", "high");

					// notification.put("icon", "myicon");

					JSONObject data = new JSONObject();
					data.put("id", "1");

					body.put("notification", notification);
					body.put("data", data);

					String url = fcmNotification.getUrl();
					URL obj = new URL(url);
					HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

					// add reuqest header
					con.setRequestMethod("POST");
					con.setRequestProperty("Authorization", fcmNotification.getAuthorization());
					con.setRequestProperty("content-type", "application/json");

					// Send post request
					con.setDoOutput(true);
					OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
					wr.write(body.toString());
					wr.flush();

					int responseCode = con.getResponseCode();
				//	System.out.println("\nSending 'POST' request to URL : " + url);
				//	System.out.println("Response Code : " + responseCode);
				//	System.out.println(body.toString());

					BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
					String inputLine;
					StringBuffer response = new StringBuffer();

					while ((inputLine = in.readLine()) != null) {
						response.append(inputLine);
					}
					in.close();

					// print result
			//		System.out.println(response.toString());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public FcmNotification getFcmNotification() {
		// TODO Auto-generated method stub
		try {
			org.hibernate.Session session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(FcmNotification.class);
			List<FcmNotification> fcmNotifications = criteria.list();
			session.close();
			return fcmNotifications.get(0);
		} catch (Exception e) {

		}
		return null;
	}

	private UserModel getclientCompanyAdminByEmailOfClientUser(String emailIdofClient) {
		org.hibernate.Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(UserModel.class);
		criteria.add(Restrictions.eq("email", emailIdofClient));
		UserModel userModel = (UserModel) criteria.uniqueResult();

		RoleModel roleModel = new RoleModel();
		roleModel.setId(1);
		Company company = userModel.getCompany();
		company.setCompanyId(userModel.getCompany().getCompanyId());

		session = sessionFactory.openSession();
		criteria = session.createCriteria(UserModel.class);
		criteria.add(Restrictions.eq("userRoll", roleModel));
		criteria.add(Restrictions.eq("company", company));
		userModel = (UserModel) criteria.uniqueResult();

		System.out.println(userModel.getUserName());
		session.close();
		return userModel;

	}

	private UserModel getUserModelByEmailId(String emailIdofClient) {
		org.hibernate.Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(UserModel.class);
		criteria.add(Restrictions.eq("email", emailIdofClient));
		UserModel userModel = (UserModel) criteria.uniqueResult();
		session.close();
		return userModel;

	}

	private List<UserModel> getListOfuserByCompanyEmails(String id) {
		org.hibernate.Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(UserModel.class);
		criteria.createCriteria("company", "c");
		criteria.add(Restrictions.eq("c.companyId", Integer.parseInt(id)));

		List<UserModel> userModels = criteria.list();

		session.close();
		return userModels;

	}

	public UserModel getclientCompanyAdminById(int companyId) {
		// TODO Auto-generated method stub
		RoleModel roleModel = new RoleModel();
		roleModel.setId(1);
		Company company = new Company();
		company.setCompanyId(companyId);
		org.hibernate.Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(UserModel.class);
		criteria.add(Restrictions.eq("userRoll", roleModel));
		criteria.add(Restrictions.eq("company", company));
		UserModel userModel = (UserModel) criteria.uniqueResult();

		System.out.println(userModel.getUserName());
		session.close();
		return userModel;
	}

	public List<FeedBackTypeTable> getFeedbackList() {
		org.hibernate.Session session = sessionFactory.openSession();
		try {
			Criteria criteria = session.createCriteria(FeedBackTypeTable.class);
			return criteria.list();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;

	}

	public void ForwardEmailToCoreFlex(Message message, List<String> emailTo, List<String> emailCC,
			UserModel userModelMessage) {

		Date date = null;
		final ServerInfo serverInfo = getServerInfo();
		Properties properties = new Properties();
		properties.setProperty("mail.host", serverInfo.getMail_host());
		properties.setProperty("mail.port", serverInfo.getMail_port());
		properties.setProperty("mail.transport.protocol", "imaps");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.host", serverInfo.getMail_host());
		properties.put("mail.smtp.port", serverInfo.getMail_port());
		Session session = Session.getDefaultInstance(properties);
		try {
			Store store = session.getStore("imaps");
			store.connect(serverInfo.getMail_serverName(), serverInfo.getMail_password());// change
			// the
			// user
			// and
			// password
			// accordingly
			BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
			System.out.print("Do you want to reply [y/n] : ");
			/* String ans = "y"; */
			try {

				String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));

				Message replyMessage = new MimeMessage(session);
				replyMessage = (MimeMessage) message.reply(false);
				replyMessage.setFrom(new InternetAddress(to));

				/*
				 * List<UserModel>listOfUser = getListOfUserByCompanyId(1);
				 * 
				 * 
				 * List<String> listOfemailAddress= new ArrayList<String>();
				 * 
				 * for( UserModel usrlist:listOfUser){
				 * listOfemailAddress.add(usrlist.getEmail()); }
				 * 
				 * StringBuffer emailsList= new
				 * StringBuffer(Arrays.toString(listOfemailAddress.toArray()));
				 * emailsList.deleteCharAt(0);
				 * emailsList.deleteCharAt(emailsList.length()-1);
				 * replyMessage.setRecipients(Message.RecipientType.TO,
				 * InternetAddress.parse(emailsList.toString()));
				 */

				String[] TO = emailTo.toArray(new String[emailTo.size()]);
				StringBuffer bufTo = new StringBuffer(Arrays.toString(TO));
				try {
					bufTo.deleteCharAt(0);
					bufTo.deleteCharAt(bufTo.length() - 1);
				} catch (Exception e) {
					e.printStackTrace();
				}

				String[] CC = emailCC.toArray(new String[emailCC.size()]);
				StringBuffer bufCC = new StringBuffer(Arrays.toString(CC));
				try {
					bufCC.deleteCharAt(0);
					bufCC.deleteCharAt(bufCC.length() - 1);
				} catch (Exception e) {
					e.printStackTrace();
				}

				replyMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(bufTo.toString(), false));
				replyMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(bufCC.toString(), false));

				// replyMessage.addRecipients(Message.RecipientType.CC,
				// recipientAddress);
				// replyMessage.setReplyTo(message.getReplyTo());
				// replyMessage.setReplyTo( InternetAddress.toString());
				/* message.setText("sender nikhil rathod"); */
				replyMessage.setContent(message, "message/rfc822");
				/* replyMessage.setText(); */

				message.getContent();

				try {

					BodyPart messageBodyPart = new MimeBodyPart();

					// messageBodyPart.setText("sender nikhil rathod");

					Multipart multipart = new MimeMultipart();
					multipart.addBodyPart(messageBodyPart);
					messageBodyPart.setDataHandler(message.getDataHandler());
					replyMessage.setContent(multipart);

					String userMessage = userModelMessage.getFirstName() + " " + userModelMessage.getLastName();

					MimeBodyPart messageBodyP = new MimeBodyPart();
					messageBodyP.setContent("Sender : " + userMessage, "text/html");
					multipart.addBodyPart(messageBodyP);
					replyMessage.setContent(multipart);

				} catch (Exception e) {
					e.printStackTrace();
				}

				Transport t = session.getTransport("smtp");
				try {

					t.connect(serverInfo.getMail_serverName(), serverInfo.getMail_password());
					t.sendMessage(replyMessage, replyMessage.getAllRecipients());
				} finally {
					t.close();
				}
				System.out.println("message forwarded successfully ....");

				store.close();

			} catch (Exception e) {
				e.printStackTrace();
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<UserModel> getListOfUserByCompanyId(int id) {

		org.hibernate.Session session = sessionFactory.openSession();
		try {
			Company company = new Company();
			company.setCompanyId(id);
			Criteria criteria = session.createCriteria(UserModel.class);
			criteria.add(Restrictions.eq("company", company));
			List<UserModel> userModels = criteria.list();
			return userModels;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public List<UserModel> getListOfUserByCompanyIdWhereDeviceTokenIsAvailable(Company company) {

		org.hibernate.Session session = sessionFactory.openSession();
		try {
			
			String hql = "from UserModel where company=:company AND deviceToken is not null ";
			Query query = session.createQuery(hql);
			query.setParameter("company", company);
			List<UserModel> userModels = query.list();
		
			return userModels;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			session.close();
		}
		return null;
	}

	public String saveReplyedChattingForFeedCom(String soNumber, String subject, List<String> Attachment,
			String chatBox, String feedbackType, String oper, UserModel userFrom, UserModel userTo,
			UserModel userMessage) {
		org.hibernate.Session session = sessionFactory.openSession();
		try {

			Status status2 = new Status();
			status2.setStatusId(1);

			Criteria criteria = session.createCriteria(FeedBackTypeTable.class);
			List<FeedBackTypeTable> list = criteria.list();
			FeedBackTypeTable typeTable = null;
			for (FeedBackTypeTable table : list) {
				if (table.getFeedbackType().equalsIgnoreCase(feedbackType)) {
					typeTable = table;
					break;
				}
			}

			// if feedback type is report then save a copy of it in Reports
			// section

			if (typeTable.getFeedbackType().equalsIgnoreCase("Report")) {
				// check allfileNames not null
				if (typeTable != null) {
					for (String fileName : Attachment) {

						Report box = new Report();
						box.setName(fileName);
						box.setUserModelFrom(userFrom);
						box.setUserModelTo(userTo);
						box.setUserfeedback(userMessage);
						box.setDate(new Date());
						saveFIleDetails(box);

					}
				}
			}
			if (typeTable.getFeedbackType().equalsIgnoreCase("Document")) {
				// check allfileNames not null
				if (typeTable != null) {
					for (String fileName : Attachment) {

						Documents box = new Documents();
						box.setName(fileName);
						box.setUserModelFrom(userFrom);
						box.setUserModelTo(userTo);
						box.setUserfeedback(userMessage);
						box.setDate(new Date());
						saveFIleDetails(box);

					}
				}
			}

			String hql = "select max(f.feedbackCounter) from FeedBackChatingCounter f where f.so_number =:so_number AND f.feedBackType =:feedBackType "
					+ " AND f.userModelFrom =:userModelFrom AND f.userModelTo=:userModelTo";
			Query query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", userFrom);
			query.setParameter("userModelTo", userTo);
			List results = query.list();

			query = session.createQuery(hql);
			query.setParameter("feedBackType", typeTable);
			query.setParameter("so_number", soNumber);
			query.setParameter("userModelFrom", userTo);
			query.setParameter("userModelTo", userFrom);
			List results1 = query.list();

			/*
			 * int userf=Integer.parseInt(results.get(0)+""); int
			 * usert=Integer.parseInt(results1.get(0)+"");
			 */

			int usert = 0;
			int userf = 0;
			try {
				usert = Integer.parseInt(results1.get(0) + "");
			} catch (Exception e) {
				e.printStackTrace();
			}
			try {
				userf = Integer.parseInt(results.get(0) + "");
			} catch (Exception e) {
				e.printStackTrace();
			}

			FeedBackChatingCounter FeedchatingCount = new FeedBackChatingCounter();
			int count = 0;
			if (userf == usert) {
				count = userf + 1;
				// FeedchatingCount.setFeedbackCounter(count);
				FeedchatingCount.setSo_number(soNumber);
				FeedchatingCount.setFeedBackType(typeTable);
				FeedchatingCount.setUserModelFrom(userFrom);
				FeedchatingCount.setUserModelTo(userTo);
				FeedchatingCount.setCount(1);
			} else if (userf > usert) {
				criteria = session.createCriteria(FeedBackChatingCounter.class);
				criteria.add(Restrictions.eq("feedbackCounter", userf));
				FeedBackChatingCounter chatingCounter = (FeedBackChatingCounter) criteria.uniqueResult();
				count = userf + 1;
				// FeedchatingCount.setFeedbackCounter(count);
				FeedchatingCount.setSo_number(chatingCounter.getSo_number());
				FeedchatingCount.setFeedBackType(chatingCounter.getFeedBackType());
				FeedchatingCount.setUserModelFrom(userFrom);
				FeedchatingCount.setUserModelTo(userTo);
				FeedchatingCount.setCount(chatingCounter.getCount() + 1);

			} else if (usert > userf) {
				criteria = session.createCriteria(FeedBackChatingCounter.class);
				criteria.add(Restrictions.eq("feedbackCounter", usert));
				FeedBackChatingCounter chatingCounter = (FeedBackChatingCounter) criteria.uniqueResult();
				count = usert + 1;
				// FeedchatingCount.setFeedbackCounter(count+1);
				FeedchatingCount.setSo_number(chatingCounter.getSo_number());
				FeedchatingCount.setFeedBackType(chatingCounter.getFeedBackType());
				FeedchatingCount.setUserModelFrom(userFrom);
				FeedchatingCount.setUserModelTo(userTo);
				FeedchatingCount.setCount(chatingCounter.getCount() + 1);
			}

			criteria = session.createCriteria(Operator.class);
			List<Operator> listOperator = criteria.list();
			Operator operator = null;

			try {
				operator = getOperatorByUserName(oper);
			} catch (Exception e) {
				e.printStackTrace();
			}

			/*
			 * for (Operator table : listOperator) { if
			 * (table.getUserName().equalsIgnoreCase(oper)) { operator = table;
			 * break; } }
			 */

			Transaction transaction = session.beginTransaction();
			session.save(FeedchatingCount);

			String sattachment = "";
			for (String string : Attachment) {
				sattachment = sattachment + string + "#@$";
			}

			final Feedback feedback = new Feedback();
			feedback.setFeedbackText(chatBox);
			feedback.setCounter(FeedchatingCount);
			feedback.setSoNumber(soNumber);
			feedback.setFeedBackType(typeTable);
			try {
				feedback.setOperator(operator);
			} catch (Exception e) {
				e.printStackTrace();
			}

			feedback.setFdate(new Date());
			feedback.setAttachment(sattachment);
			feedback.setEmailSubject(subject);
			feedback.setStatus(status2);
			feedback.setUserModelFrom(userFrom);
			feedback.setUserModelTo(userTo);

			// set user message according to condition
			if (userMessage != null) {
				feedback.setUserfeedback(userMessage);
			} else {
				feedback.setUserfeedback(userFrom);
			}
			transaction.commit();
			session.close();

			org.hibernate.Session session1 = sessionFactory.openSession();
			Transaction transaction1 = session1.beginTransaction();
			session1.save(feedback);

			transaction1.commit();
			session1.close();
			// save preferance according to date

			saveFeedBackSonumberSortingByDate(feedback);
			
			new Thread() {
				public void run() {
					sendFcmNotification(feedback);

				}
			}.start();

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
		}
		return "";

	}

	private void saveFeedBackSonumberSortingByDate(Feedback feedback) {

		org.hibernate.Session session = sessionFactory.openSession();
		Transaction transaction = session.beginTransaction();
		Criteria criteria = session.createCriteria(FeedBackSonumberSortingByDate.class);
		criteria.add(Restrictions.eq("soNumber", feedback.getSoNumber()));
		criteria.add(Restrictions.eq("feedBackType", feedback.getFeedBackType()));
		System.out.println("feed date " + feedback.getFdate());
		try {
			FeedBackSonumberSortingByDate sortingByDate = (FeedBackSonumberSortingByDate) criteria.uniqueResult();
			if (sortingByDate != null) {
				sortingByDate.setFdate(feedback.getFdate());
				sortingByDate.setSoNumber(feedback.getSoNumber());
				sortingByDate.setFeedBackType(feedback.getFeedBackType());
				sortingByDate.setUserModelFrom(feedback.getUserModelFrom());
				sortingByDate.setUserModelTo(feedback.getUserModelTo());
				session.saveOrUpdate(sortingByDate);
			} else {
				sortingByDate = new FeedBackSonumberSortingByDate();
				sortingByDate.setFdate(feedback.getFdate());
				sortingByDate.setSoNumber(feedback.getSoNumber());
				sortingByDate.setFeedBackType(feedback.getFeedBackType());
				sortingByDate.setUserModelFrom(feedback.getUserModelFrom());
				sortingByDate.setUserModelTo(feedback.getUserModelTo());
				sortingByDate.setFeedback(feedback);
				session.saveOrUpdate(sortingByDate);

			}
			transaction.commit();
			session.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public List<String> getListOfUserEmailIdByCompanyId(Company company) {

		org.hibernate.Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(UserModel.class);
		criteria.add(Restrictions.eq("company", company));
		criteria.setProjection(Projections.property("email"));
		List<String> list = new ArrayList<String>();
		list = criteria.list();
		session.close();
		return list;
	}

	public FeedBackTypeTable getFeedBackTypeObjectByFeedBackId(Integer feedbackId) {

		org.hibernate.Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(FeedBackTypeTable.class);
		criteria.add(Restrictions.eq("id", feedbackId));
		FeedBackTypeTable feedBackTypeTable = (FeedBackTypeTable) criteria.uniqueResult();

		session.close();
		return feedBackTypeTable;
	}

	public void saveFIleDetails(Documents fileName) {

		org.hibernate.Session session = sessionFactory.openSession();
		session.save(fileName);
		Transaction t = session.beginTransaction();
		t.commit();
		session.close();
	}

	/*
	 * public void saveFIleDetailsdemm(DropBox fileName) { org.hibernate.Session
	 * session=sessionFactory.openSession(); Criteria criteria =
	 * session.createCriteria( Feedback.class ); criteria.createCriteria(
	 * "userModelFrom", "a"); criteria.add( Restrictions.eq( "a.userName",
	 * "ssi")); criteria.list(); }
	 */

	public void saveFIleDetails(Report fileName) {

		org.hibernate.Session session = sessionFactory.openSession();
		session.save(fileName);
		Transaction t = session.beginTransaction();
		t.commit();
		session.close();
	}

	public List<EmailSendReceivePermission> getEmailPermissionUserListAccordingToCompany(String companyId) {
		org.hibernate.Session session = sessionFactory.openSession();
		Company company = new Company();
		company.setCompanyId(Integer.parseInt(companyId));
		List<EmailSendReceivePermission> emailSendReceivePermissions = null;
		try {
			Criteria criteria = session.createCriteria(EmailSendReceivePermission.class);
			criteria.add(Restrictions.eq("company", company));

			emailSendReceivePermissions = criteria.list();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			session.close();
		}
		return emailSendReceivePermissions;
	}

	public void SaveEmailPermissionByUserId(int id) {
		// TODO Auto-generated method stub

		EmailSendReceivePermission email = new EmailSendReceivePermission();
		org.hibernate.Session session = sessionFactory.openSession();

		UserModel user = (UserModel) session.get(UserModel.class, id);
		Criteria criteria = session.createCriteria(EmailSendReceivePermission.class);
		criteria.add(Restrictions.eq("userModel", user));

		if (criteria.list().size() > 0) {

		} else {
			Transaction t = session.beginTransaction();
			email.setCompany(user.getCompany());
			email.setUserModel(user);
			session.saveOrUpdate(email);
			t.commit();
		}

		session.close();

	}

	public String getUrlForFileUploadAndDownload() {

		String path = "";
		org.hibernate.Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(UrlForFileUploadDownload.class);

		if (criteria.list().size() > 0) {

			List<UrlForFileUploadDownload> fileUploadDownloads = criteria.list();
			path = fileUploadDownloads.get(0).getUrlName();
		}
		session.close();
		return path;

	}

	public String sendNewEmail(ArrayList<String> forSaveAllfileNames, List<String> cfsemailList,
			List<String> ssiemailList, String subject, String body, String excludefileFromSending) {

		Properties properties = new Properties();
		final ServerInfo serverInfo = getServerInfo();

		properties.setProperty("mail.host", serverInfo.getMail_host());
		properties.setProperty("mail.port", serverInfo.getMail_port());
		properties.setProperty("mail.transport.protocol", "imaps");

		// properties.put("mail.pop3.starttls.enable", "true");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.starttls.enable", "true");
		properties.put("mail.smtp.host", serverInfo.getMail_host());
		properties.put("mail.smtp.port", serverInfo.getMail_port());

		Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(serverInfo.getMail_serverName(), serverInfo.getMail_password());
			}
		});
		Store store = null;
		Folder inbox = null;

		/*
		 * int indexofCfsFiles =
		 * excludefileFromSending.lastIndexOf("CfsFiles\\");
		 * 
		 * String filename = excludefileFromSending.substring(indexofCfsFiles +
		 * 9, excludefileFromSending.length());
		 * 
		 * if (forSaveAllfileNames != null) { try {
		 * forSaveAllfileNames.remove(filename); } catch (Exception e) {
		 * e.printStackTrace(); } }
		 */

		try {
			Message replyMessage = new MimeMessage(session);
			replyMessage.setContent(body, "text/html; charset=UTF-8");

			replyMessage.setFrom(new InternetAddress(serverInfo.getMail_serverName(), serverInfo.getMsgName()));
			// replyMessage.setText(ContentsOfMessage);

			replyMessage.setContent(body, "text/html");
			String[] TO = cfsemailList.toArray(new String[cfsemailList.size()]);
			StringBuffer bufTo = new StringBuffer(Arrays.toString(TO));
			try {
				bufTo.deleteCharAt(0);
				bufTo.deleteCharAt(bufTo.length() - 1);
			} catch (Exception e) {
				e.printStackTrace();
			}

			String[] CC = ssiemailList.toArray(new String[ssiemailList.size()]);
			StringBuffer bufCC = new StringBuffer(Arrays.toString(CC));
			try {
				bufCC.deleteCharAt(0);
				bufCC.deleteCharAt(bufCC.length() - 1);
			} catch (Exception e) {
				e.printStackTrace();
			}

			replyMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(bufTo.toString(), false));
			replyMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(bufCC.toString(), false));
			replyMessage.setSubject(subject);

			store = session.getStore("imaps");
			store.connect();
			inbox = store.getFolder("INBOX");
			inbox.open(Folder.READ_WRITE);

			// replyMessage.setContent(message, "message/rfc822");

			try {

				BodyPart messageBodyPart = new MimeBodyPart();

				messageBodyPart.setText(body);
				// Create a multipar message
				Multipart multipart = new MimeMultipart();

				// add attachments
				FTPClient ftpClient = new FTPClient();
				FtpInfo ftpInfo = getFtpInfo();

				String server = ftpInfo.getServerName();
				int port = ftpInfo.getPort();

				String user = ftpInfo.getUserName();
				String pass = ftpInfo.getPassword();

				ftpClient.connect(server, port);
				ftpClient.login(user, pass);
				ftpClient.enterLocalPassiveMode();
				ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
				InputStream inputStream = null;

				for (String str : forSaveAllfileNames) {
					MimeBodyPart messageBodyP = new MimeBodyPart();
					inputStream = ftpClient.retrieveFileStream("/TEST/" + str);
					DataSource source = new ByteArrayDataSource(inputStream, "application/octet-stream");
					messageBodyP.setDataHandler(new DataHandler(source));
					messageBodyP.setFileName(str.substring(13, str.length()));
					multipart.addBodyPart(messageBodyP);
					try {

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
				}
				MimeBodyPart messageBodyP = new MimeBodyPart();
				messageBodyP.setContent(body, "text/html");
				multipart.addBodyPart(messageBodyP);
				replyMessage.setContent(multipart);
			} catch (Exception e) {
				e.printStackTrace();
			}

			// Send the message by authenticating the SMTP server
			// Create a Transport instance and call the sendMessage
			Transport t = session.getTransport("smtp");
			try {
				// connect to the smpt server using transport instance
				// change the user and password accordingly

				System.out.println(serverInfo.getMail_serverName() + "  serverInfo.getMail_serverName()");

				t.connect(serverInfo.getMail_host(), serverInfo.getMail_serverName(), serverInfo.getMail_password());
				replyMessage.saveChanges();
				t.sendMessage(replyMessage, replyMessage.getAllRecipients());

				// store message in inbox
				replyMessage.setFlag(Flags.Flag.SEEN, true);
				Message[] messages = new Message[1];
				messages[0] = replyMessage;
				inbox.appendMessages(messages);

				inbox.close(false);
				store.close();
			} finally {
				t.close();

			}
			System.out.println("message replied successfully ....");

		} catch (Exception e) {
			e.printStackTrace();
			// return e.toString()+"email";
		}
		return "EMail Sent Successfully!!";
	}

	public List<Company> getListCompany() {
		org.hibernate.Session session = sessionFactory.openSession();
		List<Company> company = null;
		try {
			Criteria criteria = session.createCriteria(Company.class);
			company = criteria.list();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			session.close();
		}
		return company;
	}

	public UserModel getClientCompanyAdminByCompany(Company company) {
		org.hibernate.Session session = sessionFactory.openSession();

		UserModel userModel = null;
		RoleModel roleModel = new RoleModel();
		roleModel.setId(1);
		try {
			Criteria criteria = session.createCriteria(UserModel.class);
			criteria.add(Restrictions.eq("company", company));
			criteria.add(Restrictions.eq("userRoll", roleModel));

			userModel = (UserModel) criteria.uniqueResult();
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			session.close();
		}
		return userModel;
	}

	public Company getClientCompanyObjectCompanyName(String company) {
		company = company.trim();
		org.hibernate.Session session = sessionFactory.openSession();
		Company comapnyobj = null;
		try {

			Query query = session.createQuery("from Company where companyName =:companyName ");
			query.setParameter("companyName", company);
			List<Company> results = query.list();
			comapnyobj = results.get(0);
		} catch (Exception exception) {
			exception.printStackTrace();
		} finally {
			session.close();
		}
		return comapnyobj;
	}

	public FtpInfo getFtpInfo() {
		org.hibernate.Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(FtpInfo.class);
		List<FtpInfo> ftpInfos = criteria.list();
		session.close();
		return ftpInfos.get(0);
	}

	public ServerInfo getServerInfo() {
		org.hibernate.Session session = sessionFactory.openSession();
		Criteria criteria = session.createCriteria(ServerInfo.class);
		List<ServerInfo> ftpInfos = criteria.list();
		session.close();
		return ftpInfos.get(0);
	}

	public Operator getOperatorByUserName(String userName) {
		org.hibernate.Session session = sessionFactory.openSession();
		try {
			Criteria criteria = session.createCriteria(Operator.class);
			criteria.add(Restrictions.eq("userName", userName));
			List<Operator> operator = (List<Operator>) criteria.list();
			System.out.println("userName     " + userName);
			int flag = 0;
			try {
				if (operator.size() > 0) {
					System.out.println("size ------------------------" + operator.size());
					session.close();
					return operator.get(0);
				} else {
					Transaction tx = session.beginTransaction();
					Operator op = new Operator();
					op.setUserName(userName);
					op.setStatus("Active");
					session.save(op);
					tx.commit();
					operator = new ArrayList<Operator>();
					operator.add(op);
				}
			} catch (Exception e) {

			}
			session.close();
			return operator.get(0);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}