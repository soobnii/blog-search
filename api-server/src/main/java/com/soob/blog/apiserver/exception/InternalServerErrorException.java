package com.soob.blog.apiserver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
@Getter
public class InternalServerErrorException extends RuntimeException {
	
	private int status;
	private String message;
	
	public InternalServerErrorException(String message) {
		this.status = HttpStatus.INTERNAL_SERVER_ERROR.value();
		this.message = message;
	}
	
}
