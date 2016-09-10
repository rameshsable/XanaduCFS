package com.xanadutec.coreflex.FileUploadDownload;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;

import com.xanadutec.coreflex.model.FtpInfo;
import com.xanadutec.coreflex.model.Operator;
import com.xanadutec.coreflex.model.UserModel;

public interface FileUploadService {

	public void saveFTP(FtpInfo ftpInfo);
	public FtpInfo getFtpInfo();
}
