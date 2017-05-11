package com.example;

import io.restassured.response.Response;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.get;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SmoketestsApplicationTests {

	@Test
	public void smokeTestSleuth() throws InterruptedException {
		Response response = get("https://sleuth-message-client.cfapps.io/message/template");
		response.then().statusCode(200).body("span", notNullValue());

		String span = response.body().path("span");
		TimeUnit.SECONDS.sleep(2);
		get("https://sleuth-zipkin-query-service.cfapps.io/api/v1/trace/{span}", span)
				.then().body("$", hasSize(4));

	}

}
