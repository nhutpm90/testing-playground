package com.example.testing.jwt;

public class TokenException extends Exception {

	private static final long serialVersionUID = 1L;

	private String errorCode;

	public TokenException(String errorCode) {
		super(errorCode);
		this.errorCode = errorCode;
	}

	public TokenException(String errorCode, String msg) {
		super(msg);
		this.errorCode = errorCode;
	}

	public String getErrorCode() {
		return this.errorCode;
	}

	public void setErrorCode(String errorCode) {
		this.errorCode = errorCode;
	}
}