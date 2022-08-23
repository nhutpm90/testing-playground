package com.example.testing.jwt;

public class SecurityConstants {

	public static final String JWT_KEY = "jxgEQeXHuPq8VdbyYFNkANdudQ53YUn4";
	public static final Integer JWT_EXPIRATION_IN_MS = 10 * 60 * 1000; // 10 minutes
	public static final Integer REFRESH_TOKEN_EXPIRATION_IN_MS = 10 * 60 * 1000; // 10 minutes
	
	public static final String JWT_HEADER = "Authorization";

}