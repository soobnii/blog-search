package com.soob.blog.apiserver.keyword.dto;

import com.soob.blog.coreserver.entity.KeywordSearchHistory;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
public class KeywordHistoryDto {
	
	private String keyword;
	
	private LocalDate regDate;
	
	private LocalDateTime regDateTime;
	
	private String searchId;
	
	public KeywordHistoryDto(KeywordSearchHistory history) {
		this.keyword = history.getSearchKeyword();
		this.searchId = history.getSearchId();
		this.regDate = history.getRegDate();
		this.regDateTime = history.getRegDateTime();
	}
}
