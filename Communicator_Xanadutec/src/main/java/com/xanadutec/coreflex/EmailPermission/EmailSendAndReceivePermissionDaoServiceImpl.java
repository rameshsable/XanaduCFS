package com.xanadutec.coreflex.EmailPermission;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.xanadutec.coreflex.feedback.FeedbackDao;
import com.xanadutec.coreflex.model.EmailSendReceivePermission;
import com.xanadutec.coreflex.model.ServerInfo;
@Service("EmailSendAndReceivePermissionService")
public class EmailSendAndReceivePermissionDaoServiceImpl implements EmailSendAndReceivePermissionService  {
	@Autowired
	private EmailSendAndReceivePermissionDao emailPermissionServiceDao;
	
	@Override
	public List<EmailSendReceivePermission> getEmailPermissionUserListAccordingToCompany(String companyId) {
		// TODO Auto-generated method stub
		return emailPermissionServiceDao.getEmailPermissionUserListAccordingToCompany(companyId);
	}

	@Override
	public void SaveEmailPermissionByUserId(int id,int comid) {
		// TODO Auto-generated method stub
		emailPermissionServiceDao.SaveEmailPermissionByUserId(id,comid);
	}

	@Override
	public ServerInfo getServerInfo() {
		// TODO Auto-generated method stub
		return emailPermissionServiceDao.getServerInfo();
	}
	
	

}
