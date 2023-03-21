package com.soob.blog.apiserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.HttpServerErrorException;

@RestControllerAdvice
public class ExceptionHandlers {
	
	@ExceptionHandler(BadRequestException.class)
	protected ResponseEntity<ErrorResponse> handlerException(BadRequestException ex){
		ErrorResponse response = new ErrorResponse(ex.getStatus(), ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatus()));
	}
	
	@ExceptionHandler(InternalServerErrorException.class)
	protected ResponseEntity<ErrorResponse> handlerException(InternalServerErrorException ex){
		ErrorResponse response = new ErrorResponse(ex.getStatus(), ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.valueOf(ex.getStatus()));
	}
}
