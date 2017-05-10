package com.example;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.cloud.sleuth.Sampler;
import org.springframework.cloud.stream.annotation.EnableBinding;
import org.springframework.cloud.stream.messaging.Sink;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.integration.annotation.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

@EnableDiscoveryClient
@EnableFeignClients
//@EnableAspectJAutoProxy(proxyTargetClass = true)
@EnableBinding(Sink.class)
@IntegrationComponentScan
@SpringBootApplication
public class MessageClientApplication {
	public static final String SLEUTH_TEST = "sleuth-test";
	private Logger logger = LoggerFactory.getLogger(MessageClientApplication.class);

	/**
	 * the service with which we're communicating
	 */
	public static final String ZIPKIN_CLIENT_B = "message-service";

	@Bean
	Sampler sampler() {
		return span -> true;
	}

	@LoadBalanced
	@Bean RestTemplate restTemplate() {
		return new RestTemplate();
	}

	public static void main(String[] args) {
		SpringApplication.run(MessageClientApplication.class, args);
	}

	@RabbitListener(queues = SLEUTH_TEST)
	public void process(String message) {
		logger.info("Received message : {}", message);
	}

	@Bean
	public Queue queue() {
		return new Queue(SLEUTH_TEST, false);
	}
}

@FeignClient(serviceId = MessageClientApplication.ZIPKIN_CLIENT_B)
interface RestMessageReader {

	@RequestMapping(
			method = RequestMethod.GET,
			value = "/",
			consumes = MediaType.APPLICATION_JSON_VALUE)
	Map<String, String> readMessage();
}

@RestController
@RequestMapping("/message")
class MessageClientRestController {

	@Autowired
	private RestTemplate restTemplate;

	@Autowired
	private RestMessageReader restReader;


	@RequestMapping("/template")
	ResponseEntity<Map<String, String>> template() {

		String url = "//" + MessageClientApplication.ZIPKIN_CLIENT_B;

		ParameterizedTypeReference<Map<String, String>> ptr =
				new ParameterizedTypeReference<Map<String, String>>() {
				};

		ResponseEntity<Map<String, String>> responseEntity =
				this.restTemplate.exchange(url, HttpMethod.GET, null, ptr);

		return ResponseEntity
				.ok()
				.contentType(responseEntity.getHeaders().getContentType())
				.body(responseEntity.getBody());
	}

	@RequestMapping("/feign")
	Map<String, String> feign() {
		return this.restReader.readMessage();
	}
}


