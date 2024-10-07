package com.ibmtoapigee.ibmToApigee.service;

import java.io.BufferedReader;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BashScriptRunnerService implements Runnable {

	private CommonsDataUtils commonsDataUtils;
	private String cTFileName;
	private static final Logger logger = LoggerFactory.getLogger(BashScriptRunnerService.class);

	public BashScriptRunnerService(String cTFileName, CommonsDataUtils commonsDataUtils) {
		this.cTFileName = cTFileName;
		this.commonsDataUtils = commonsDataUtils;
	}

	@Override
	public void run() {
		try {
			logger.info("Bashscript BashScriptRunnerService run method called");
			String line;
			ProcessBuilder processBuilder;
			String getRepoFolderName = commonsDataUtils.getGitRepoFile().getAbsolutePath();
			logger.info("getRepoFolderName present ::: 32 ::: " + getRepoFolderName);
			if (getRepoFolderName != null) {
				processBuilder = new ProcessBuilder("bash", "samplebash.sh", cTFileName, getRepoFolderName);
			} else {
				processBuilder = new ProcessBuilder("bash", "samplebash.sh", cTFileName);
			}

			// Process process = Runtime.getRuntime().exec("bash samplebash.sh
			// "+cTFileName);
			Process process = processBuilder.start();
			process.waitFor();
			BufferedReader in = new BufferedReader(new InputStreamReader(process.getInputStream()));
			while ((line = in.readLine()) != null) {
				logger.info(line);
			}
			in.close();
			if (process.exitValue() == 0) {
				logger.info("Command succeeded");
			} else {
				logger.warn("Command failed");
			}
		} catch (Exception e) {
			logger.error("Something went wrong while executing bash script error : " + e.getMessage());
		}

	}
}
