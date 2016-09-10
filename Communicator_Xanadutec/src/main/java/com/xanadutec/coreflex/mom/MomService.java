package com.xanadutec.coreflex.mom;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.userdetails.UserDetails;

import com.xanadutec.coreflex.model.Documents;
import com.xanadutec.coreflex.model.Mom;
import com.xanadutec.coreflex.model.UserModel;

public interface MomService {

	public int saveMom(Mom mom);

	public List<Mom> viewMom();

	public Mom getMomById(int id);

	public List<Mom> getListOfupdated50Mom(UserModel userFrom, UserModel userTo);

	public List<Date> getDistinctMomDates();

	public Map<Date, Long> getCountOfMomListByDistinctDatesOfMomAndUserLastVisitTime(List<Date> distinctDatesOfMom,
			Date momLastVisitDate, UserModel userFrom, UserModel userTo,HttpServletRequest request);

	public List<Mom> getListOfMomByDate(Date date, Date dateTo, HttpServletRequest request);
}
