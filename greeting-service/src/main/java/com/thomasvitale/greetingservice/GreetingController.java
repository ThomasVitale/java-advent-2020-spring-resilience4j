package com.thomasvitale.greetingservice;

import com.thomasvitale.greetingservice.client.HolidayClient;
import lombok.RequiredArgsConstructor;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("greetings")
@RequiredArgsConstructor
public class GreetingController {

	private static final String GREETING_SERVER_URL = "http://localhost:8181";
	private final HolidayClient holidayClient;

	@GetMapping
	public Mono<String> getGreetings() {
		return holidayClient.getHolidayGreetings(GREETING_SERVER_URL);
	}

	@GetMapping("bulkhead")
	public Mono<String> getGreetingsWithBulkhead() {
		return holidayClient.getHolidayGreetingWithBulkhead(GREETING_SERVER_URL + "/bulkhead");
	}

	@GetMapping("rate-limiter")
	public Mono<String> getGreetingsWithRateLimiter() {
		return holidayClient.getHolidayGreetingWithRateLimiter(GREETING_SERVER_URL + "/rate-limiter");
	}

	@GetMapping("retry")
	public Mono<String> getGreetingsWithRetry() {
		return holidayClient.getHolidayGreetingWithRetries(GREETING_SERVER_URL + "/retries");
	}
}
