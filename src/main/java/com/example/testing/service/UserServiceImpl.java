package com.example.testing.service;

import java.util.UUID;

import com.example.testing.model.User;
import com.example.testing.repo.UserRepo;

public class UserServiceImpl implements UserService {
	
	private UserRepo userRepo;
	private EmailVerificationService emailVerificationService;
	
	public UserServiceImpl(UserRepo userRepo, EmailVerificationService emailVerificationService) {
		this.userRepo = userRepo;
		this.emailVerificationService = emailVerificationService;
	}
	
    @Override
    public User createUser(String firstName,
                           String lastName,
                           String email,
                           String password,
                           String repeatPassword) {
		if (firstName == null || firstName.trim().isEmpty()) {
			throw new IllegalArgumentException("User's first name is empty");
		}
		if (lastName == null || lastName.trim().isEmpty()) {
			throw new IllegalArgumentException("User's last name is empty");
		}

		String userId = UUID.randomUUID().toString();
		User user = new User(userId, firstName, lastName, email);

		boolean result = false;
		try {
			result = this.userRepo.save(user);
		} catch(RuntimeException ex) {
			throw new UserServiceException(ex.getMessage());
		}
		
		if (!result) {
			throw new UserServiceException("could not create user");
		}

		try {
			emailVerificationService.scheduleEmailConfirmation(user);
		} catch(RuntimeException ex) {
			throw new UserServiceException(ex.getMessage());
		}
		return user;
    }
}


//    @Override
//    public User createUser(String firstName,
//                           String lastName,
//                           String email,
//                           String password,
//                           String repeatPassword) {
//    	
//    	if(firstName == null || firstName.trim().isEmpty()) {
//    		throw new IllegalArgumentException("User's first name is empty");
//    	}
//    	String userId = UUID.randomUUID().toString();
//        return new User(userId, firstName, lastName, email);
//    }


