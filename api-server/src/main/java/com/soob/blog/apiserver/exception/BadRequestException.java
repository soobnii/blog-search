package com.soob.blog.apiserver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.BAD_REQUEST)
@Getter
public class BadRequestException extends RuntimeException {
	
	private final String message;
	
	public BadRequestException(String message) {
		this.message = message;
	}
	
	public BadRequestException() {
		this.message = "잘못된 요청입니다.";
	}
}

