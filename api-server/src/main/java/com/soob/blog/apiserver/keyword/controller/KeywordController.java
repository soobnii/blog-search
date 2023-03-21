package com.soob.blog.apiserver.keyword.controller;

import com.soob.blog.apiserver.keyword.dto.KeywordDto;
import com.soob.blog.apiserver.keyword.dto.SearchRequestDto;
import com.soob.blog.apiserver.keyword.dto.SearchResponseDto;
import com.soob.blog.apiserver.keyword.service.KeywordService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/search/blog/keyword")
public class KeywordController {

	private final KeywordService keywordService;
	
	@GetMapping("")
	public SearchResponseDto findBlog(@RequestBody SearchRequestDto requestDto) {
		return keywordService.findBlog(requestDto);
	}
	
	@GetMapping("/popularity")
	public List<KeywordDto> findKeyword() {
		return keywordService.findKeyword();
	}
}
