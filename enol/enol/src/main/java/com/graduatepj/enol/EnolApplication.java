package com.graduatepj.enol;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.graduatepj.enol.service.CategorySearchService;
import com.graduatepj.enol.service.KeywordSearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import javax.annotation.PostConstruct;

@RestController
@SpringBootApplication
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

	// 아래부터 메인 실행시 같이 실행되는 코드
	@Autowired
	private CategorySearchService categorySearchService;

	@Autowired
	private KeywordSearchService keywordSearchService;

	@PostConstruct
	public void init() throws JsonProcessingException {
		Double xleft = 126.734086;
		Double yleft = 37.715133;
		Double xright = 127.269311;
		Double yright = 37.413294;
		//System.out.println("카테고리 검색");
		//categorySearchService.saveCategorySearch(xleft, yleft, xright, yright);
		System.out.println("여가시설");
		keywordSearchService.saveKeywordSearch(xleft, yleft, xright, yright);
	}

}
