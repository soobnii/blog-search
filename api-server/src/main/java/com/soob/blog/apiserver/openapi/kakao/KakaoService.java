package com.soob.blog.apiserver.openapi.kakao;

import com.google.gson.Gson;
import com.soob.blog.apiserver.exception.BadRequestException;
import com.soob.blog.apiserver.exception.InternalServerErrorException;
import com.soob.blog.apiserver.keyword.dto.SearchRequestDto;
import com.soob.blog.apiserver.keyword.dto.SearchResponseDto;
import com.soob.blog.apiserver.keyword.enums.SortCode;
import com.soob.blog.apiserver.openapi.kakao.model.KakaoResponseModel;
import com.soob.blog.apiserver.openapi.naver.NaverService;
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
public class KakaoService {
	
	@Value("${api.kakao.host}")
	private String host;
	
	@Value("${api.kakao.apiKey}")
	private String apiKey;
	
	private static final String KAKAO_BLOG_SEARCH_URL = "/v2/search/blog";
	
	private final NaverService naverService;
	
	public SearchResponseDto searchBlog(SearchRequestDto requestDto) {
		RestTemplate restTemplate = new RestTemplate();
		HttpHeaders headers = new HttpHeaders();
		headers.add("Authorization", apiKey);
		headers.add("Content-Type", "application/json;charset=UTF-8");
		
		MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
		queryParams.add("query", requestDto.getQuery());
		if(requestDto.getSort() != null) {
			String sort = "accuracy";
			if(requestDto.getSort().equals(SortCode.RECENT)) {
				sort = "recency";
			}
			queryParams.add("sort", sort);
		}
		if(requestDto.getSize() != null) {
			queryParams.add("size", String.valueOf(requestDto.getSize()));
		}
		if(requestDto.getPage() != null) {
			queryParams.add("page", String.valueOf(requestDto.getPage()));
		}
		
		UriComponents completedUri = UriComponentsBuilder
				.fromHttpUrl(host + KAKAO_BLOG_SEARCH_URL)
				.queryParams(queryParams)
				.build();
		
		SearchResponseDto responseDto = null;
		try {
			HttpEntity<String> request = new HttpEntity<>(headers);
			ResponseEntity<KakaoResponseModel> response = restTemplate
				.exchange(completedUri.toString(), HttpMethod.GET, request, KakaoResponseModel.class);
			KakaoResponseModel model = response.getBody();
			
			if(model != null) {
				responseDto = model.toDto();
			}
		} catch (HttpStatusCodeException exception) {
			if(!exception.getStatusCode().is2xxSuccessful()) {
				if(exception.getStatusCode().is5xxServerError()) {
					responseDto = naverService.searchBlog(requestDto);
				} else {
					String messageStr = exception.getResponseBodyAsString();
					Gson gson = new Gson();
					HashMap<String, String> map = gson.fromJson(messageStr, HashMap.class);
					throw new BadRequestException(map.get("message"));
				}
			}
		} catch (Exception exception) {
			throw new InternalServerErrorException();
		}
		
		return responseDto;
	}
}
