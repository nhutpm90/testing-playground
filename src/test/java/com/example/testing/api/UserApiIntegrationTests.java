package com.example.testing.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.UUID;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.testing.model.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONObject;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT //DEFINED_PORT
//,properties = {"server.port=8888", "hostname=localhost"}
)
@TestPropertySource(locations = "/application-test.properties"
//	,properties = {"server.port=7777"}
)
public class UserApiIntegrationTests {

	@Value("${server.port}")
	private int serverPort;
	
	@LocalServerPort
	private int localServerPort;
	
    @Autowired
    private TestRestTemplate testRestTemplate;
    
	@Test
	void contextLoads() {
		System.out.println("server.port:: " + serverPort);
		System.out.println("localServerPort:: " + localServerPort);
	}
	
	@DisplayName("user can be created")
	@Test
	void testCreateUser_whenValidUser_returnCreatedUser() throws Exception {
		// arrange
		JSONObject userRequest = new JSONObject();
		userRequest.put("firstName", "Nhut");
		userRequest.put("lastName", "Pham");
		userRequest.put("email", "nhut.pham@aa.aa");
		userRequest.put("password", "12345678");
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		HttpEntity<String> request = new HttpEntity<>(userRequest.toString(), headers);
		
		ResponseEntity<UserDto> responseEntity = testRestTemplate.postForEntity("/users",
                request, UserDto.class);
		UserDto userDtoResponse = responseEntity.getBody();
		
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
        assertEquals(userRequest.getAsString("firstName"),
        		userDtoResponse.getFirstName(),"Returned user's first name seems to be incorrect");
        assertEquals(userRequest.getAsString("lastName"),
        		userDtoResponse.getLastName(), "Returned user's last name seems to be incorrect");
        assertEquals(userRequest.getAsString("email"),
        		userDtoResponse.getEmail(), "Returned user's email seems to be incorrect");
        assertFalse(userDtoResponse.getUserId().trim().isEmpty(),
                "User id should not be empty");
	}

}