package com.soob.blog.apiserver.keyword.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class SearchResponseDto {
	
	private Integer totalCount;
	
	private Integer pageableCount;
	
	private List<BlogDto> blog;
	
}
