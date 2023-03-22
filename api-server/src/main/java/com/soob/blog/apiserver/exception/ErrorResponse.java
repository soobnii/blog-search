package com.soob.blog.apiserver.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
	
	private Integer status;
	private String message;
	
	public ErrorResponse(int status, String message) {
		this.status = status;
		this.message = message;
	}
	
}
