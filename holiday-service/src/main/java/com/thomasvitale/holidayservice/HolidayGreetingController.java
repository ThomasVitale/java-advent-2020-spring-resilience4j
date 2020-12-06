package com.thomasvitale.holidayservice;

import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("holiday-greetings")
public class HolidayGreetingController {

	@GetMapping
	public Mono<String> getHolidayGreetings() {
		return Mono.just("I wish you happy holidays!");
	}
}
