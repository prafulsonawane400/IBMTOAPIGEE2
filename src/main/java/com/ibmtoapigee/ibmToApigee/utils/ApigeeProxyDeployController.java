package com.ibmtoapigee.ibmToApigee.utils;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.ibmtoapigee.ibmToApigee.DBOperation.FileTrackerDBService;
import com.ibmtoapigee.ibmToApigee.service.MyService;
import com.ibmtoapigee.ibmToApigee.utils.ApigeeAPIUriBuilder.ApigeeAction;

@RestController
@RequestMapping("/deploy")
public class ApigeeProxyDeployController {
	
	private static Boolean isApigeeX = true;

	@Value("${apigee.authorizationToken}")
	private String authorizationToken;

	@Value("${apigee.domainURI}")
	private String domainURI;

	@Value("${apigee.organization.name}")
	private String organizationName;

	@Value("${apigee.environment}")
	private String apigeeEnvironment;	

	@Autowired
	private FileTrackerDBService ftDBService;
	
	@Value("${apigee.domainURI1}")
	private String domainURI1;
	
	@Value("${apigee.action}")
	private String domainAction;
	
	@Autowired
	private MyService myService;

	@GetMapping
	public String deployZip(String fileName) {
		String result = null;
		try {
			// String fileName = "account-and-transaction-api-specification_v3";
//			String uploadURI = "https://api.enterprise.apigee.com/v1/organizations/prafulsonawane400-eval/environments/prod/apis/account-transaction/revisions/1/deployments";
			String uploadURI = ApigeeAPIUriBuilder.getURI(ApigeeAction.DEPLOY, domainURI, organizationName,
					apigeeEnvironment, fileName, false);
			if (isApigeeX) {
				uploadURI = ApigeeAPIUriBuilder.getURI(ApigeeAction.DEPLOY, domainURI1, organizationName,
						apigeeEnvironment, fileName, isApigeeX);
			}
			ApigeeUploadObject apigeeUO = new ApigeeUploadObject();
			apigeeUO.setUploadURI(uploadURI);
			apigeeUO.setAuthorizationToken(authorizationToken);
			
			if (isApigeeX) {
				result = deployZipMethodV2(apigeeUO);
			}
			else {
				result = deployZipMethod(apigeeUO);
			}
			
			ftDBService.updateDeployStatus(fileName);
		} catch (Exception e) {
			result = e.getMessage();
		}
		return result;
	}
	
	
	private String deployZipMethod(ApigeeUploadObject apigeeUO) {
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Basic " + apigeeUO.getAuthorizationToken()); // Replace with actual credentials

		JSONObject requestBody = new JSONObject(); // Using org.json library for JSON handling
		requestBody.put("stagename", "eval");

		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);

		ResponseEntity<String> response = restTemplate.postForEntity(apigeeUO.getUploadURI(), requestEntity,
				String.class);

		return response.getBody();

	}
	
	private String deployZipMethodV2(ApigeeUploadObject apigeeUO) {
		System.out.println(apigeeUO.toString());
		RestTemplate restTemplate = new RestTemplate();

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", "Bearer " + myService.getAccessTokenValue()); // Replace with actual credentials

		JSONObject requestBody = new JSONObject(); // Using org.json library for JSON handling
		//requestBody.put("stagename", "eval");

		HttpEntity<String> requestEntity = new HttpEntity<>(requestBody.toString(), headers);

		ResponseEntity<String> response = restTemplate.postForEntity(apigeeUO.getUploadURI(), requestEntity,
				String.class);

		return response.getBody();

	}

}
