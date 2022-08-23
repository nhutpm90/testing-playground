package com.example.testing.api;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.json.JSONException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestMethodOrder;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.example.testing.jwt.JwtWrapper;
import com.example.testing.model.UserDto;
import com.fasterxml.jackson.databind.ObjectMapper;

import net.minidev.json.JSONObject;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT //DEFINED_PORT
//,properties = {"server.port=8888", "hostname=localhost"}
)
@TestPropertySource(locations = "/application-test.properties"
//	,properties = {"server.port=7777"}
)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class UserApiIntegrationTests {

	@Value("${server.port}")
	private int serverPort;
	
	@LocalServerPort
	private int localServerPort;
	
    @Autowired
    private TestRestTemplate testRestTemplate;
    
    private String authorizationToken;
    
    
	@Test
	void contextLoads() {
		System.out.println("server.port:: " + serverPort);
		System.out.println("localServerPort:: " + localServerPort);
	}
	
	@DisplayName("user can be created")
	@Order(1)
	@Test
	void testCreateUser_whenValidUser_returnsCreatedUser() throws Exception {
		// arrange
		JSONObject userRequest = new JSONObject();
		userRequest.put("username", "nhutpham");
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
		assertEquals(userRequest.getAsString("username"),
        		userDtoResponse.getUsername(),"Returned user's username seems to be incorrect");
        assertEquals(userRequest.getAsString("firstName"),
        		userDtoResponse.getFirstName(),"Returned user's first name seems to be incorrect");
        assertEquals(userRequest.getAsString("lastName"),
        		userDtoResponse.getLastName(), "Returned user's last name seems to be incorrect");
        assertEquals(userRequest.getAsString("email"),
        		userDtoResponse.getEmail(), "Returned user's email seems to be incorrect");
        assertFalse(userDtoResponse.getUserId().trim().isEmpty(),
                "User id should not be empty");
	}

    @DisplayName("GET /home requires JWT")
    @Order(2)
    @Test
    void testGetHome_whenMissingJWT_returns401() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        // Act
		ResponseEntity<String> response = testRestTemplate.exchange("/home", HttpMethod.GET, 
				requestEntity, String.class);

        // Assert
        Assertions.assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode(),
                "HTTP Status code 401 should have been returned");
    }
    
    @DisplayName("/login works")
    @Order(3)
    @Test
    void testUserLogin_whenValidCredentialsProvided_returnsJWT() throws JSONException {
        // Arrange
        JSONObject loginCredentials = new JSONObject();
        loginCredentials.put("username","dev");
        loginCredentials.put("password","12345678");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<String> request = new HttpEntity<>(loginCredentials.toString(), headers);

        // Act
        ResponseEntity<JwtWrapper> responseEntity = testRestTemplate.postForEntity("/auth/login",
                request, JwtWrapper.class);
        JwtWrapper response = responseEntity.getBody();
        String token = response.getToken();
        
        authorizationToken = token;
        
        // Assert
        Assertions.assertEquals(HttpStatus.OK, responseEntity.getStatusCode(),
                "HTTP Status code should be 200");
        Assertions.assertNotNull(token,
                "Response should contain JWT token");
    }
    
    @DisplayName("GET /home works")
    @Order(4)
    @Test
    void testGetHome_whenProvidedJWTToken_returnsData() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        headers.setBearerAuth(authorizationToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        // Act
		ResponseEntity<String> response = testRestTemplate.exchange("/home", HttpMethod.GET, 
				requestEntity, String.class);

        // Assert
		Assertions.assertEquals(HttpStatus.OK, response.getStatusCode(),
                "HTTP Status code should be 200");
    }
    
    @DisplayName("GET /admin requires ADMIN role")
    @Order(5)
    @Test
    void testGetAdmin_whenMissingAdminRole_returns403() {
        // Arrange
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		headers.setBearerAuth(authorizationToken);
        HttpEntity<String> requestEntity = new HttpEntity<>(null, headers);

        // Act
		ResponseEntity<String> response = testRestTemplate.exchange("/admin", HttpMethod.GET, 
				requestEntity, String.class);

        // Assert
        Assertions.assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode(),
                "HTTP Status code 403 should have been returned");
    }
}