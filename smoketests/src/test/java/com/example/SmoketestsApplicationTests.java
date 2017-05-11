package com.example;

import io.restassured.response.Response;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.swing.text.html.Option;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.get;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmoketestsApplicationTests {
	private static String sleuthMessageClientEndpoint;
	private static String sleuthZipkinQueryServiceEndpoint;

	private Logger logger = LoggerFactory.getLogger(SmoketestsApplicationTests.class);

	@BeforeClass
	public static void setup() {
		sleuthMessageClientEndpoint = Optional.ofNullable(System.getenv("SLEUTH_MESSAGE_CLIENT_ENDPOINT"))
				.orElse("https://sleuth-message-client.cfapps.io");
		sleuthZipkinQueryServiceEndpoint = Optional.ofNullable(System.getenv("SLEUTH_ZIPKIN_QUERY_SERVICE_ENDPOINT"))
				.orElse("https://sleuth-zipkin-query-service.cfapps.io");
	}

	@Test
	public void smokeTestSleuth() throws InterruptedException {
		logger.info("sleuthMessageClientEndpoint: {}", sleuthMessageClientEndpoint);
		logger.info("sleuthZipkinQueryServiceEndpoint: {}", sleuthZipkinQueryServiceEndpoint);

		Response response = given().baseUri(sleuthMessageClientEndpoint).get("/message/template");
		response.then().statusCode(200).body("span", notNullValue());

		String span = response.body().path("span");

		TimeUnit.SECONDS.sleep(2);

		given().baseUri(sleuthZipkinQueryServiceEndpoint).get("/api/v1/trace/{span}", span)
				.then().body("$", hasSize(4));

	}

}
