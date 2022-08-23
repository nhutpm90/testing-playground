package com.example.testing.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.testing.entity.User;
import com.example.testing.model.UserDto;
import com.example.testing.service.UserService;
import com.example.testing.service.UserServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(controllers = UserApi.class, 
excludeAutoConfiguration = {SecurityAutoConfiguration.class})
//@AutoConfigureMockMvc(addFilters = false)
//@MockBean({UserServiceImpl.class})
public class UserApiTests {
	
	@Autowired
	private MockMvc mockMvc;
	
	@MockBean
//	@Autowired
	UserService userService;

	@Test
	void testCreateUser_whenValidUser_returnCreatedUser() throws Exception {
		// arrange
		ObjectMapper om = new ObjectMapper();
		
		UserDto userRequest = new UserDto();
		userRequest.setFirstName("Nhut");
		userRequest.setLastName("Pham");
		userRequest.setEmail("nhut.pham@aa.aa");
		userRequest.setPassword("12345678");
		
		String userRequestJson = om.writeValueAsString(userRequest);
		
		UserDto responseFromUserService = new ModelMapper().map(userRequest, UserDto.class);
		responseFromUserService.setUserId(UUID.randomUUID().toString());
		when(userService.createUser(any(UserDto.class))).thenReturn(responseFromUserService);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(userRequestJson);
		
		// act
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
		String responseBodyAsString = mvcResult.getResponse().getContentAsString();
		
		UserDto userDtoResponse = om.readValue(responseBodyAsString, UserDto.class);
		
		// assert
		assertEquals(HttpStatus.OK.value(), mvcResult.getResponse().getStatus(), "incorrect http status code returned");
		assertEquals(userRequest.getFirstName(), userDtoResponse.getFirstName(), "invalid response first name");
		assertEquals(userRequest.getLastName(), userDtoResponse.getLastName(), "invalid response last name");
		assertEquals(userRequest.getEmail(), userDtoResponse.getEmail(), "invalid response email");
		assertNotNull(userDtoResponse.getUserId(), "invalid response user id");
		assertFalse(userDtoResponse.getUserId().isEmpty(), "empty response user id");
	}
	
	@Test
	void testCreateUser_whenInvalidUserData_returnHTTPErrorCode() throws Exception {
		// arrange
		ObjectMapper om = new ObjectMapper();
		
		UserDto userRequest = new UserDto();
		userRequest.setFirstName("a");
		userRequest.setLastName("Pham");
		userRequest.setEmail("nhut.pham@aa.aa");
		userRequest.setPassword("123456");
		
		String userRequestJson = om.writeValueAsString(userRequest);
		
		RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/users")
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)
			.content(userRequestJson);
		
		// act
		MvcResult mvcResult = mockMvc.perform(requestBuilder).andReturn();
		
		// assert
		assertEquals(HttpStatus.BAD_REQUEST.value(), 
				mvcResult.getResponse().getStatus(), "incorrect http status code returned");
	}

}