package com.example;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Source;
import org.springframework.context.annotation.Bean;
import org.springframework.integration.annotation.IntegrationComponentScan;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@EnableDiscoveryClient
@IntegrationComponentScan
@SpringBootApplication
@EnableBinding(Source.class)
public class MessageServiceApplication {
    static String SLEUTH_TEST = "sleuth-test";

	@Bean
	Sampler sampler() {
		return span -> true;
	}

	public static void main(String[] args) {
		SpringApplication.run(MessageServiceApplication.class, args);
	}
}


@RestController
class MessageServiceRestController {

	@Autowired
    Source replySender;

	@RequestMapping("/")
	Map<String, String> message(HttpServletRequest httpRequest) {

		List<String> headers = Arrays.asList("x-span-id",
				"x-span-name", "x-trace-id");

		String key = "message";

		Map<String, String> response = new HashMap<>();

		String value = "Hi, from a REST endpoint: " + System.currentTimeMillis();

		response.put(key, value);

		headers
				.stream()
				.filter(h -> httpRequest.getHeader(h) != null)
				.forEach(h -> response.put(h, httpRequest.getHeader(h)));

		this.replySender.output().send(MessageBuilder.withPayload(value).build());

		return response;
	}
}
