package com.soob.blog.apiserver.openapi.kakao.model;

import lombok.Getter;

import java.util.Date;

@Getter
public class DocumentModel {
	
	private String title;
	
	private String contents;
	
	private String url;
	
	private String blogname;
	
	private String thumbnail;
	
	private Date datetime;
}
