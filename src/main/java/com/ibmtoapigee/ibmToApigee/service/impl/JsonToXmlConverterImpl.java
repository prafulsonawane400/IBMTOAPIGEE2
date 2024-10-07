package com.ibmtoapigee.ibmToApigee.service.impl;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import com.ibmtoapigee.ibmToApigee.service.CommonsDataUtils;
import com.ibmtoapigee.ibmToApigee.service.ExternalApiCallService;
import com.ibmtoapigee.ibmToApigee.service.JsonToXmlConverter;

@Component
public class JsonToXmlConverterImpl implements JsonToXmlConverter {

	private static final Logger logger = LoggerFactory.getLogger(JsonToXmlConverterImpl.class);

	@Value("${APICONNECT_TO_APIGEE_FOLDER}")
	private String getFolder;

	@Autowired
	private CommonsDataUtils commonDataUtils;

	@Autowired
	private ExternalApiCallService externalApiCallService;

	public String convertJsonToXml(String jsonData) throws IOException {
		logger.info("Json to XML conversion starts");

		// String xmlString =U.jsonToXml(jsonData);
		String xmlString = null;
		xmlString = externalApiCallService.postApiResponse("https://emea-poc8-test.apigee.net/v1/json2xml", jsonData);
		String cTFileName1 = YamlToJsonConverterImpl.yamlFileName;

		String[] parts = cTFileName1.split("\\.");
		String cTFileName = parts[0];

		logger.info("File name: " + cTFileName);

		logger.info("cTFileName " + cTFileName);
		File file = commonDataUtils.getXMLStoredFile(cTFileName);
		java.io.FileWriter fw = new java.io.FileWriter(file);
		fw.write(xmlString);
		fw.close();
		logger.info("JSON-XML file stored in " + file.getAbsolutePath());
		return cTFileName;
	}

}
