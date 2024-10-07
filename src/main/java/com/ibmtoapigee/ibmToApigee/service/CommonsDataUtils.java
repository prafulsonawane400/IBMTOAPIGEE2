package com.ibmtoapigee.ibmToApigee.service;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class CommonsDataUtils {

	private static final Logger logger = LoggerFactory.getLogger(CommonsDataUtils.class);

	private static final String gitFolderName = "apic2apigee";

	private static final String xmlStoringDirectory = "apic2apigee/files";

	@Value("${APICONNECT_TO_APIGEE_FOLDER}")
	private String getFolder;

	public File getGitRepoFile() {
		String filePath = gitFolderName;
		if (getFolder != null) {
			filePath = getFolder + "/" + gitFolderName;
		}
		File file = new File(filePath);
		return file;
	}

	public File getXMLStoredFile(String fileName) throws IOException {
		String tempXmlStoringDirectory = xmlStoringDirectory;
		if (getFolder != null) {
			tempXmlStoringDirectory = getFolder + "/" + xmlStoringDirectory;
		}
		String filePath = tempXmlStoringDirectory + "/" + fileName + ".xml";
		logger.info("Printing Dir : " + tempXmlStoringDirectory);
		File directory = new File(tempXmlStoringDirectory);
		logger.info("CommonsDataUtils ::: getXMLStoredFile ::: Dir Exist " + directory.exists());
		if (!directory.exists()) {
			directory.mkdir();
			logger.info("CommonsDataUtils ::: getXMLStoredFile ::: created Dir " + directory.exists());
		}
		logger.info("CommonsDataUtils ::: getXMLStoredFile ::: Dir Exist " + directory.exists());
		File file = new File(filePath);
		if (!file.exists()) {
			file.createNewFile();
		}
		logger.info("File " + file.getName() + " exist : File Exist" + file.exists());
		return file;
	}

}
