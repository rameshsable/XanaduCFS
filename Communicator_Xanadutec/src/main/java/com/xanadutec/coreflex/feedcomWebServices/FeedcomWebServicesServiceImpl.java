package com.xanadutec.coreflex.feedcomWebServices;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.xanadutec.coreflex.model.FeedBackCounterForHome;
import com.xanadutec.coreflex.model.FeedBackSonumberSortingByDate;
import com.xanadutec.coreflex.model.FeedBackTypeTable;
import com.xanadutec.coreflex.model.Feedback;
import com.xanadutec.coreflex.model.UserModel;

@Service("FeedcomWebServicesService")
public class FeedcomWebServicesServiceImpl implements FeedcomWebServicesService{
	@Autowired FeedcomWebServicesDao feedcomWebServicesDao;

	@Override
	public List<FeedBackCounterForHome> webServiceGetfeedBackCounterForHome(List<FeedBackTypeTable> feedBackTypeTables,
			UserModel userFrom, UserModel userTo) {
		// TODO Auto-generated method stub
		return feedcomWebServicesDao.webServiceGetfeedBackCounterForHome(feedBackTypeTables,userFrom,userTo);
	}

	@Override
	public List<FeedBackSonumberSortingByDate> getUpdatedSortedFeedBackList(UserModel userFrom,
			UserModel clientCompanyAdmin) {
		// TODO Auto-generated method stub
		return feedcomWebServicesDao.getUpdatedSortedFeedBackList(userFrom, clientCompanyAdmin);
	}

	@Override
	public List<Feedback> getListOfChatingUsingSoNumberAndFeedBackType(String soNumber,
			FeedBackTypeTable feedBackTypeTable, UserModel userFrom, UserModel clientCompanyAdmin) {
		// TODO Auto-generated method stub
		return feedcomWebServicesDao.getListOfChatingUsingSoNumberAndFeedBackType(soNumber, feedBackTypeTable, userFrom, clientCompanyAdmin);
	}

	@Override
	public List<FeedBackSonumberSortingByDate> getUpdatedSortedFeedBackListWithFeedbackType(UserModel userFrom, UserModel clientCompanyAdmin,
			FeedBackTypeTable feedBackTypeTable) {
		// TODO Auto-generated method stub
		return feedcomWebServicesDao.getUpdatedSortedFeedBackListWithFeedbackType(userFrom, clientCompanyAdmin, feedBackTypeTable);
	}



}
