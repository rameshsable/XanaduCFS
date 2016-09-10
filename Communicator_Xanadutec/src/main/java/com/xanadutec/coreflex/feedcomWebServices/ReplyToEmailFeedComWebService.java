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

import org.springframework.beans.factory.annotation.Autowired;

import com.xanadutec.coreflex.EmailPermission.EmailSendAndReceivePermissionService;
import com.xanadutec.coreflex.model.ServerInfo;
import com.xanadutec.coreflex.model.UserModel;

public class ReplyToEmailFeedComWebService {

public void sendEmailReply(String actualSubject ,List<String> emailTo,List<String> emailCC ,String attachment,String ContentsOfMessage, EmailSendAndReceivePermissionService emailSendAndReceivePermissionService){

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

          for (int i = 0, n = messages.length; i < n; i++) {
             Message message = messages[i];
             date = message.getSentDate();
             
             Address[] msgfrom = message.getFrom();
           
             
             /*emailCC.remove(userModel.getEmail());
             emailTo.remove(userModel.getEmail());*/
             
             String[] TO=  emailTo.toArray(new String[emailTo.size()]);
             StringBuffer bufTo = new StringBuffer(Arrays.toString(TO));
             try{
             bufTo.deleteCharAt(0);
             bufTo.deleteCharAt(bufTo.length()-1);
             }catch(Exception e){
             	
             }
             
             String[]CC= emailCC.toArray(new String[emailCC.size()]);
             StringBuffer bufCC = new StringBuffer(Arrays.toString(CC));
             try{
             bufCC.deleteCharAt(0);
             bufCC.deleteCharAt(bufCC.length()-1);
         	}catch(Exception e){
         		
         	}
             
             
             
             // Get all the information from the message
            // String from = InternetAddress.toString(message.getFrom());
             String from = serverInfo.getMail_serverName();
             String replyTo = InternetAddress.toString(message.getReplyTo());
             String to = InternetAddress.toString(message.getRecipients(Message.RecipientType.TO));
             String subject = message.getSubject();
             Date sent = message.getSentDate();
             if (sent != null) {
                System.out.println("Sent: " + sent);
             }
             try{
            	 
            	 System.out.println("actualSubject  "+actualSubject+"  subject "+subject);
            	 if(subject.equalsIgnoreCase(actualSubject)){
                        Message replyMessage = new MimeMessage(session);
                        replyMessage = (MimeMessage) message.reply(false);
                        replyMessage.setFrom(new InternetAddress(from,serverInfo.getMsgName()));
                        replyMessage.setSubject(subject);
                        replyMessage.setContent(ContentsOfMessage,"text/html");
                        replyMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(bufTo.toString(), false));
                        replyMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(bufCC.toString(), false));
                        try{
                        	
                        	BodyPart messageBodyPart = new MimeBodyPart();

                              // Now set the actual message
                           //   messageBodyPart.setText("ContentsOfMessage");
                        	messageBodyPart.setText(ContentsOfMessage);
                              // Create a multipar message
                              Multipart multipart = new MimeMultipart();

                              // Set text message part
                              /*multipart.addBodyPart(messageBodyPart);*/

                              List<String> attach=new ArrayList<String>();
                              
                              StringTokenizer iterator=new StringTokenizer(attachment, "#@$");
                              while ( iterator.hasMoreElements()) {
                            	  attach.add((String) iterator.nextElement());
                        	}
                              String pathact="";
                              
                              try{
                      		//	uploadDownloadService.saveUrlForFileUploadAndDownload(pathact);
                      		}catch(Exception e){
                      			e.printStackTrace();
                      		}
                              File dir1 = new File(pathact);

                                //add attachments    	
                              for (String str : attach) {
                            	    MimeBodyPart messageBodyP = new MimeBodyPart();
                            	    DataSource source = new FileDataSource(dir1.getAbsolutePath()+File.separator+str);
                            	    messageBodyP.setDataHandler(new DataHandler(source));
                            	    messageBodyP.setFileName(source.getName());
                            	    multipart.addBodyPart(messageBodyP);
                            	}
                              
                              MimeBodyPart messageBodyP = new MimeBodyPart();
                              messageBodyP.setContent(ContentsOfMessage,"text/html");
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
        	             t.connect(serverInfo.getMail_serverName(),
        	            		 serverInfo.getMail_password());
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
                    } 
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