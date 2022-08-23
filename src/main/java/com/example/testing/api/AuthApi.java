package com.example.testing.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.testing.jwt.AuthService;
import com.example.testing.jwt.JwtWrapper;
import com.example.testing.jwt.LoginRequest;
import com.example.testing.jwt.TokenException;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/auth")
public class AuthApi {

	@Autowired
	private AuthService authService;

	@PostMapping("/login")
	public JwtWrapper login(@RequestBody LoginRequest loginRequest) throws JsonProcessingException {
		return this.authService.login(loginRequest.getUsername(), loginRequest.getPassword());
	}
	
	@PostMapping("/refresh-token")
	public JwtWrapper refreshtoken(@RequestBody JwtWrapper request) throws TokenException {
		return this.authService.refreshToken(request.getRefreshToken());
	}
}