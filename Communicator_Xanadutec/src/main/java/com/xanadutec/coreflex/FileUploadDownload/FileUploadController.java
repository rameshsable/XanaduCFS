package com.xanadutec.coreflex.FileUploadDownload;

import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.Iterator;

import javax.servlet.ServletContext;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import com.xanadutec.coreflex.model.FtpInfo;

@Controller
public class FileUploadController {
	@Autowired
	FileUploadService fileUploadService;

	@RequestMapping(value = "/uploadMyFile", method = RequestMethod.POST)
	@ResponseBody
	public String handleFileUpload(MultipartHttpServletRequest request) throws Exception {

		Iterator<String> itrator = request.getFileNames();
		String str = "";
		while (itrator.hasNext()) {

			MultipartFile multiFile = request.getFile(itrator.next());
			try {
				// just to show that we have actually received the file
				String fileName = new Date().getTime() + multiFile.getOriginalFilename();

				byte[] bytes = multiFile.getBytes();
				str = str + fileName + "#@$";
				FtpInfo ftpInfo= fileUploadService.getFtpInfo();

				String server = ftpInfo.getServerName();
				int port = ftpInfo.getPort();

				String user = ftpInfo.getUserName();
				String pass = ftpInfo.getPassword();

				FTPClient ftpClient = new FTPClient();
				try {

					ftpClient.connect(server, port);
					ftpClient.login(user, pass);
					ftpClient.enterLocalPassiveMode();

					ftpClient.setFileType(FTP.BINARY_FILE_TYPE);

					// APPROACH #2: uploads second file using an OutputStream
					String secondRemoteFile = "/TEST/" + fileName;
					InputStream inputStream = null;
					try {
						inputStream = new ByteArrayInputStream(bytes);
					} catch (Exception e) {
						e.printStackTrace();
					}

					OutputStream outputStream = ftpClient.storeFileStream(secondRemoteFile);
					byte[] bytesIn = new byte[4096];
					int read = 0;

					while ((read = inputStream.read(bytesIn)) != -1) {
						outputStream.write(bytesIn, 0, read);
					}
					inputStream.close();
					outputStream.close();

					boolean completed = ftpClient.completePendingCommand();
					if (completed) {
						System.out.println("The file is uploaded successfully.");
					}

				} catch (IOException ex) {
					System.out.println("Error: " + ex.getMessage());
					ex.printStackTrace();
				} finally {
					try {
						if (ftpClient.isConnected()) {
							ftpClient.logout();
							ftpClient.disconnect();
						}
					} catch (IOException ex) {
						ex.printStackTrace();
					}
				}

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				throw new Exception("Error while loading the file"+e.getMessage());
			}

		}

		return toJson(str);
	}

	public String toJson(Object data) {
		ObjectMapper mapper = new ObjectMapper();
		StringBuilder builder = new StringBuilder();
		try {
			builder.append(mapper.writeValueAsString(data));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return builder.toString();
	}
}