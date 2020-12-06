# Resilient applications with Spring and Resilience4J (Java Advent 2020)

Examples of resilience patterns implemented using Spring Boot and Resilience4j. Presented at JavaAdvent 2020.

## Description

Greeting Service is a simple application exposing endpoints to return greetings. The latter are fetched from a second application, Holiday Service, that returns holiday greetings. The integration point between Greeting Service and Holiday Service is enhanced by applying retries, rate limiters, and bulkheads.

## Testing

### Bulkhead

You can test the bulkhead feature with a tool like [Apache Benchmark](https://httpd.apache.org/docs/2.4/programs/ab.html).

Firing 50 requests to the `/bulkhead` endpoint concurrently should result in at least a few failed requests.
The test can give different results depending on your system.

````bash
ab -n 50 -c 50 http://localhost:8080/greetings/bulkhead
````

The application logs will show an event for each failed request, like the following.

````log
2020-12-07 00:23:51.535 ERROR 14191 --- [ctor-http-nio-3] c.t.g.client.HolidayClient               : Fallback executed for http://localhost:8181/holiday-greetings with io.github.resilience4j.bulkhead.BulkheadFullException: Bulkhead 'holidayClient' is full and does not permit further calls
````

### Rate Limiter

You can test the rate limiter feature with a tool like [Apache Benchmark](https://httpd.apache.org/docs/2.4/programs/ab.html).

Firing 20 requests to the `/rate-limiter` endpoint should result in 10 successful requests, and 10 failed requests.

````bash
ab -n 20 http://localhost:8080/greetings/rate-limiter
````

The application logs will show an event for each failed request, like the following.

````log
2020-12-07 00:17:57.518 ERROR 14191 --- [ctor-http-nio-6] c.t.g.client.HolidayClient: Fallback executed for http://localhost:8181/holiday-greetings with io.github.resilience4j.ratelimiter.RequestNotPermitted: RateLimiter 'holidayClient' does not permit further calls
````

### Retry

You can test the retry functionality in an auto-test, leveraging a mock web server like the one provided by OkHTTP3.
The `HolidayClientTests` class contains tests covering when the retry component returns successfully, and when the fallback is executed due to an error.

## Resources

### Websites

* [Resilience4J](https://resilience4j.readme.io/)
* [Spring Cloud Circuit Breaker](https://spring.io/projects/spring-cloud-circuitbreaker)

### Books

* "[Cloud Native Spring in Action](https://www.manning.com/books/cloud-native-spring-in-action?utm_source=affiliate&utm_medium=affiliate&a_aid=thomas&a_bid=3dda43a8)", Thomas Vitale, Manning
* "[Reactive Spring](https://reactivespring.io/)", Josh Long
* "[Release It! Design and Deploy Production-Ready Software](https://pragprog.com/titles/mnee2/release-it-second-edition/)", Michael T. Nygard, The Pragmatic Programmers
