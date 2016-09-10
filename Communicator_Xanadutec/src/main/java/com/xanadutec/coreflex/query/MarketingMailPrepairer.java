package com.xanadutec.coreflex.query;



import javax.mail.internet.MimeMessage;

import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;

public class MarketingMailPrepairer implements MimeMessagePreparator {

	String emailAddress;
	String message;
	String cc;
	public MarketingMailPrepairer(String  emailAddress, String message){
		this.message=message;
		this.emailAddress=emailAddress;
		System.out.println("dfsfsf nikhil111");
		System.out.println(emailAddress);
	}
	
	@Override
	public void prepare(MimeMessage mimeMessage) throws Exception {
		// TODO Auto-generated method stub
		
		System.out.println("nikihl2322");
		MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

		mimeMessageHelper.addTo(emailAddress);
		//mimeMessageHelper.addCc(cc);
		
		System.out.println("455454");
		//mimeMessageHelper.setTo(bill.getCust().getEmail());
		
		mimeMessageHelper.setSubject("PantuDantu");

		StringBuilder text = new StringBuilder();
		text.append("<html>");
		text.append("<body>");
		
		text.append("<p>" +message +"</p>");
		text.append("</body>");
		text.append("</html>");
		mimeMessageHelper.setText(text.toString(), true);

	}

}
