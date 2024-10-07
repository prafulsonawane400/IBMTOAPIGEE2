package com.ibmtoapigee.ibmToApigee.utils;

import org.springframework.stereotype.Component;

@Component
public class ApigeeUploadObject {

	private String uploadURI;

	private String zipLocation;

	private String authorizationToken;

	public void setAuthorizationToken(String authorizationToken) {
		this.authorizationToken = authorizationToken;
	}

	public String getUploadURI() {
		return uploadURI;
	}

	public String getAuthorizationToken() {
		return authorizationToken;
	}

	public void setUploadURI(String uploadURI) {
		this.uploadURI = uploadURI;
	}

	public String getZipLocation() {
		return zipLocation;
	}

	public void setZipLocation(String zipLocation) {
		this.zipLocation = zipLocation;
	}

	@Override
	public String toString() {
		return "ApigeeUploadObject [uploadURI=" + uploadURI + ", zipLocation=" + zipLocation + ", authorizationToken="
				+ authorizationToken + "]";
	}
	
	

}
