package com.ibmtoapigee.ibmToApigee.service;

import java.io.File;
import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public interface YamlToJsonConverter {

	public String convertYamlToJsonFile(File uploadedFile) throws IOException;
}
