package com.message.central.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.message.central.responses.ExceptionResponse;

import jakarta.servlet.http.HttpServletRequest;

@ControllerAdvice
public class GlobalHanderException {

	@ExceptionHandler(EmailAlreadyExistsException.class)
	public ResponseEntity<ExceptionResponse> emailAlreadyExists(EmailAlreadyExistsException ex,
			HttpServletRequest request) {
		ExceptionResponse error = new ExceptionResponse(HttpStatus.CONFLICT.value(), "Conflict.", ex.getMessage());

		return new ResponseEntity<>(error, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(NotFoundException.class)
	public ResponseEntity<ExceptionResponse> userNotFound(NotFoundException ex,
			HttpServletRequest request) {
		ExceptionResponse error = new ExceptionResponse(HttpStatus.NOT_FOUND.value(), "Not Found.", ex.getMessage());

		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InsufficienteCharactersException.class)
	public ResponseEntity<ExceptionResponse> insufficientCharacters(InsufficienteCharactersException ex,
			HttpServletRequest request) {
		ExceptionResponse error = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request.", ex.getMessage());
		
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidPermissionTypeException.class)
	public ResponseEntity<ExceptionResponse> invalidPermissionType(InvalidPermissionTypeException ex,
			HttpServletRequest request) {
		ExceptionResponse error = new ExceptionResponse(HttpStatus.BAD_REQUEST.value(), "Bad Request.", ex.getMessage());
		
		return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(InvalidCredentials.class)
	public ResponseEntity<ExceptionResponse> invalidCredenitials(InvalidCredentials ex,
			HttpServletRequest request) {
		ExceptionResponse error = new ExceptionResponse(HttpStatus.UNAUTHORIZED.value(), "Unauthorized.", ex.getMessage());
		
		return new ResponseEntity<>(error, HttpStatus.UNAUTHORIZED);
	}
}
