package com.soob.blog.apiserver.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.List;

@RestControllerAdvice
public class ExceptionHandlers {
	
	private static final int BAD_REQUEST = HttpStatus.BAD_REQUEST.value();
	
	private static final int SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR.value();
	
	@ExceptionHandler(BadRequestException.class)
	protected ResponseEntity<ErrorResponse> handlerException(BadRequestException ex){
		ErrorResponse response = new ErrorResponse(BAD_REQUEST, ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.valueOf(BAD_REQUEST));
	}
	
	@ExceptionHandler(InternalServerErrorException.class)
	protected ResponseEntity<ErrorResponse> handlerException(InternalServerErrorException ex){
		ErrorResponse response = new ErrorResponse(SERVER_ERROR, ex.getMessage());
		return new ResponseEntity<>(response, HttpStatus.valueOf(SERVER_ERROR));
	}
	
	@ExceptionHandler(HttpMessageNotReadableException.class)
	protected ResponseEntity<ErrorResponse> handlerException(HttpMessageNotReadableException ex) {
		ErrorResponse response = new ErrorResponse(BAD_REQUEST, "잘못된 요청입니다.");
		return new ResponseEntity<>(response, HttpStatus.valueOf(BAD_REQUEST));
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	protected ResponseEntity<List<ErrorResponse>> handlerException(MethodArgumentNotValidException ex) {
		List<ErrorResponse> response = new ArrayList<>();
		List<ObjectError> list = ex.getBindingResult().getAllErrors();
		for (int i = 0; i < list.size(); i++) {
			response.add(new ErrorResponse(BAD_REQUEST, list.get(i).getDefaultMessage()));
		}
		return new ResponseEntity<>(response, HttpStatus.valueOf(BAD_REQUEST));
	}
	
	@ExceptionHandler(NullPointerException.class)
	protected ResponseEntity<ErrorResponse> handlerException(NullPointerException ex){
		ErrorResponse response = new ErrorResponse(SERVER_ERROR, "서버 에러입니다.");
		return new ResponseEntity<>(response, HttpStatus.valueOf(SERVER_ERROR));
	}
}
