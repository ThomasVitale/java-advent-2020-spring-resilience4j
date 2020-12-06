package com.thomasvitale.greetingservice.client;

import io.github.resilience4j.bulkhead.annotation.Bulkhead;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.github.resilience4j.retry.annotation.Retry;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
@Log4j2
@RequiredArgsConstructor
public class HolidayClient {

	public static final String DEFAULT_GREETINGS = "Happy holidays!";

	private final WebClient webClient;

	public Mono<String> getHolidayGreetings(String url) {
		return webClient.get().uri(url)
				.retrieve()
				.bodyToMono(String.class);
	}

	@Retry(name = "holidayClient", fallbackMethod = "fallback")
	public Mono<String> getHolidayGreetingsWithRetries(String url) {
		return getHolidayGreetings(url);
	}

	@RateLimiter(name = "holidayClient", fallbackMethod = "fallback")
	public Mono<String> getHolidayGreetingsWithRateLimiter(String url) {
		return getHolidayGreetings(url);
	}

	@Bulkhead(name = "holidayClient", fallbackMethod = "fallback")
	public Mono<String> getHolidayGreetingsWithBulkhead(String url) {
		return getHolidayGreetings(url);
	}

	public Mono<String> fallback(String url, Throwable throwable) {
		log.error("Fallback executed for {} with {}", url, throwable.toString());
		return Mono.just(DEFAULT_GREETINGS);
	}
}
