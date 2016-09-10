package com.xanadutec.coreflex.feedcomWebServices;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.xanadutec.coreflex.EmailPermission.EmailSendAndReceivePermissionService;
import com.xanadutec.coreflex.model.ServerInfo;


public class ReplyToEmailMom {
	
	public void sendEmailReply(String actualSubject ,List<String> emailList, String attendee,String keyPoints,String createdDateJson,String submittedDateTimeJson, String creator,  HttpServletRequest request, EmailSendAndReceivePermissionService emailSendAndReceivePermissionService){

		//subject,emailList,attendee, keyPoints,createdDateJson
		
		
		
		/*String MsgText="Weekly Feedback Meeting"+"\n"+createdDateJson+"\n"
				+"Created By :- ramesh"+"\n Attendee :- \n"+attendee+"\n Key Points:-\n"+keyPoints;
		
		System.out.println(MsgText);*/
		
		
		String MsgText = "<i>Weekly Feedback Meeting!</i><br>";
		MsgText += "<b>"+createdDateJson+"</b><br>";
		MsgText += "<b>Created By :</b>"+creator +"<br>";
		MsgText +=submittedDateTimeJson+"<br>";
		MsgText += "<b>Attendee:</b>"+attendee +"<br>";
		MsgText += "<b>Key points:</b>"+keyPoints +"<br>";
      
		
	    Date date = null;

	    ServerInfo serverInfo=emailSendAndReceivePermissionService .getServerInfo();
		Properties properties = new Properties();
		properties.setProperty("mail.host", serverInfo.getMail_host());
		properties.setProperty("mail.port", serverInfo.getMail_port());
		properties.setProperty("mail.transport.protocol", "imaps");
		properties.put("mail.smtp.auth", "true");
		properties.put("mail.smtp.host", serverInfo.getMail_host());
		properties.put("mail.smtp.port", serverInfo.getMail_port());
		Session session = Session.getDefaultInstance(properties);


	    
	    System.out.println("ramesh one");
	    
	    try 
	    {
	       // Get a Store object and connect to the current host
	       Store store = session.getStore("imaps");
	       store.connect(serverInfo.getMail_serverName(),serverInfo.getMail_password());//change the user and password accordingly
	       Folder folder = store.getFolder("inbox");
	       if (!folder.exists()) {
	          System.out.println("inbox not found");
	             System.exit(0);
	       }
	       folder.open(Folder.READ_ONLY);

	       BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
	       Message[] messages = folder.getMessages();
	       if (messages.length != 0) {

	    	   System.out.println("ramesh two");
	          for (int i = 0, n = messages.length; i < n; i++) {
	             Message message = messages[i];
	             date = message.getSentDate();
	             System.out.println("ramesh three");
	             
	             Address[] msgfrom = message.getFrom();
	             System.out.println("ramesh 5");
	           
	             
	             HttpSession	 httpSession=request.getSession();
	             System.out.println("ramesh 6");
	             /*emailCC.remove(userModel.getEmail());
	             emailTo.remove(userModel.getEmail());*/
	             
	             String[] TO=  emailList.toArray(new String[emailList.size()]);
	             StringBuffer bufTo = new StringBuffer(Arrays.toString(TO));
	             try{
	             bufTo.deleteCharAt(0);
	             bufTo.deleteCharAt(bufTo.length()-1);
	             }catch(Exception e){
	             	
	             }
	             
	             
	             System.out.println("ramesh 7");
	             
	             // Get all the information from the message
	            // String from = InternetAddress.toString(message.getFrom());
	             String from = serverInfo.getMail_serverName();
	             String replyTo = InternetAddress.toString(message.getReplyTo());
	             String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
	             String subject = message.getSubject();
	             Date sent = message.getSentDate();
	             System.out.println("ramesh 8");
	             if (sent != null) {
	                System.out.println("Sent: " + sent);
	             }
	             try{
	            	 
	            	 
	                        Message replyMessage = new MimeMessage(session);
	                        replyMessage = (MimeMessage) message.reply(false);
	                        replyMessage.setFrom(new InternetAddress(from,serverInfo.getMsgName()));
	                        replyMessage.setSubject(actualSubject);
	                        
	                        replyMessage.setContent(MsgText,"text/html");
	                        replyMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(bufTo.toString(), false));
	                        //replyMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(bufCC.toString(), false));
	                        try{
	                        	
	                        	BodyPart messageBodyPart = new MimeBodyPart();

	                              // Now set the actual message
	                           //   messageBodyPart.setText("ContentsOfMessage");
	                        	
	                        	messageBodyPart.setText(MsgText);
	                              // Create a multipar message
	                              Multipart multipart = new MimeMultipart();

	                              // Set text message part
	                              /*multipart.addBodyPart(messageBodyPart);*/

	                             
	                              
	                              
	                              
	                              MimeBodyPart messageBodyP = new MimeBodyPart();
	                              messageBodyP.setContent(MsgText,"text/html");
	                              multipart.addBodyPart(messageBodyP);
	                              
	                          replyMessage.setContent(multipart);
	                        
	                        }catch(Exception e){
	                        	e.printStackTrace();
	                        }
	                       // Send the message by authenticating the SMTP server
	                        // Create a Transport instance and call the sendMessage
	                        Transport t = session.getTransport("smtp");
	                        try {
	        	   	     //connect to the smpt server using transport instance
	        		     //change the user and password accordingly	
	        	             t.connect(serverInfo.getMail_serverName(),serverInfo.getMail_password());
	        	             t.sendMessage(replyMessage,
	                              replyMessage.getAllRecipients());
	                        } finally {
	                           t.close();
	                        }
	                        System.out.println("message replied successfully ....");

	                        // close the store and folder objects
	                        folder.close(false);
	                        store.close();
	                        break;
	                    
	             }catch(Exception e){
	            	 e.printStackTrace();
	             }
	          }//end of for loop

	       } else {
	          System.out.println("There is no msg....");
	       }

	    } catch (Exception e) {
	       e.printStackTrace();
	    }

	 
	}
	
	

}
