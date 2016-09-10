package com.xanadutec.coreflex.dropBox;

import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.xanadutec.coreflex.model.Company;
import com.xanadutec.coreflex.model.Documents;
import com.xanadutec.coreflex.model.Mom;
import com.xanadutec.coreflex.model.Report;
import com.xanadutec.coreflex.model.UserModel;

@Service("DropBoxService")
public class DropBoxServiceImpl implements DropBoxService {

	@Autowired
	private DropBoxDao dropboxDao;

	@Override
	public void saveFIleDetails(Report fileName) {

		dropboxDao.saveFIleDetails(fileName);
	}

	@Override
	public List<Date> getDistinctDropBoxList() {
		// TODO Auto-generated method stub
		return dropboxDao.getDistinctDropBoxList();
	}

	@Override
	public void deleteFileByName(String name) {
		// TODO Auto-generated method stub
		dropboxDao.deleteFileByName(name);
	}

	public Map<Date, Map<Integer, List<Report>>> getCountOfDistinctDropBoxListByDistinctDatesOfDropBoxlist(
			List<Date> distinctDatesOfDropBoxlist, HttpServletRequest request) {
		return dropboxDao.getCountOfDistinctDropBoxListByDistinctDatesOfDropBoxlist(distinctDatesOfDropBoxlist,
				request);

	}

	@Override
	public List<Report> getListOfDropBoxByDate(Date date, Date dateTo,  HttpServletRequest request) {
		// TODO Auto-generated method stub
		return dropboxDao.getListOfDropBoxByDate(date,dateTo, request);
	}

	@Override
	public void saveDocumentFIleDetails(Documents fileName) {
		// TODO Auto-generated method stub
		dropboxDao.saveDocumentFIleDetails(fileName);

	}

	@Override
	public List<Date> getDistinctDocumentList() {
		// TODO Auto-generated method stub
		return dropboxDao.getDistinctDocumentList();
	}

	@Override
	public Map<Date, Map<Integer, List<Documents>>> getCountOfDistinctDocumentListByDistinctDatesOfDocuement(
			List<Date> distinctDatesOfDocuement, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return dropboxDao.getCountOfDistinctDocumentListByDistinctDatesOfDocuement(distinctDatesOfDocuement, request);
	}

	@Override
	public List<Documents> getListOfDocumentByDate(Date date, Date dateTo, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return dropboxDao.getListOfDocumentByDate(date,dateTo, request);
	}

	@Override
	public Map<Date, Long> getCountOfDistinctDropBoxListByDistinctDatesOfDropBoxlistAndUserLastVisitTime(
			List<Date> distinctDatesOfDocuement, Date lastVisitedTime, UserModel userFrom, UserModel userTo) {
		// TODO Auto-generated method stub
		return dropboxDao.getCountOfDistinctDropBoxListByDistinctDatesOfDropBoxlistAndUserLastVisitTime(
				distinctDatesOfDocuement, lastVisitedTime, userFrom, userTo);
	}

	@Override
	public Map<Date, Long> getCountOfDistinctDocumentListByDistinctDatesOfDocuementAndUserLastVisitTime(
			List<Date> distinctDatesOfDocuement, Date lastVisitedTime, UserModel userFrom, UserModel userTo) {
		// TODO Auto-generated method stub
		return dropboxDao.getCountOfDistinctDocumentListByDistinctDatesOfDocuementAndUserLastVisitTime(
				distinctDatesOfDocuement, lastVisitedTime, userFrom, userTo);
	}

	@Override
	public List<Report> getListOfupdated50Reports(UserModel userFrom, UserModel userTo) {
		// TODO Auto-generated method stub
		return dropboxDao.getListOfupdated50Reports(userFrom, userTo);
	}

	@Override
	public List<Documents> getListOfupdated50Documents(UserModel userFrom, UserModel userTo) {
		// TODO Auto-generated method stub
		return  dropboxDao.getListOfupdated50Documents(userFrom, userTo);
	}

	@Override
	public List<Report> getListOfDropBoxByFileName(String fileName, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return  dropboxDao.getListOfDropBoxByFileName(fileName, request);
	}

	@Override
	public List<Documents> getListOfDocumentByFileName(String fileName, HttpServletRequest request) {
		// TODO Auto-generated method stub
		return dropboxDao.getListOfDocumentByFileName(fileName, request);
	}

	@Override
	public Report getReportObjectByReportId(int id) {
		// TODO Auto-generated method stub
		return dropboxDao.getReportObjectByReportId(id);
	}

	@Override
	public Documents getDocumentsObjectByDocumentsId(int id) {
		// TODO Auto-generated method stub
		return dropboxDao.getDocumentsObjectByDocumentsId(id);
	}

}
