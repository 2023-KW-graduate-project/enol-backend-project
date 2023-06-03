package com.graduatepj.enol;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
@SpringBootApplication
@RequiredArgsConstructor
@EnableMongoRepositories
public class EnolApplication {


	@Value("${kakao.rest.api.key}")
	private String restApiKey;

	@GetMapping("/test")
	public String testKakaoAPI() {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.set("Authorization", "KakaoAK "+restApiKey);
		UriComponentsBuilder builder = UriComponentsBuilder.fromHttpUrl("https://dapi.kakao.com/v2/local/search/address.json")
				.queryParam("query", "서울시 강남구 역삼동 1-2");

		HttpEntity<?> entity = new HttpEntity<>(headers);
		ResponseEntity<String> response = restTemplate.exchange(
				builder.toUriString(),
				HttpMethod.GET,
				entity,
				String.class
		);
		return response.getBody();
	}

	public static void main(String[] args) {
		SpringApplication.run(EnolApplication.class, args);
	}

}
