package com.soob.blog.apiserver.keyword.controller;

import com.google.gson.Gson;
import com.soob.blog.apiserver.exception.BadRequestException;
import com.soob.blog.apiserver.keyword.dto.BlogDto;
import com.soob.blog.apiserver.keyword.dto.KeywordDto;
import com.soob.blog.apiserver.keyword.dto.KeywordHistoryDto;
import com.soob.blog.apiserver.keyword.dto.SearchRequestDto;
import com.soob.blog.apiserver.keyword.dto.SearchResponseDto;
import com.soob.blog.apiserver.keyword.enums.SortCode;
import com.soob.blog.apiserver.keyword.service.KeywordService;
import com.soob.blog.coreserver.entity.KeywordSearchCnt;
import com.soob.blog.coreserver.entity.KeywordSearchHistory;
import com.soob.blog.coreserver.repository.KeywordRepository;
import com.soob.blog.coreserver.repository.KeywordSearchCntRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class KeywordControllerTest {
	
	@Autowired
	KeywordService keywordService;
	
	@Autowired
	KeywordRepository keywordRepository;
	
	@Autowired
	KeywordSearchCntRepository keywordSearchCntRepository;
	
	@Autowired
	MockMvc mockMvc;

	@Test
	void findBlogBase() {
		SearchRequestDto requestDto = SearchRequestDto.builder()
				.query(" 행복  성장  희망 ")
				.build();
		
		keywordService.findBlog("sessionid", requestDto);
		
		String[] keywords = requestDto.getQuery().split(" ");
		for (String key : keywords) {
			if(StringUtils.hasText(key.trim())) {
				KeywordSearchCnt result = keywordSearchCntRepository.findBySearchKeyword(key).orElse(null);
				assertThat(result.getSearchKeyword()).isEqualTo(key);
			}
		}
		
		List<KeywordSearchHistory> list = keywordRepository.findAll();
		assertThat(list).hasSize(3);
	}
	
	@Test
	void findBlogPagingSorting() {
		SearchRequestDto requestDto = SearchRequestDto.builder()
				.query("행복 성장 희망")
				.size(50)
				.page(1)
				.sort(SortCode.RECENT)
				.build();
		
		SearchResponseDto responseDto = keywordService.findBlog("sessionid", requestDto);
		
		List<BlogDto> list = responseDto.getBlog();
		assertThat(list).hasSize(50);
		for (int i = 1; i < list.size(); i++) {
			assertThat(list.get(i-1).getDate().compareTo(list.get(i).getDate()))
					.isGreaterThanOrEqualTo(0);
		}
	}
	
	@Test
	void findBlogAnotherApi() {
		SearchRequestDto requestDto = SearchRequestDto.builder()
				.query("행복 성장 희망")
				.size(100)
				.page(1)
				.type("naver")
				.sort(SortCode.RECENT)
				.build();
		
		SearchResponseDto responseDto = keywordService.findBlog("sessionid", requestDto);
		
		List<BlogDto> list = responseDto.getBlog();
		assertThat(list).hasSize(100);
		for (int i = 1; i < list.size(); i++) {
			assertThat(list.get(i-1).getDate().compareTo(list.get(i).getDate()))
					.isGreaterThanOrEqualTo(0);
		}
	}
	
	@Test
	void errorMessageTest() {
		SearchRequestDto requestDto = SearchRequestDto.builder()
				.build();
		
		BadRequestException ex = assertThrows(BadRequestException.class, () -> {
			keywordService.findBlog("sessionid", requestDto);
		});
		
		assertThat(ex.getMessage()).isEqualTo("query parameter required");
	}
	
	@Test
	void errorControllerMessageTest() throws Exception {
		mockMvc.perform(get("/search/blog/keyword"))
			.andExpect(status().isBadRequest())
			.andExpect(result -> {
				Gson gson = new Gson();
				HashMap<String, String> map = gson.fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8), HashMap.class);
				String message = map.get("message");
				assertEquals("잘못된 요청입니다.", message);
			});
	}
	
	@Test
	void findKeyword() {
		String[] keywordList = {"행복 성장 희망", "행복 과제 테스트", " 행복 ", "과제", "휴대폰", "노트북", "카카오", "은행 뱅크"
			, "카카오", "카카오 뱅크", "카카오 은행", "행복 테스트", "모니터 행복", "모니터 키보드", "모니터  마우스"};
		
		for (String keyword : keywordList) {
			SearchRequestDto requestDto = SearchRequestDto.builder()
				.query(keyword)
				.build();
			keywordService.findBlog("sessionid", requestDto);
		}
		
		List<KeywordDto> list = keywordService.findKeyword();
		assertThat(list).hasSize(10);
	
		int[] resultInt = {5, 4, 3, 2, 2, 2, 2, 1, 1, 1};
		String[] resultStr = {"행복", "카카오", "모니터", "테스트", "은행", "뱅크", "과제", "마우스", "키보드", "노트북"};
		
		for (int i = 1; i < list.size(); i++) {
			Integer topCount = list.get(i-1).getSearchCount();
			Integer nextCount = list.get(i).getSearchCount();
			assertThat(topCount.compareTo(nextCount))
					.isGreaterThanOrEqualTo(0);
			assertThat(topCount).isEqualTo(resultInt[i-1]);
			assertThat(nextCount).isEqualTo(resultInt[i]);
			assertThat(list.get(i).getKeyword()).isEqualTo(resultStr[i]);
			assertThat(list.get(i-1).getKeyword()).isEqualTo(resultStr[i-1]);
		}
	}
	
	@Test
	void findHistory() throws Exception {
		String[] keywordList = {"행복 성장 희망", "행복 과제 테스트", " 행복 ", "과제", "휴대폰", "노트북", "카카오", "은행 뱅크"
			, "카카오", "카카오 뱅크", "카카오 은행", "행복 테스트", "모니터 행복", "모니터 키보드", "모니터  마우스"};
		
		for (String keyword : keywordList) {
			SearchRequestDto requestDto = SearchRequestDto.builder()
				.query(keyword)
				.build();
			keywordService.findBlog("sessionid", requestDto);
		}
		
		List<KeywordHistoryDto> list = keywordService.findKeywordHistory(null);
		assertThat(list).hasSize(26);
		
		mockMvc.perform(get("/search/blog/keyword/history?date=20229999"))
			.andExpect(status().isBadRequest())
			.andExpect(result -> {
				Gson gson = new Gson();
				HashMap<String, String> map = gson.fromJson(result.getResponse().getContentAsString(StandardCharsets.UTF_8), HashMap.class);
				String message = map.get("message");
				assertEquals("입력한 날짜를 확인해주세요. 형식은 yyyy-MM-dd 이어야 합니다.", message);
			});
	}
}