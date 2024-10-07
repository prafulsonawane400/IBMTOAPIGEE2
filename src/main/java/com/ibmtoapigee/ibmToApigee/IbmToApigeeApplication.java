package com.ibmtoapigee.ibmToApigee;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class IbmToApigeeApplication {

	public static void main(String[] args) throws Exception {
		SpringApplication.run(IbmToApigeeApplication.class, args);
	}

	@Bean
	public WebClient webClient() {
		WebClient webClient = WebClient.builder().exchangeStrategies(ExchangeStrategies.builder()
				.codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(524288)).build()).build();
		return webClient;
	}

}
