package com.xanadutec.coreflex.feedcomWebServices;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.xanadutec.coreflex.model.FeedBackCounterForHome;
import com.xanadutec.coreflex.model.FeedBackSonumberSortingByDate;
import com.xanadutec.coreflex.model.FeedBackTypeTable;
import com.xanadutec.coreflex.model.Feedback;
import com.xanadutec.coreflex.model.UserModel;

public interface FeedcomWebServicesService {

	List<FeedBackCounterForHome> webServiceGetfeedBackCounterForHome(List<FeedBackTypeTable> feedBackTypeTables,
			UserModel userFrom, UserModel userTo);

	List<FeedBackSonumberSortingByDate> getUpdatedSortedFeedBackList(UserModel userFrom, UserModel clientCompanyAdmin);

	List<Feedback> getListOfChatingUsingSoNumberAndFeedBackType(String soNumber, FeedBackTypeTable feedBackTypeTable,
			UserModel userFrom, UserModel clientCompanyAdmin);
	
	public List<FeedBackSonumberSortingByDate> getUpdatedSortedFeedBackListWithFeedbackType(UserModel userFrom,
			UserModel clientCompanyAdmin,FeedBackTypeTable feedBackTypeTable);
}
