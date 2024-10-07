package com.ibmtoapigee.ibmToApigee.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.stereotype.Component;

@Component
public class ZipUtils {

	private List<String> fileList;
	private static String SOURCE_FOLDER;
	private static String OUTPUT_ZIP_FILE;
	

	public ZipUtils() {
		fileList = new ArrayList<String>();
	}

	public void convertToZip(String SOURCE_FOLDER, String outputFile) {
		this.SOURCE_FOLDER = SOURCE_FOLDER;
		this.OUTPUT_ZIP_FILE = outputFile;
		ZipUtils appZip = new ZipUtils();
		appZip.generateFileList(new File(SOURCE_FOLDER));
		appZip.zipIt(OUTPUT_ZIP_FILE);
	}

	public void zipIt(String zipFile) {
		byte[] buffer = new byte[1024];
		String source = new File(SOURCE_FOLDER).getName();
		FileOutputStream fos = null;
		ZipOutputStream zos = null;
		try {
			fos = new FileOutputStream(zipFile);
			zos = new ZipOutputStream(fos);

			System.out.println("Output to Zip : " + zipFile);
			FileInputStream in = null;

			for (String file : this.fileList) {
				System.out.println("File Added : " + file);
				ZipEntry ze = new ZipEntry(source + File.separator + file);
				zos.putNextEntry(ze);
				try {
					in = new FileInputStream(SOURCE_FOLDER + File.separator + file);
					int len;
					while ((len = in.read(buffer)) > 0) {
						zos.write(buffer, 0, len);
					}
				} finally {
					in.close();
				}
			}

			zos.closeEntry();
			System.out.println("Folder successfully compressed");

		} catch (IOException ex) {
			ex.printStackTrace();
		} finally {
			try {
				zos.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

//
	public void generateFileList(File node) {
		// add file only
		if (node.isFile()) {
			fileList.add(generateZipEntry(node.toString()));
		}

		if (node.isDirectory()) {
			String[] subNote = node.list();
			for (String filename : subNote) {
				generateFileList(new File(node, filename));
			}
		}
	}

	private String generateZipEntry(String file) {
		return file.substring(SOURCE_FOLDER.length() + 1, file.length());
	}

	public void UploadZipFileToApigeePortal() throws IOException {

		System.out.println("Method Called");

		String command = "curl --location 'https://api.enterprise.apigee.com/v1/organizations/prafulsonawane400-eval/apis?name=account-transaction&action=import' \\\r\n"
				+ "--header 'Authorization: Basic cHJhZnVsc29uYXdhbmU0MDBAZ21haWwuY29tOlByYWZ1bEA0MDA=' \\\r\n"
				+ "--form 'file=@\"/C:/Users/shraddhasonawane/OneDrive - Nagarro/Documents/account-and-transaction-api-specification_v3.zip\"'";
		ProcessBuilder processBuilder = new ProcessBuilder(command.split(" "));
		System.out.println("Process Builder called");
		Process process = processBuilder.start();

		InputStream inputStream = process.getInputStream();
		InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		System.out.println("Process Builder called 1");
		String line;
		while ((line = bufferedReader.readLine()) != null) {
			System.out.println(line);
		}

	}

}