package com.example.testing.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class AdminApi {
	
	@GetMapping("/admin")
	public String admin() {
		log.info("+ calling /admin");
		return "admin";
	}
}