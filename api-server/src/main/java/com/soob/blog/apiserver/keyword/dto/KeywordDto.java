package com.soob.blog.apiserver.keyword.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class KeywordDto {
	
	private String keyword;
	
	private Integer searchCount;
	
}
