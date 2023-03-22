package com.soob.blog.apiserver.keyword.controller;

import com.soob.blog.apiserver.keyword.dto.KeywordDto;
import com.soob.blog.apiserver.keyword.dto.KeywordHistoryDto;
import com.soob.blog.apiserver.keyword.dto.SearchRequestDto;
import com.soob.blog.apiserver.keyword.dto.SearchResponseDto;
import com.soob.blog.apiserver.keyword.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search/blog/keyword")
public class KeywordController {

	private final KeywordService keywordService;
	
	@GetMapping("")
	public SearchResponseDto findBlog(@Valid @RequestBody SearchRequestDto requestDto, HttpSession session) {
		String searchId = session.getId();
		return keywordService.findBlog(searchId, requestDto);
	}
	
	@GetMapping("/popularity")
	public List<KeywordDto> findKeyword() {
		return keywordService.findKeyword();
	}
	
	@GetMapping("/history")
	public List<KeywordHistoryDto> findKeywordHistory(@Nullable @RequestParam String date) {
		return keywordService.findKeywordHistory(date);
	}
}
