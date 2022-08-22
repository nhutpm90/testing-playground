package com.example.testing.service;

import com.example.testing.model.User;

public class EmailVerificationServiceImpl implements EmailVerificationService {

	@Override
	public void scheduleEmailConfirmation(User user) {
		System.out.println("invoke scheduleEmailConfirmation");
	}

}
