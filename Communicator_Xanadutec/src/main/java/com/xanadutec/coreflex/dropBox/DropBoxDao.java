package com.xanadutec.coreflex.dropBox;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.Documents;

import com.xanadutec.coreflex.model.Report;
import com.xanadutec.coreflex.model.UserModel;

public interface DropBoxDao {

	public void saveFIleDetails(Report fileName);

	public List<Date> getDistinctDropBoxList();

	public void deleteFileByName(String name);

	public Map<Date, Map<Integer, List<Report>>> getCountOfDistinctDropBoxListByDistinctDatesOfDropBoxlist(
			List<Date> distinctDatesOfDropBoxlist, HttpServletRequest request);

	public Map<Date, Long> getCountOfDistinctDropBoxListByDistinctDatesOfDropBoxlistAndUserLastVisitTime(
			List<Date> distinctDatesOfDocuement, Date lastVisitedTime, UserModel userFrom, UserModel userTo);

	public List<Report> getListOfupdated50Reports(UserModel userFrom, UserModel userTo);

	public List<Report> getListOfDropBoxByFileName(String fileName, HttpServletRequest request);

	public Report getReportObjectByReportId(int id);

	/* document section is below */
	public void saveDocumentFIleDetails(Documents fileName);

	public List<Date> getDistinctDocumentList();

	public Map<Date, Map<Integer, List<Documents>>> getCountOfDistinctDocumentListByDistinctDatesOfDocuement(
			List<Date> distinctDatesOfDocuement, HttpServletRequest request);

	public Map<Date, Long> getCountOfDistinctDocumentListByDistinctDatesOfDocuementAndUserLastVisitTime(
			List<Date> distinctDatesOfDocuement, Date lastVisitedTime, UserModel userFrom, UserModel userTo);

	public List<Documents> getListOfupdated50Documents(UserModel userFrom, UserModel userTo);

	public List<Documents> getListOfDocumentByFileName(String fileName, HttpServletRequest request);

	List<Report> getListOfDropBoxByDate(Date date, Date dateTo, HttpServletRequest request);

	public List<Documents> getListOfDocumentByDate(Date date, Date dateTo, HttpServletRequest request);

	public Documents getDocumentsObjectByDocumentsId(int id);
}
