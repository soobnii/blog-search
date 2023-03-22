package com.soob.blog.apiserver.keyword.dto;

import com.soob.blog.apiserver.keyword.enums.SortCode;
import lombok.Builder;
import lombok.Getter;

import javax.validation.constraints.NotBlank;

@Getter
@Builder
public class SearchRequestDto {
	
	@NotBlank(message = "query는 필수값입니다.")
	private String query;
	
	private SortCode sort;
	
	private Integer page;
	
	private Integer size;
	
	private String type;
	
}
