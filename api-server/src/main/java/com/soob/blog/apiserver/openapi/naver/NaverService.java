package com.soob.blog.apiserver.openapi.naver;

import com.google.gson.Gson;
import com.soob.blog.apiserver.exception.BadRequestException;
import com.soob.blog.apiserver.exception.InternalServerErrorException;
import com.soob.blog.apiserver.keyword.dto.SearchRequestDto;
import com.soob.blog.apiserver.keyword.dto.SearchResponseDto;
import com.soob.blog.apiserver.keyword.enums.SortCode;
import com.soob.blog.apiserver.openapi.naver.model.NaverResponseModel;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class NaverService {
	
	@Value("${api.naver.host}")
	private String host;
	
	@Value("${api.naver.clientId}")
	private String clientId;
	
	@Value("${api.naver.clientSecret}")
	private String clientSecret;
	
	private static final String NAVER_SEARCH_BLOG_URL = "/v1/search/blog";
	
	public SearchResponseDto searchBlog(SearchRequestDto requestDto) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("X-Naver-Client-Id", clientId);
		headers.add("X-Naver-Client-Secret", clientSecret);
		headers.add("Content-Type", "application/json;charset=UTF-8");
		
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("query", requestDto.getQuery());
		if(requestDto.getSort() != null) {
			String sort = "sim";
			if(requestDto.getSort().equals(SortCode.RECENT)) {
				sort = "date";
			}
			queryParams.add("sort", sort);
		}
		if(requestDto.getSize() != null) {
			queryParams.add("display", String.valueOf(requestDto.getSize()));
		}
		if(requestDto.getPage() != null) {
			queryParams.add("start", String.valueOf(requestDto.getPage()));
		}
		
		UriComponents completedUri = UriComponentsBuilder
				.fromHttpUrl(host + NAVER_SEARCH_BLOG_URL)
				.queryParams(queryParams)
				.build();
		
		SearchResponseDto responseDto = null;
		try {
			HttpEntity<String> request = new HttpEntity<>(headers);
			ResponseEntity<NaverResponseModel> response = restTemplate
				.exchange(completedUri.toString(), HttpMethod.GET, request, NaverResponseModel.class);
			NaverResponseModel model = response.getBody();
			
			if(model != null) {
				responseDto = model.toDto();
			}
		} catch (HttpStatusCodeException exception) {
			if(!exception.getStatusCode().is2xxSuccessful()) {
				String messageStr = exception.getResponseBodyAsString();
				Gson gson = new Gson();
				HashMap<String, String> map = gson.fromJson(messageStr, HashMap.class);
				String messages = map.get("errorMessage");
				if(exception.getStatusCode().is5xxServerError()) {
					throw new InternalServerErrorException(messages);
				} else {
					throw new BadRequestException(messages);
				}
			}
		} catch (Exception exception) {
			throw new InternalServerErrorException();
		}
		
		return responseDto;
	}
}
