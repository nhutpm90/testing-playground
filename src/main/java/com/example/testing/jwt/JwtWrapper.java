package com.example.testing.jwt;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

public class JwtWrapper {

	@JsonInclude(Include.NON_NULL)
	private String username;

	@JsonInclude(Include.NON_NULL)
	private String token;

	@JsonInclude(Include.NON_NULL)
	private String refreshToken;

	@JsonInclude(Include.NON_NULL)
	private Set<String> roles;

	public JwtWrapper() {
	}

	public JwtWrapper(String username, String token, String refreshToken, Set<String> roles) {
		this.username = username;
		this.token = token;
		this.refreshToken = refreshToken;
		this.roles = roles;
	}

	public JwtWrapper(String token, String refreshToken) {
		this.token = token;
		this.refreshToken = refreshToken;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getRefreshToken() {
		return refreshToken;
	}

	public void setRefreshToken(String refreshToken) {
		this.refreshToken = refreshToken;
	}

	public Set<String> getRoles() {
		return roles;
	}

	public void setRoles(Set<String> roles) {
		this.roles = roles;
	}

}