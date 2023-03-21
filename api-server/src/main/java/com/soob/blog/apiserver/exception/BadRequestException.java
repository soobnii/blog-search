package com.soob.blog.apiserver.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(code = HttpStatus.BAD_REQUEST)
@Getter
public class BadRequestException extends RuntimeException {
	
	private int status;
	private String message;
	
	public BadRequestException(String message) {
		this.status = HttpStatus.BAD_REQUEST.value();
		this.message = message;
	}
}

