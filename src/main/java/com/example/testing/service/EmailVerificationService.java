package com.example.testing.service;

import com.example.testing.model.User;

public interface EmailVerificationService {
	
	void scheduleEmailConfirmation(User user);
	
}
