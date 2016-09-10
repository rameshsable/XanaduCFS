package com.xanadutec.coreflex.mom;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.xanadutec.coreflex.model.Mom;
import com.xanadutec.coreflex.model.UserModel;

@Service("MomService")
public class MomServiceImpl implements MomService {

	@Autowired
	private MomDao momDao;

	@Override
	public int saveMom(Mom mom) {
		return momDao.saveMom(mom);
	}

	@Override
	public List<Mom> viewMom() {
		// TODO Auto-generated method stub
		return momDao.viewMom();
	}

	@Override
	public Mom getMomById(int id) {
		// TODO Auto-generated method stub
		return momDao.getMomById(id);
	}

	@Override
	public List<Mom> getListOfupdated50Mom(UserModel userFrom, UserModel userTo) {
		// TODO Auto-generated method stub
		return momDao.getListOfupdated50Mom(userFrom, userTo);
	}

	@Override
	public List<Date> getDistinctMomDates() {
		// TODO Auto-generated method stub
		return momDao.getDistinctMomDates();
	}

	@Override
	public Map<Date, Long> getCountOfMomListByDistinctDatesOfMomAndUserLastVisitTime(List<Date> distinctDatesOfMom,
			Date momLastVisitDate, UserModel userFrom, UserModel userTo, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return momDao.getCountOfMomListByDistinctDatesOfMomAndUserLastVisitTime(distinctDatesOfMom, momLastVisitDate,
				userFrom, userTo, request);
	}

	@Override
	public List<Mom> getListOfMomByDate(Date date, Date dateTo, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return momDao.getListOfMomByDate(date, dateTo, request);
	}

}
