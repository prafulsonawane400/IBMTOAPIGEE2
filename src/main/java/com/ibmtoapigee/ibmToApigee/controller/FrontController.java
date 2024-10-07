package com.ibmtoapigee.ibmToApigee.controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.ibmtoapigee.ibmToApigee.DBOperation.FileTracker;
import com.ibmtoapigee.ibmToApigee.DBOperation.FileTrackerDBService;
import com.ibmtoapigee.ibmToApigee.service.ProcessApigeeConversionService;
import com.ibmtoapigee.ibmToApigee.service.ZipDownloadService;
import com.ibmtoapigee.ibmToApigee.utils.ApigeeFileUplaodController;
import com.ibmtoapigee.ibmToApigee.utils.ApigeeProxyDeployController;

import jakarta.servlet.http.HttpServletResponse;

@Controller
public class FrontController {

	private Logger logger = LoggerFactory.getLogger(FrontController.class);

	@Autowired
	private ProcessApigeeConversionService processApigeeConversionService;

	@Autowired
	private FileTrackerDBService fileTrackerDBService;

	@Autowired
	ApigeeFileUplaodController apigeeFileUplaodController;

	@Autowired
	private ApigeeProxyDeployController apDeployController;

	@Autowired
	private ZipDownloadService zipDownloadService;

	@PostMapping("/download-zip-file/{fileId}")
	public void downloadZipFile(HttpServletResponse response, @PathVariable String fileId) throws Exception {
		try {
			logger.info("Zip file creation process started for " + fileId);
			zipDownloadService.DownloadZipFile(response, fileId);
			logger.info("Zip file creation process is completed for " + fileId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/upload-to-apigee-portal/{fileId}")
	public String uploadFileToApigeePortal(@PathVariable String fileId) throws Exception {
		try {
			logger.info("file is starting to upload on apigee portal" + fileId);
			apigeeFileUplaodController.uploadZip(fileId);
			logger.info("started uploading " + fileId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/v/dashboard";
	}

	@GetMapping("/deploy-to-apigee-portal/{fileId}")
	public String deployFileToApigeePortal(@PathVariable String fileId) throws Exception {
		try {
			logger.info("file is starting to upload on apigee portal" + fileId);
			apDeployController.deployZip(fileId);
			logger.info("started uploading " + fileId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "redirect:/v/dashboard";
	}

	@GetMapping("/download-zip-file/{fileId}")
	public String redirectToHome(HttpServletResponse response, @PathVariable String fileId) throws Exception {
		downloadZipFile(response, fileId);
		return "redirect:/v/dashboard?failDownload=true";
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/" })
	public ModelAndView getVHome() {
		logger.info("FrontController :: getVHome ::: page is loading...");
		ModelAndView mAv = new ModelAndView();
		mAv.setViewName("index");
		mAv.addObject("formControl", true);
		return mAv;
	}

	@RequestMapping(method = RequestMethod.GET, value = { "/v/dashboard" })
	public ModelAndView getDashBoard(@RequestParam(defaultValue = "false", required = false) Boolean failDownload) {
		logger.info("FrontController :: getDashBoard ::: page is loading...");
		List<FileTracker> fileTrackerList = fileTrackerDBService.getFileTrackerList();
		ModelAndView mAv = new ModelAndView();
		mAv.setViewName("dashboard");
		mAv.addObject("formControl", false);
		mAv.addObject("DashboardView", true);
		mAv.addObject("fileTrackerList", fileTrackerList);
		if (failDownload) {
			mAv.addObject("Download_Error", true);
		}
		return mAv;
	}

	@PostMapping("/upload-file")
	public String uploadFile(@RequestParam("file") MultipartFile file) {
		ModelAndView mAv = null;
		String result = null;

		try {
			logger.info("Upload-file request triggered");
			mAv = new ModelAndView();
			mAv.setViewName("dashboard");
			result = processApigeeConversionService.processRequest(file);
			createAndStore(result, file);
			mAv.addObject("success", true);
			mAv.addObject("downloadUrl", "/download-zip-file/" + result);
			List<FileTracker> fileTrackerList = fileTrackerDBService.getFileTrackerList();
			mAv.addObject("DashboardView", true);
			mAv.addObject("fileTrackerList", fileTrackerList);
			logger.info("Output is " + result);
		} catch (Exception e) {
			logger.error("Error is " + e.getMessage());
			result = e.getMessage();
			mAv.addObject("error", true);
			mAv.addObject("errorMsg", result);
		}
		mAv.addObject("formControl", false);
		return "redirect:/v/dashboard";
		
	}
	
	private void createAndStore(String fileName, MultipartFile file) throws Exception {
		FileTracker fT = createFileTrackerObj(fileName, file);
		fileTrackerDBService.addFileToRepo(fT);
	}

	private FileTracker createFileTrackerObj(String fileName, MultipartFile file) {
		FileTracker fT = new FileTracker();
		fT.setFileId(fileName);
		fT.setFileName(file.getOriginalFilename());
		fT.setCreatedDate(new Timestamp(System.currentTimeMillis()));
		fT.setUploadToApigee(false);
		fT.setDeployedToApigee(false);
		return fT;
	}

}
