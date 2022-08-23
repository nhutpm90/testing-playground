package com.example.testing.api;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
public class OperationApi {
	
	@GetMapping("/operation")
	public String operation() {
		log.info("+ calling /operation");
		return "operation";
	}
	
	@GetMapping("/manager")
	public String manager() {
		log.info("+ calling /manager");
		return "manager";
	}
}