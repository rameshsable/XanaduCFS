package com.xanadutec.coreflex.FileUploadDownload;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.xanadutec.coreflex.model.FtpInfo;
import com.xanadutec.coreflex.model.Operator;
import com.xanadutec.coreflex.model.UserModel;

@Service("FileUploadService")
public class FileUploadServiceImpl implements FileUploadService{

	@Autowired
	private FileUploadDao fileUploadDao;

	@Override
	public void saveFTP(FtpInfo ftpInfo) {
		fileUploadDao.saveFTP(ftpInfo);		
	}

	@Override
	public FtpInfo getFtpInfo() {
		return fileUploadDao.getFtpInfo();
	}
	


}
