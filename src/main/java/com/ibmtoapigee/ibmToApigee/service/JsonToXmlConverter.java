package com.ibmtoapigee.ibmToApigee.service;

import java.io.IOException;

import org.springframework.stereotype.Service;

@Service
public interface JsonToXmlConverter {

	public String convertJsonToXml(String jsonData) throws IOException;

}
