package com.soob.blog.apiserver.keyword.controller;

import com.soob.blog.apiserver.exception.BadRequestException;
import com.soob.blog.apiserver.keyword.dto.BlogDto;
import com.soob.blog.apiserver.keyword.dto.KeywordDto;
import com.soob.blog.apiserver.keyword.dto.SearchRequestDto;
import com.soob.blog.apiserver.keyword.dto.SearchResponseDto;
import com.soob.blog.apiserver.keyword.enums.SortCode;
import com.soob.blog.apiserver.keyword.service.KeywordService;
import com.soob.blog.coreserver.entity.Keyword;
import com.soob.blog.coreserver.repository.KeywordRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@Transactional
class KeywordControllerTest {
	
	@Autowired
	KeywordService keywordService;
	
	@Autowired
	KeywordRepository keywordRepository;
	
	@Test
	void findBlogBase() {
		SearchRequestDto requestDto = SearchRequestDto.builder()
				.query(" 행복  성장  희망 ")
				.build();
		
		keywordService.findBlog(requestDto);
		
		String[] keywords = requestDto.getQuery().split(" ");
		for (String key : keywords) {
			if(StringUtils.hasText(key.trim())) {
				Keyword result = keywordRepository.findByKeyword(key).orElse(null);
				assertThat(result.getKeyword()).isEqualTo(key);
			}
		}
		
		List<Keyword> list = keywordRepository.findAll();
		assertThat(list.size()).isEqualTo(3);
	}
	
	@Test
	void findBlogPagingSorting() {
		SearchRequestDto requestDto = SearchRequestDto.builder()
				.query("행복 성장 희망")
				.size(50)
				.page(1)
				.sort(SortCode.RECENT)
				.build();
		
		SearchResponseDto responseDto = keywordService.findBlog(requestDto);
		
		List<BlogDto> list = responseDto.getBlog();
		assertThat(list.size()).isEqualTo(50);
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
				.oepnApi("naver")
				.sort(SortCode.RECENT)
				.build();
		
		SearchResponseDto responseDto = keywordService.findBlog(requestDto);
		
		List<BlogDto> list = responseDto.getBlog();
		assertThat(list.size()).isEqualTo(100);
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
			keywordService.findBlog(requestDto);
		});
		
		// kakao 에서 제공하는 메시지와 동일한지 체크
		assertThat(ex.getMessage()).isEqualTo("query parameter required");

	}
	
	@Test
	void findKeyword() {
		String[] keywordList = {"행복 성장 희망", "행복 과제 테스트", " 행복 ", "과제", "휴대폰", "노트북", "카카오", "은행 뱅크"
			, "카카오", "카카오 뱅크", "카카오 은행", "행복 테스트", "모니터 행복", "모니터 키보드", "모니터  마우스"};
		
		for (String keyword : keywordList) {
			SearchRequestDto requestDto = SearchRequestDto.builder()
				.query(keyword)
				.build();
			keywordService.findBlog(requestDto);
		}
		
		List<KeywordDto> list = keywordService.findKeyword();
		assertThat(list.size()).isEqualTo(10);
		
		int[] resultArr = {5, 4, 3, 2, 2, 2, 2, 1, 1, 1};
		
		for (int i = 1; i < list.size(); i++) {
			Integer topCount = list.get(i-1).getSearchCount();
			Integer nextCount = list.get(i).getSearchCount();
			assertThat(topCount.compareTo(nextCount))
					.isGreaterThanOrEqualTo(0);
			assertThat(topCount).isEqualTo(resultArr[i-1]);
			assertThat(nextCount).isEqualTo(resultArr[i]);
			/*System.out.println(list.get(i-1).getKeyword() + " >= "
					+ list.get(i).getKeyword());*/
		}
		
	}
}