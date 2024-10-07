package com.ibmtoapigee.ibmToApigee.service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class UploadZipFileService {

	public void UploadZipFileToApugeePortal() throws IOException {

		String command = "curl --location 'https://api.enterprise.apigee.com/v1/organizations/prafulsonawane400-eval/apis?name=test-api1&action=import' \\\n"
				+ "--header 'Authorization: Basic cHJhZnVsc29uYXdhbmU0MDBAZ21haWwuY29tOlByYWZ1bEA0MDA=' \\\n"
				+ "--form 'file=@\"/Users/prafulsonawane/Downloads/test-api_1 (4).zip\"'";
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
