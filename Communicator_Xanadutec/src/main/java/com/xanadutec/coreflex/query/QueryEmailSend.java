package com.xanadutec.coreflex.query;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.Properties;

import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class QueryEmailSend {
	
	public void sendEmail()
	{
		
		String to = "rameshsable15gmail.com";//change accordingly  
	      String from = "cfscommunicator@coreflexsolutions.com";
	     // host = "localhost";//or IP address  
	  
	     /*//Get the session object  
	      Properties properties = System.getProperties();  
	      properties.setProperty("mail.smtp.host", host);*/  
	      
	      Properties properties = new Properties();
	      /*  properties.put("mail.store.protocol", "pop3");
	        properties.put("mail.pop3s.host", "pop.gmail.com");
	        properties.put("mail.pop3s.port", "995");*/
	        
	        properties.setProperty("mail.host", "mail.coreflexsolutions.com");
	        properties.setProperty("mail.port", "110");
	        properties.setProperty("mail.transport.protocol", "imaps");
	        
	        
	      //  properties.put("mail.pop3.starttls.enable", "true");
	        properties.put("mail.smtp.auth", "true");
	      //  properties.put("mail.smtp.starttls.enable", "true");
	        properties.put("mail.smtp.host", "mail.coreflexsolutions.com");
	        properties.put("mail.smtp.port", "25");
	        Session session = Session.getDefaultInstance(properties);

	        System.out.println("1");
	     //compose the message  
	      try{  
	         MimeMessage message = new MimeMessage(session);  
	         System.out.println("2");
	         message.setFrom(new InternetAddress(from));  
	         System.out.println("3");

	         message.addRecipient(Message.RecipientType.TO,new InternetAddress(to));  
	         System.out.println("4");

	         message.setSubject("Ping");  
	         message.setText("Hello, this is example of sending email  ");  
	  
	         // Send message  
	         Transport.send(message);  
	         System.out.println("message sent successfully....");  
	  
	      }catch (MessagingException mex) {mex.printStackTrace();}  
	   }  
		
	}
	


