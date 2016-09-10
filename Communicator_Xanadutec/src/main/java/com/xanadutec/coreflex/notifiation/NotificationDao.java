package com.xanadutec.coreflex.notifiation;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.xanadutec.coreflex.model.Documents;
import com.xanadutec.coreflex.model.FcmNotification;
import com.xanadutec.coreflex.model.Feedback;
import com.xanadutec.coreflex.model.Mom;
import com.xanadutec.coreflex.model.Report;
import com.xanadutec.coreflex.model.UserModel;

public interface NotificationDao {

	public FcmNotification getFcmNotification();

	public void sendReportFcmNotification(Report report,
			FcmNotification fcmNotification, List<UserModel> userModels,
			HttpServletRequest request);

	public void sendDocumentFcmNotification(Documents documents,
			FcmNotification fcmNotification, List<UserModel> userModels,
			HttpServletRequest request);

	public void sendMomFcmNotification(Mom mom,
			FcmNotification fcmNotification, List<UserModel> userModels,
			HttpServletRequest request);

	public void sendFeedbackNotification(Feedback feedback,
			FcmNotification fcmNotification, List<UserModel> userModels,
			HttpServletRequest request);
}
