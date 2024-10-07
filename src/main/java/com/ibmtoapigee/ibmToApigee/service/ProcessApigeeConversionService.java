package com.ibmtoapigee.ibmToApigee.service;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class ProcessApigeeConversionService {

	private static final Logger logger = LoggerFactory.getLogger(ProcessApigeeConversionService.class);
	private static final ExecutorService executorService = Executors.newFixedThreadPool(4);

	@Autowired
	private GitRepoDownloadService gitRepoDownloadService;

	@Autowired
	private YamlToJsonConverter yamlToJsonConverter;

	@Autowired
	private JsonToXmlConverter jsonToXmlConverter;

	@Autowired
	private CommonsDataUtils commonsDataUtils;

	public String processRequest(MultipartFile file) throws Exception {
		String cTFileName = null;
		try {
			logger.info("Get/ Cloning Apic2Apigee Repo....");
			gitRepoDownloadService.getOrCloneRepo();
			File uploadedFile = new File(file.getOriginalFilename());
			logger.info("uploadFile : " + uploadedFile);
			file.transferTo(uploadedFile.getAbsoluteFile());
			logger.info("Converting (Yaml) file into Json String....");
			String jsonOutput = yamlToJsonConverter.convertYamlToJsonFile(uploadedFile);
			logger.info("Converting (JSON) String into XML File....");
			cTFileName = jsonToXmlConverter.convertJsonToXml(jsonOutput);
			logger.info("Apigee Conversion Process is going to start...");
			processConversion(cTFileName);
			logger.info("Apigee Conversion Process is ended...");
			logger.warn("Removing uploaded file...");
			uploadedFile.delete();
			logger.warn("Uploaded File removed successfully");
		} catch (Exception e) {
			logger.error("ProcessApigeeConversionService ::: processRequest ::: " + e.getMessage());
			throw new Exception("ProcessApigeeConversionService ::: processRequest ::: " + e.getMessage());
		}
		return cTFileName;
	}

	private void processConversion(String cTFileName) throws Exception {
		try {
			logger.info("Triggering BashScript file...");
			executorService.execute(new BashScriptRunnerService(cTFileName, commonsDataUtils));
			logger.info("Bash file job done");
		} catch (Exception e) {
			logger.error(e.getMessage());
		}
	}
}
