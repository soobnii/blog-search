package com.soob.blog.apiserver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
public class InternalServerErrorException extends RuntimeException {
	
	private final String message;
	
	public InternalServerErrorException(String message) {
		this.message = message;
	}
	
	public InternalServerErrorException() {
		this.message = "서버 에러입니다";
	}
	
}
