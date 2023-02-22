package br.com.roniebrunhara.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED)
public class UnauthorizedException extends RuntimeException {
	
	private static final long serialVersionUID = 1L;
	
	public UnauthorizedException() {
		super();
	}
	
	public UnauthorizedException(String message) {
		super(message);
	}
	
	public UnauthorizedException(Throwable throwable) {
		super(throwable);
	}
	
	public UnauthorizedException(String message, Throwable throwable) {
		super(message,throwable);
	}
	
}
