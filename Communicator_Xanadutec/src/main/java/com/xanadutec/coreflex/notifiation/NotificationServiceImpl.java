package com.xanadutec.coreflex.notifiation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xanadutec.coreflex.model.Documents;
import com.xanadutec.coreflex.model.FcmNotification;
import com.xanadutec.coreflex.model.Feedback;
import com.xanadutec.coreflex.model.Mom;
import com.xanadutec.coreflex.model.Report;
import com.xanadutec.coreflex.model.UserModel;

@Service("NotificationService")
public class NotificationServiceImpl implements NotificationService {

	@Autowired
	private NotificationDao notificationDao;

	@Override
	public FcmNotification getFcmNotification() {
		// TODO Auto-generated method stub
		return notificationDao.getFcmNotification();

	}

	@Override
	public void sendReportFcmNotification(Report report,
			FcmNotification fcmNotification, List<UserModel> userModels,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		notificationDao.sendReportFcmNotification(report, fcmNotification,
				userModels, request);
	}

	@Override
	public void sendDocumentFcmNotification(Documents documents,
			FcmNotification fcmNotification, List<UserModel> userModels,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		notificationDao.sendDocumentFcmNotification(documents, fcmNotification,
				userModels, request);

	}

	@Override
	public void sendMomFcmNotification(Mom mom,
			FcmNotification fcmNotification, List<UserModel> userModels,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		notificationDao.sendMomFcmNotification(mom, fcmNotification,
				userModels, request);
	}

	@Override
	public void sendFeedbackNotification(Feedback feedback,
			FcmNotification fcmNotification, List<UserModel> userModels,
			HttpServletRequest request) {
		// TODO Auto-generated method stub
		notificationDao.sendFeedbackNotification(feedback, fcmNotification,
				userModels, request);
	}

}
