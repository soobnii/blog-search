package com.soob.blog.apiserver.keyword.enums;

import lombok.Getter;

@Getter
public enum SortCode {
	
	ACCURACY("A", "정확도"),
	RECENT("R", "최신순")
	;
	
	private final String code;
	private final String name;
	
	SortCode(String code, String name) {
		this.code = code;
		this.name = name;
	}
}
