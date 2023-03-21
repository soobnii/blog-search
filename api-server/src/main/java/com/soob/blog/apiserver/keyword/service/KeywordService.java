package com.soob.blog.apiserver.keyword.service;


import com.soob.blog.apiserver.keyword.dto.KeywordDto;
import com.soob.blog.apiserver.keyword.dto.SearchRequestDto;
import com.soob.blog.apiserver.keyword.dto.SearchResponseDto;

import java.util.List;

public interface KeywordService {
	
	SearchResponseDto findBlog(SearchRequestDto requestDto);
	
	List<KeywordDto> findKeyword();
}
