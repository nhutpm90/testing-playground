package com.example.testing.api;

import java.util.List;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.testing.model.UserDto;
import com.example.testing.model.UserRequestModel;
import com.example.testing.service.UserService;

@RestController
@RequestMapping("/users")
public class UserApi {

    private UserService userService;

	public UserApi(UserService userService) {
		this.userService = userService;
	}

    @GetMapping
    public List<UserDto> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
			@RequestParam(value = "limit", defaultValue = "5") int limit) {
		List<UserDto> users = this.userService.getUsers(page, limit);
		return users;
	}
    
    @PostMapping
    public UserDto addUser(@RequestBody @Valid UserRequestModel userRequest) throws Exception {
        UserDto userDto = new ModelMapper().map(userRequest, UserDto.class);
        UserDto ret = this.userService.createUser(userDto);
        return ret;
    }
}

