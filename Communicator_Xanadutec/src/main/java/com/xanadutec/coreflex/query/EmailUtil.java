package com.xanadutec.coreflex.query;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
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
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.NoSuchProviderException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;

import com.xanadutec.coreflex.EmailPermission.EmailSendAndReceivePermissionService;
import com.xanadutec.coreflex.FileUploadDownload.FileUploadService;
import com.xanadutec.coreflex.model.FtpInfo;
import com.xanadutec.coreflex.model.ServerInfo;
 
public class EmailUtil {
 
    
	
	public static String sendEmail(String attachment, String[] clientArray,String[] coreArray,
			String subject, String body,HttpServletRequest request, FileUploadService fileUploadService, EmailSendAndReceivePermissionService emailSendAndReceivePermissionService){
			/*Store store = null;*/
		    Folder folder = null;
		    
		    String reply="";
		    
		    final ServerInfo serverInfo=emailSendAndReceivePermissionService .getServerInfo();
			Properties properties = new Properties();
			properties.setProperty("mail.host", serverInfo.getMail_host());
			properties.setProperty("mail.port", serverInfo.getMail_port());
			properties.setProperty("mail.transport.protocol", "imaps");
			properties.put("mail.smtp.auth", "true");
			properties.put("mail.smtp.host", serverInfo.getMail_host());
			properties.put("mail.smtp.port", serverInfo.getMail_port());
		    
		   /* Session session = Session.getDefaultInstance(properties);*/
		   
		    Store store;
		    Session session = Session.getInstance(properties, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(serverInfo.getMail_serverName(), serverInfo.getMail_password());
				}
		    });
		    try {
				store = session.getStore("imaps");
			
		       store.connect(serverInfo.getMail_serverName(), serverInfo.getMail_password());//change the user and password accordingly
		       folder = store.getFolder("inbox");
		       if (!folder.exists()) {
		          System.out.println("inbox not found");
		             System.exit(0);
		       }
		       
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
        try
        {
               Message replyMessage = new MimeMessage(session);
               replyMessage.setContent(body, "text/html; charset=UTF-8");    
               
               replyMessage.setFrom(new InternetAddress(serverInfo.getMail_serverName(), serverInfo.getMsgName()));
               //replyMessage.setText(ContentsOfMessage);
               replyMessage.setContent(body,"text/html");
               System.out.println(Arrays.toString(clientArray));
               StringBuffer buf = new StringBuffer(Arrays.toString(clientArray));
               buf.deleteCharAt(0);
               buf.deleteCharAt(buf.length()-1);
              
               System.out.println(Arrays.toString(coreArray));
               StringBuffer corBuff=new StringBuffer(Arrays.toString(coreArray));
               corBuff.deleteCharAt(0);
               corBuff.deleteCharAt(corBuff.length()-1);
               
               replyMessage.setRecipients(Message.RecipientType.TO, InternetAddress.parse(buf.toString(), false));
               replyMessage.setRecipients(Message.RecipientType.CC, InternetAddress.parse(corBuff.toString(), false));
               replyMessage.setSubject(subject);
              
               store = session.getStore("imaps");
               store.connect();
               folder.open(Folder.READ_WRITE);
               
  //             replyMessage.setContent(message, "message/rfc822");
               
               try{
               	
               	BodyPart messageBodyPart = new MimeBodyPart();

               	messageBodyPart.setText(body);
                     // Create a multipar message
                     Multipart multipart = new MimeMultipart();

                     // Set text message part
                  /*   multipart.addBodyPart(messageBodyPart);*/

                     List<String> attach=new ArrayList<String>();
                     
                     StringTokenizer iterator=new StringTokenizer(attachment, "#@$");
                     while ( iterator.hasMoreElements()) {
                   	  attach.add((String) iterator.nextElement());
               		
               	}
                     
                     
                     String pathact=request.getServletContext().getRealPath("/resources/CfsFiles");
                 	
         			try{
         			//	uploadDownloadService.saveUrlForFileUploadAndDownload(pathact);
         			}catch(Exception e){
         				reply+=e.getMessage()+"   1";
         				e.printStackTrace();
         			}
                   	File dir1 = new File(pathact);
                       //add attachments    	
                	if (attach.size() >= 0) {
						

						FTPClient ftpClient = new FTPClient();
						try {

							FtpInfo ftpInfo= fileUploadService.getFtpInfo();

							String server = ftpInfo.getServerName();
							int port = ftpInfo.getPort();

							String user = ftpInfo.getUserName();
							String pass = ftpInfo.getPassword();
							
							
							ftpClient.connect(server, port);
							ftpClient.login(user, pass);
							ftpClient.enterLocalPassiveMode();
							ftpClient.setFileType(FTP.BINARY_FILE_TYPE);
							InputStream inputStream = null;

							for (String str : attach) {
								MimeBodyPart messageBodyP = new MimeBodyPart();
								inputStream = ftpClient.retrieveFileStream("/TEST/"+str);
								DataSource source = new ByteArrayDataSource(inputStream,
										"application/octet-stream");
								messageBodyP.setDataHandler(new DataHandler(source));
								messageBodyP.setFileName(str.substring(13,str.length()));
								multipart.addBodyPart(messageBodyP);
							}
							try{
								inputStream.close();	
							}catch(Exception e){
								
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
                	
                	
                     MimeBodyPart messageBodyP = new MimeBodyPart();
                     messageBodyP.setContent(body,"text/html");
                     multipart.addBodyPart(messageBodyP);
                  replyMessage.setContent(multipart);
               }catch(Exception e){
            	reply+=e.getMessage()+"   2";
               	e.printStackTrace();
               }
             
               System.out.println("transaction start");
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
	             
	             // store message in inbox
	             replyMessage.setFlag(Flags.Flag.SEEN, true);
	             Message [] messages = new Message[1];
	             messages[0] = replyMessage;
	             folder.appendMessages(messages);

	             folder.close(false);
	             store.close();
	             System.out.println("transaction end");
               }catch(Exception e){
            	   e.printStackTrace();
               }
               finally {
                  t.close();
                  
                  
               }
               System.out.println("message replied successfully ....");

        }
        catch (Exception e) {
        	reply+=e.getMessage()+"   3";
          e.printStackTrace();
          //return e.toString()+"email";
        }
        return "EMail Sent Successfully!! "+reply;
    }
}