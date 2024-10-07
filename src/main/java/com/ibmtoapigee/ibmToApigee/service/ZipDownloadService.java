package com.ibmtoapigee.ibmToApigee.service;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibmtoapigee.ibmToApigee.service.impl.YamlToJsonConverterImpl;

import jakarta.servlet.http.HttpServletResponse;

@Component
public class ZipDownloadService {

	private static final Logger logger = LoggerFactory.getLogger(YamlToJsonConverterImpl.class);

	private static final String FOLDER = "apic2apigee/_res_/apiproxy";

	private static String FOLDER_TEMP = "apic2apigee";

	@Autowired
	private CommonsDataUtils commonDataUtils;

	@Value("${APICONNECT_TO_APIGEE_FOLDER}")
	private String getFolder;

	private void zipFolder(File folder, ZipOutputStream zipOutputStream, String fileName) throws IOException {

		// Get all the files in the folder.
		File[] files = folder.listFiles();
		String filePath;

		// Iterate through the files and add each file to the zip file using a ZipEntry
		// object.
		for (File file : files) {
			String[] splittedPath = file.getPath().split("/" + fileName + "/");
			filePath = file.getPath();
			if (splittedPath != null && splittedPath.length > 0 && splittedPath[1] != null) {
				filePath = splittedPath[1];
			}
			logger.info("filepath is " + filePath);
			if (file.isDirectory()) {
				zipFolder(file, zipOutputStream, fileName);
			} else {
				// ZipEntry zipEntry = new ZipEntry(file.getAbsolutePath());
				ZipEntry zipEntry = new ZipEntry(filePath);
				zipOutputStream.putNextEntry(zipEntry);

				FileInputStream fileInputStream = new FileInputStream(file);
				byte[] bytes = new byte[1024];
				int length;
				while ((length = fileInputStream.read(bytes)) >= 0) {
					zipOutputStream.write(bytes, 0, length);
				}

				fileInputStream.close();
				zipOutputStream.closeEntry();
			}
		}
	}

	public void DownloadZipFile(HttpServletResponse response, String fileName) throws IOException {
		String path = commonDataUtils.getGitRepoFile().getAbsolutePath();
		path += ("/" + fileName + "/apiproxy");
		logger.info("ZipDownloadService ::: DownloadZipFile started");
		logger.info("New Path" + path);
		// Create a new ZipOutputStream object
		ZipOutputStream zipOutputStream = new ZipOutputStream(response.getOutputStream());

		// Add the contents of the folder to the ZipOutputStream
		// File folder = new File(FOLDER);
		File folder = new File(path);
		logger.info("Creating Zip file for " + folder.getAbsolutePath() + "");
		zipFolder(folder, zipOutputStream, fileName);
		zipOutputStream.close();
		// Set the response headers
		response.setHeader("Content-Disposition", "attachment; filename=" + fileName + ".zip");
		response.setHeader("Content-Type", "application/zip");
		logger.info("ZipDownloadService ::: DownloadZipFile end");
	}

	public void UploadZipFileToApigeePortal(HttpServletResponse response, String fileName) throws IOException {

		String command = "curl --location 'https://api.enterprise.apigee.com/v1/organizations/prafulsonawane400-eval/apis?name="
				+ fileName + "&action=import' \\\n"
				+ "--header 'Authorization: Basic cHJhZnVsc29uYXdhbmU0MDBAZ21haWwuY29tOlByYWZ1bEA0MDA=' \\\n"
				+ "--form 'file=@\"" + fileName + "\"'";
		ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
		Process process = processBuilder.start();

		InputStream inputStream = process.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);

		String line;
		while ((line = bufferedReader.readLine()) != null) {
			System.out.println(line);
		}

	}

}
