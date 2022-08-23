package com.example.testing.service;

import java.util.List;

import com.example.testing.model.UserDto;

public interface UserService {
	UserDto createUser(UserDto user);

	List<UserDto> getUsers(int page, int limit);

	UserDto getUser(String email);
}
