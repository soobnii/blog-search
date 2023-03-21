package com.soob.blog.apiserver.openapi.naver.model;

import com.soob.blog.apiserver.keyword.dto.BlogDto;
import com.soob.blog.apiserver.keyword.dto.SearchResponseDto;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class NaverResponseModel {
	
	public String lastBuildDate;
	
	public Integer total;
	
	public Integer start;
	
	public Integer display;
	
	public List<ItemModel> items;
	
	public SearchResponseDto toDto() {
		List<BlogDto> blogList = new ArrayList<>();
		this.items.forEach(item -> {
				String postDate = item.getPostdate();
				blogList.add(BlogDto.builder()
								.bloggername(item.getBloggername())
								.contents(item.getDescription())
								.date(LocalDate.parse(postDate, DateTimeFormatter.ofPattern("yyyyMMdd")))
								.title(item.getTitle())
								.url(item.getLink())
								.build());
			});
		
		return SearchResponseDto.builder()
				.totalCount(this.total)
				.blog(blogList)
				.build();
	}
}
