package io.pivotal.rest;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
@EnableScheduling
public class WebConsumerApplication {

	public static void main(String[] args) {
		SpringApplication.run(WebConsumerApplication.class, args);
	}

	@Bean
	WebClient webClient() {
		return WebClient.builder()
				.baseUrl("http://localhost:8085")
				.build();
	}
}
