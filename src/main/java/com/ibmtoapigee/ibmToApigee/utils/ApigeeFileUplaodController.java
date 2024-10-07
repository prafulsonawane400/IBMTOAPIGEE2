package com.ibmtoapigee.ibmToApigee.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ibmtoapigee.ibmToApigee.DBOperation.FileTrackerDBService;
import com.ibmtoapigee.ibmToApigee.controller.FrontController;
import com.ibmtoapigee.ibmToApigee.service.CommonsDataUtils;
import com.ibmtoapigee.ibmToApigee.service.MyService;
import com.ibmtoapigee.ibmToApigee.utils.ApigeeAPIUriBuilder.ApigeeAction;

@RestController
@RequestMapping("/upload")
public class ApigeeFileUplaodController {
	
	private static Boolean isApigeeX = true;

	@Value("${apigee.authorizationToken}")
	private String authorizationToken;

	@Value("${apigee.domainURI}")
	private String domainURI;

	@Value("${apigee.organization.name}")
	private String organizationName;

	@Value("${apigee.environment}")
	private String apigeeEnvironment;

	@Value("${apigee.zipFileLocation}")
	private String apigeeZipFileLocation;
	
	@Value("${apigee.domainURI1}")
	private String domainURI1;
	
	@Value("${apigee.action}")
	private String domainAction;
	

	@Autowired
	private FileTrackerDBService ftDBService;

	private Logger logger = LoggerFactory.getLogger(FrontController.class);

	@Autowired
	private ZipUtils zipUtils;

	@Autowired
	private CommonsDataUtils commonDataUtils;
	
	@Autowired
	private MyService myService;

	@GetMapping
	public String uploadZip(String fileName) {
		String result = null;
		try {
			String uploadURI = ApigeeAPIUriBuilder.getURI(ApigeeAction.CREATE_PROXY, domainURI, organizationName,
					apigeeEnvironment, fileName, false);
			if (isApigeeX) {
				uploadURI = ApigeeAPIUriBuilder.getURI(ApigeeAction.CREATE_PROXY, domainURI1, organizationName,
						apigeeEnvironment, fileName, isApigeeX);				
			}
			String apigeeProxyPath = commonDataUtils.getGitRepoFile().getAbsolutePath();
			apigeeProxyPath += ("/" + fileName + "/apiproxy");
			String zipFileLocation = apigeeZipFileLocation + "/" + fileName + ".zip";
			logger.info("Zip File Location is " + zipFileLocation);
			zipUtils.convertToZip(apigeeProxyPath, zipFileLocation);
			// String zipFileLocation =
			// apigeeZipFileLocation+"/account-and-transaction-api-specification_v3.zip";
			ApigeeUploadObject apigeeUO = new ApigeeUploadObject();
			apigeeUO.setUploadURI(uploadURI);
			apigeeUO.setZipLocation(zipFileLocation);
			apigeeUO.setAuthorizationToken(authorizationToken);
			logger.info("Setting upload URI" + apigeeUO);
			if (!isApigeeX) {
				result = uploadZipMethod(apigeeUO);
			}
			else {
				result = uploadZipMethodV2(apigeeUO);
			}			
			logger.info("Result " + result);
			ftDBService.updateUploadedStatus(fileName);
		} catch (Exception e) {
			result = e.getMessage();
			logger.info("Exception " + e.getMessage());
		}
		return result;
	}

	private String uploadZipMethod(ApigeeUploadObject apigeeUO) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.MULTIPART_FORM_DATA);
		headers.add("Authorization", "Basic " + apigeeUO.getAuthorizationToken());

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", new FileSystemResource(apigeeUO.getZipLocation()));

		HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

		ResponseEntity<String> response = restTemplate.postForEntity(apigeeUO.getUploadURI(), requestEntity,
				String.class);

		return response.getBody();

	}
	
	private String uploadZipMethodV2(ApigeeUploadObject apigeeUO) {
		RestTemplate restTemplate = new RestTemplate();
		System.out.println(apigeeUO.toString());
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_OCTET_STREAM); // Or MediaType.ALL if unsure
		headers.set("Content-Encoding", "gzip");
		headers.add("Authorization", "Bearer " + myService.getAccessTokenValue());

		MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
		body.add("file", new FileSystemResource(apigeeUO.getZipLocation()));
	
		//HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);
		ResponseEntity<String> response = null;
		
        // Send POST request
        try {
   		    byte[] requestBody = new FileSystemResource(apigeeUO.getZipLocation()).getContentAsByteArray(); /* your byte array representing the request body */;
	        HttpEntity<byte[]> entity = new HttpEntity<>(requestBody, headers);
            response = restTemplate.exchange(apigeeUO.getUploadURI(), HttpMethod.POST, entity, String.class);
            System.out.println("Response Status Code: " + response.getStatusCode());
            System.out.println("Response Body: " + response.getBody()); 

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

		return response.getBody();

	}

}
