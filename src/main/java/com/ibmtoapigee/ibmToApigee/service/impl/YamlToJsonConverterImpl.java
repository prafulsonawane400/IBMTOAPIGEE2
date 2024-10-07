package com.ibmtoapigee.ibmToApigee.service.impl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.ibmtoapigee.ibmToApigee.service.YamlToJsonConverter;

@Component
public class YamlToJsonConverterImpl implements YamlToJsonConverter {

	private static final Logger logger = LoggerFactory.getLogger(YamlToJsonConverterImpl.class);

	static String yamlFileName;

	public String convertYamlToJsonFile(File uploadedFile) throws IOException {
		logger.info("Yaml - Json conversion starts");
		String yamlData = Files.readString(Path.of(uploadedFile.getPath()));
		yamlFileName = uploadedFile.getName();
		logger.info("Yaml FileName " + uploadedFile.getName());
		ObjectMapper yamlReader = new ObjectMapper(new YAMLFactory());
		Object obj = yamlReader.readValue(yamlData, Object.class);

		ObjectMapper jsonWriter = new ObjectMapper();
		String output = jsonWriter.writeValueAsString(obj);
		logger.info("output json " + output);
		return output;
	}
}

