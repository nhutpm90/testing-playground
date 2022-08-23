package com.example.testing.service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.testing.entity.User;
import com.example.testing.model.UserDto;
import com.example.testing.repo.UserRepo;

@Service
public class UserServiceImpl implements UserService {

	private UserRepo userRepo;
    private PasswordEncoder passwordEncoder;

	public UserServiceImpl(UserRepo userRepo, PasswordEncoder passwordEncoder) {
		this.userRepo = userRepo;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public UserDto createUser(UserDto userDto) {
		if (this.userRepo.findByEmail(userDto.getEmail()) != null) {
			throw new UserServiceException("user already exists");
		}

		ModelMapper modelMapper = new ModelMapper();
		
		User user = modelMapper.map(userDto, User.class);
		user.setUserId(UUID.randomUUID().toString());
		user.setEncryptedPassword(this.passwordEncoder.encode(userDto.getPassword()));
		User storedUser = this.userRepo.save(user);

		UserDto returnedUser = modelMapper.map(storedUser, UserDto.class);
		return returnedUser;
	}

	@Override
	public List<UserDto> getUsers(int page, int limit) {
		List<UserDto> ret = new ArrayList<>();

		Pageable pageableRequest = PageRequest.of(page, limit);

		Page<User> usersPage = userRepo.findAll(pageableRequest);
		List<User> users = usersPage.getContent();

		Type listType = new TypeToken<List<UserDto>>() {}.getType();
		ret = new ModelMapper().map(users, listType);

		return ret;
	}

	@Override
	public UserDto getUser(String email) {
		User user = this.userRepo.findByEmail(email);
		if (user == null) {
			throw new UserServiceException("not found user");
		}
		
		UserDto returnValue = new UserDto();
		BeanUtils.copyProperties(user, returnValue);

		return returnValue;
	}
}
