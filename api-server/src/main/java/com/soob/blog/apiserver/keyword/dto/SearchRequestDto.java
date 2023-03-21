package com.soob.blog.apiserver.keyword.dto;

import com.soob.blog.apiserver.keyword.enums.SortCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SearchRequestDto {
	
	private String query;
	
	private SortCode sort;
	
	private Integer page;
	
	private Integer size;
	
	private String oepnApi;
	
}
