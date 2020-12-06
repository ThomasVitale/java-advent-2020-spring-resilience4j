package com.thomasvitale.greetingservice.client;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class ClientConfig {

	@Bean
	WebClient webClient(WebClient.Builder webClientBuilder) {
		return webClientBuilder.build();
	}
}
