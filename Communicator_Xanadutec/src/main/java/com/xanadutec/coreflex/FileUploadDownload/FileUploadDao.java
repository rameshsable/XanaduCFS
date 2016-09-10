package com.xanadutec.coreflex.FileUploadDownload;

import java.util.List;

import com.xanadutec.coreflex.model.FtpInfo;


public interface FileUploadDao {

	public void saveFTP(FtpInfo ftpInfo);
	public FtpInfo getFtpInfo();
}
