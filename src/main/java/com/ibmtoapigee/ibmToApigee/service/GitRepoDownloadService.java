package com.ibmtoapigee.ibmToApigee.service;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class GitRepoDownloadService {

	private static String MY_PROPERTY = "https://github.com/JoelGauci/apic2apigee.git";
	private static final Logger logger = LoggerFactory.getLogger(GitRepoDownloadService.class);

	@Autowired
	private static Git git;

	@Autowired
	private CommonsDataUtils commonsDataUtils;

	public static String getRepoPath() throws Exception {
		return git.getRepository().getWorkTree().getPath();
	}

	public void getOrCloneRepo() throws Exception {
		logger.info("GitRepoDownloadService ::: getOrCloneRepo started");
		try {
//			Git_REPO_Default_NAME = commonsDataUtils.getGitRepoPath(Git_REPO_Default_NAME); 
//			logger.info("GitRepoDownloadService ::: getOrCloneRepo ::  getFolder "+Git_REPO_Default_NAME);
			File file = commonsDataUtils.getGitRepoFile();
			if (file.exists()) {
				logger.info("Repo is already present.");
			} else {
				logger.info("Repo is not present. Downloading repo in local folder....");
				cloneRepo(file);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception("Error Occured while Getting/ Cloning Repository...");
		}
		logger.info("GitRepoDownloadService ::: getOrCloneRepo end");
	}

	public ResponseEntity<String> cloneRepo(File file) throws Exception {
		logger.info("GitRepoDownloadService ::: cloneRepo started");
		try {
			logger.info("GitRepoDownloadService ::: cloneRepo ::: Git_REPO_Default_NAME :: 59 :::" + file);
			git = Git.cloneRepository().setDirectory(file).setURI(MY_PROPERTY).call();
			String repoPath = git.getRepository().getWorkTree().getPath();
			git.close();
			return new ResponseEntity<>(repoPath, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("error triggered : " + e.getMessage());
		}
		logger.info("GitRepoDownloadService ::: cloneRepo ended");
		return null;
	}
}
