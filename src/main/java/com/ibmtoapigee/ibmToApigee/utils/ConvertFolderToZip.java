package com.ibmtoapigee.ibmToApigee.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/convertToZip")
public class ConvertFolderToZip {

	@Value("${apigee.zipFileLocation}")
	private String zipFileLocation;

	@GetMapping
	public String uploadFolderZip() {
		String fileName = "account-and-transaction-api-specification_v3";
		String incomingFileLocation = "C:\\Users\\shraddhasonawane\\Downloads\\" + fileName;
		String targetLocation = zipFileLocation + "/" + fileName + ".zip";

		// Develop a functionality to create a zip of above folder including all files
		// and uploaded it in ziplocation
		try (ZipOutputStream zipOut = new ZipOutputStream(
				new BufferedOutputStream(new FileOutputStream(targetLocation)))) {
			File folder = new File(incomingFileLocation);
			zipFiles(folder, folder.getName(), zipOut);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return targetLocation;
	}

	private static void zipFiles(File folder, String parentFolder, ZipOutputStream zipOut) throws Exception {
		for (File file : folder.listFiles()) {
			if (file.isDirectory()) {
				zipFiles(file, parentFolder + "/" + file.getName(), zipOut);
			} else {
				String path = parentFolder + "/" + file.getName();
				ZipEntry zipEntry = new ZipEntry(path);
				zipOut.putNextEntry(zipEntry);
				try (BufferedInputStream in = new BufferedInputStream(new FileInputStream(file))) {
					byte[] buffer = new byte[1024];
					int len;
					while ((len = in.read(buffer)) > 0) {
						zipOut.write(buffer, 0, len);
					}
				}
			}
		}
	}

}
