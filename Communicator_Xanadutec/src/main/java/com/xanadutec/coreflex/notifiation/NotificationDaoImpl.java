package com.xanadutec.coreflex.notifiation;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.xanadutec.coreflex.model.Documents;
import com.xanadutec.coreflex.model.FcmNotification;
import com.xanadutec.coreflex.model.Feedback;
import com.xanadutec.coreflex.model.Mom;
import com.xanadutec.coreflex.model.Report;
import com.xanadutec.coreflex.model.UserModel;


@Repository("NotificationDao")
public class NotificationDaoImpl implements NotificationDao {

	@Autowired
	private SessionFactory sessionFactory;

	public FcmNotification getFcmNotification() {
		// TODO Auto-generated method stub
		try{
			Session session = sessionFactory.openSession();
			Criteria criteria = session.createCriteria(FcmNotification.class);
			List<FcmNotification> fcmNotifications = criteria.list();
			session.close();
			return fcmNotifications.get(0);	
		}catch(Exception e){
			e.printStackTrace();
		}
		return null;
	}


	@Override
	public void sendReportFcmNotification(Report report,
			FcmNotification fcmNotification,List<UserModel> userModels, HttpServletRequest request) {

		try {

			for (UserModel userModel : userModels) {
				try {
					JSONObject body = new JSONObject();
					String CLIENT_TOKEN = userModel.getDeviceToken();// "cVVPt5vJizI:APA91bFOgGjOd1gDaU7GEyftM5P-FP7wekoMXgh0ODJqlc_SxZGulwCwAk6C6iyLaXp5TTWWqdo2B12ps7zvNMP6JU4pknn_MATPPiiA-zNNNlMF6jg_hJQ_FNObzRxT5LySKzeFamB5";
																		// //
																		// https://developers.google.com/instance-id/
					body.put("to", CLIENT_TOKEN);
					body.put("priority", "high");
					
					JSONObject bodyText = new JSONObject();
					bodyText.put("ReportId", report.getId());
				

					JSONObject notification = new JSONObject();
					notification.put("body", bodyText.toString());
					notification.put("title", "CoreFlex Communicator");
					notification.put("sound", "default");
					notification.put("priority", "high");
					notification.put("click_action", "OPEN_ACTIVITY_1");
					//notification.put("Authorization", fcmNotification.getAuthorization());

					// notification.put("icon", "myicon");

					JSONObject data = new JSONObject();
					data.put("id", "1");

					body.put("notification", notification);
					body.put("data", data);

					data.put("valueOfData", bodyText.toString());
					
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


	@Override
	public void sendDocumentFcmNotification(Documents documents,
			FcmNotification fcmNotification, List<UserModel> userModels,
			HttpServletRequest request) {

		try {

			for (UserModel userModel : userModels) {
				try {
					JSONObject body = new JSONObject();
					String CLIENT_TOKEN = userModel.getDeviceToken();// "cVVPt5vJizI:APA91bFOgGjOd1gDaU7GEyftM5P-FP7wekoMXgh0ODJqlc_SxZGulwCwAk6C6iyLaXp5TTWWqdo2B12ps7zvNMP6JU4pknn_MATPPiiA-zNNNlMF6jg_hJQ_FNObzRxT5LySKzeFamB5";
																		// //
																		// https://developers.google.com/instance-id/
					body.put("to", CLIENT_TOKEN);
					body.put("priority", "high");
				//	body.put("click_action", "OPEN_ACTIVITY_1");

					JSONObject bodyText = new JSONObject();
					bodyText.put("DocumentId", documents.getId());
				

					JSONObject notification = new JSONObject();
					notification.put("body", bodyText.toString());
					notification.put("title", "CoreFlex Communicator");
					notification.put("sound", "default");
					notification.put("priority", "high");
					notification.put("click_action", "OPEN_ACTIVITY_1");

					// notification.put("icon", "myicon");

					JSONObject data = new JSONObject();
					data.put("id", "1");

					body.put("notification", notification);
					body.put("data", data);

					data.put("valueOfData", bodyText.toString());
					
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


	@Override
	public void sendMomFcmNotification(Mom mom,
			FcmNotification fcmNotification, List<UserModel> userModels,
			HttpServletRequest request) {
		
		System.out.println("notification mom " +userModels.size());

		try {

			for (UserModel userModel : userModels) {
				try {
					JSONObject body = new JSONObject();
					String CLIENT_TOKEN = userModel.getDeviceToken();// "cVVPt5vJizI:APA91bFOgGjOd1gDaU7GEyftM5P-FP7wekoMXgh0ODJqlc_SxZGulwCwAk6C6iyLaXp5TTWWqdo2B12ps7zvNMP6JU4pknn_MATPPiiA-zNNNlMF6jg_hJQ_FNObzRxT5LySKzeFamB5";
																		// //
																		// https://developers.google.com/instance-id/
					body.put("to", CLIENT_TOKEN);
					body.put("priority", "high");
					//body.put("click_action", "OPEN_ACTIVITY_1");

					JSONObject bodyText = new JSONObject();
					bodyText.put("MomId", mom.getId());
				

					JSONObject notification = new JSONObject();
					notification.put("body", bodyText.toString());
					notification.put("title", "CoreFlex Communicator");
					notification.put("sound", "default");
					notification.put("priority", "high");
					notification.put("click_action", "OPEN_ACTIVITY_1");

					// notification.put("icon", "myicon");

					JSONObject data = new JSONObject();
					data.put("id", "1");

					body.put("notification", notification);
					body.put("data", data);

					data.put("valueOfData", bodyText.toString());
					
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


	@Override
	public void sendFeedbackNotification(Feedback feedback,
			FcmNotification fcmNotification, List<UserModel> userModels,
			HttpServletRequest request) {


		try {

			for (UserModel userModel : userModels) {
				try {
					JSONObject body = new JSONObject();
					String CLIENT_TOKEN = userModel.getDeviceToken();// "cVVPt5vJizI:APA91bFOgGjOd1gDaU7GEyftM5P-FP7wekoMXgh0ODJqlc_SxZGulwCwAk6C6iyLaXp5TTWWqdo2B12ps7zvNMP6JU4pknn_MATPPiiA-zNNNlMF6jg_hJQ_FNObzRxT5LySKzeFamB5";
																		// //
																		// https://developers.google.com/instance-id/
					body.put("to", CLIENT_TOKEN);
					body.put("priority", "high");
					//body.put("click_action", "OPEN_ACTIVITY_1");
					JSONObject bodyText = new JSONObject();
					bodyText.put("CompanyFrom", feedback.getUserModelFrom().getUserId());
					bodyText.put("CompanyTo", feedback.getUserModelTo().getUserId());
					bodyText.put("IssueType", feedback.getFeedBackType().getId());
					bodyText.put("SoNumber", feedback.getSoNumber());

					JSONObject notification = new JSONObject();
					notification.put("body", bodyText.toString());
					notification.put("title", "CoreFlex Communicator");
					notification.put("sound", "default");
					notification.put("priority", "high");
					notification.put("click_action", "OPEN_ACTIVITY_1");
					// notification.put("icon", "myicon");

					JSONObject data = new JSONObject();
					data.put("id", "1");

					body.put("notification", notification);
					body.put("data", data);
					
					data.put("valueOfData", bodyText.toString());

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

					System.out.println(body);
					
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




}
