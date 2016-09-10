package com.xanadutec.coreflex.EmailPermission;

import java.util.List;

import com.xanadutec.coreflex.model.EmailSendReceivePermission;
import com.xanadutec.coreflex.model.ServerInfo;

public interface EmailSendAndReceivePermissionDao {

	public List<EmailSendReceivePermission> getEmailPermissionUserListAccordingToCompany(String companyId);
	public void SaveEmailPermissionByUserId(int id,int comid);
	public ServerInfo getServerInfo();
	
}
