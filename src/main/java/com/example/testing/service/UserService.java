package com.example.testing.service;

import com.example.testing.model.User;

public interface UserService {
	
    User createUser(String firstName,
                    String lastName,
                    String email,
                    String password,
                    String repeatPassword);
}
