package com.thomasvitale.greetingservice.client;

import java.io.IOException;

import okhttp3.mockwebserver.MockResponse;
import okhttp3.mockwebserver.MockWebServer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class HolidayClientTests {

	private MockWebServer mockWebServer;
	private String serverUrl;

	@Autowired
	private HolidayClient holidayClient;

	@BeforeEach
	void setup() throws IOException {
		this.mockWebServer = new MockWebServer();
		this.mockWebServer.start();
		this.serverUrl = mockWebServer.url("/").toString();
	}

	@AfterEach
	void clean() throws IOException {
		this.mockWebServer.shutdown();
	}

	@Test
	void whenRetrySuccessfulThenReturn() {
		String expectedGreeting = "I wish you happy holidays!";
		MockResponse mockResponseWithError = new MockResponse().setResponseCode(503);
		MockResponse mockResponse = new MockResponse().setBody(expectedGreeting);
		mockWebServer.enqueue(mockResponseWithError);
		mockWebServer.enqueue(mockResponseWithError);
		mockWebServer.enqueue(mockResponse);

		Mono<String> actualGreeting = holidayClient.getHolidayGreetingWithRetries(serverUrl);

		StepVerifier.create(actualGreeting)
				.expectNextMatches(g -> g.equals(expectedGreeting))
				.verifyComplete();
	}

	@Test
	void whenRetryNotSuccessfulThenThrows() {
		MockResponse mockResponseWithError = new MockResponse().setResponseCode(503);
		mockWebServer.enqueue(mockResponseWithError);
		mockWebServer.enqueue(mockResponseWithError);
		mockWebServer.enqueue(mockResponseWithError);

		Mono<String> actualGreeting = holidayClient.getHolidayGreetingWithRetries(serverUrl);

		StepVerifier.create(actualGreeting)
				.expectNextMatches(g -> g.equals(HolidayClient.DEFAULT_GREETINGS))
				.verifyComplete();
	}
}