package com.soob.blog.apiserver.keyword.dto;

import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Builder
@Getter
public class BlogDto {
	
	private String title;
	
	private String contents;
	
	private String url;
	
	private String blogname;
	
	private String thumbnail;
	
	private LocalDate date;
	
}
