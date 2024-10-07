package com.ibmtoapigee.ibmToApigee.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;

@Component
public class ExternalApiCallService {

	public ExternalApiCallService() {
		// TODO Auto-generated constructor stub
	}

	@Autowired
	private WebClient webClient; // null

	public String postApiResponse(String url, String reqContent) {
		try {
			Mono<String> response = webClient.post().uri(url).header("Content-Type", "application/json")
					.bodyValue(reqContent).retrieve().bodyToMono(String.class);

			String output = response.block();
			return output;
		} catch (Exception e) {
			e.printStackTrace();
			return e.getMessage();
		}
	}
}
