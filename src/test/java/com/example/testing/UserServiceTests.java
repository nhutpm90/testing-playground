package com.example.testing;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.testing.model.User;
import com.example.testing.repo.UserRepo;
import com.example.testing.service.EmailNotificationServiceException;
import com.example.testing.service.EmailVerificationServiceImpl;
import com.example.testing.service.UserServiceException;
import com.example.testing.service.UserServiceImpl;

@ExtendWith(MockitoExtension.class)
class UserServiceTests {
	
	@InjectMocks
	UserServiceImpl userService;
	
	@Mock
	UserRepo userRepo;
	
	@Mock
	EmailVerificationServiceImpl emailVerificationService;
	
	String firstName;
	String lastName;
	String email;
	String password;
	String repeatPassword;
	
	@BeforeEach
	void init() {
		firstName = "Sergey";
		lastName = "Kargopolov";
		email = "test@test.com";
		password = "12345678";
		repeatPassword = "12345678";
	}
	
	@DisplayName("User object created")
	@Test
	void testCreateUser_whenUserDetailsProvided_returnsUserObject() {
		// Arrange
		when(userRepo.save(any(User.class))).thenReturn(true);
		
		// Act
		User user = userService.createUser(firstName, lastName, email, password, repeatPassword);

		// Assert
		assertNotNull(user, "The createUser() should not have returned null");
		assertEquals(firstName, user.getFirstName(), "User's first name is incorrect.");
		assertEquals(lastName, user.getLastName(), "User's last name is incorrect");
		assertEquals(email, user.getEmail(), "User's email is incorrect");
		assertNotNull(user.getId(), "UserId is missing");
		
//		verify(userRepo, times(1)).save(any(User.class));
		verify(userRepo).save(any(User.class));
	}
	
	@DisplayName("Empty first name causes correct exception")
	@Test
	void testCreateUser_whenFirstNameIsEmpty_throwsIllegalArgumentException() {
		
		// Arrange
		String firstName = "";
		String expectedExceptionInMessage = "User's first name is empty";
		
		// Act & Assert
		IllegalArgumentException thrown = Assertions.assertThrows(IllegalArgumentException.class, () -> {
			userService.createUser(firstName, lastName, email, password, repeatPassword);
		}, "Empty first name should have caused an Illegal Argument Exception");
		
		assertEquals(expectedExceptionInMessage, thrown.getMessage(), 
				"Exception error message is not correct");
	}
	
	@Test
	void testCreateUser_whenSaveMethodThrowsException_thenThrowsUserServiceException() {
		// Arrange
		when(userRepo.save(any(User.class))).thenThrow(RuntimeException.class);
		// Act & Assert
		assertThrows(UserServiceException.class, () -> {
			userService.createUser(firstName, lastName, email, password, repeatPassword);
		}, "Should have thrown UserServiceException instead");
	}
	
	@Test
	void testCreateUser_whenEmailNotificationThrowsException_thenThrowsUserServiceException() {
		// Arrange
		when(userRepo.save(any(User.class))).thenReturn(true);
		
		doThrow(EmailNotificationServiceException.class)
			.when(emailVerificationService)
			.scheduleEmailConfirmation(any(User.class));
		
//		doNothing().when(emailVerificationService).scheduleEmailConfirmation(any(User.class));
		
		// Act & Assert
		assertThrows(UserServiceException.class, () -> {
			userService.createUser(firstName, lastName, email, password, repeatPassword);
		}, "Should have thrown UserServiceException instead");
		
		verify(userRepo, times(1)).save(any(User.class));
		verify(emailVerificationService, times(1)).scheduleEmailConfirmation(any(User.class));
	}
	
	@DisplayName("Schedule email confirmation is executed")
	@Test
	void testCreateUser_whenUserCreated_thenScheduleEmailConfirmation() {
		// Arrange
		when(userRepo.save(any(User.class))).thenReturn(true);
		
		doCallRealMethod().when(emailVerificationService).scheduleEmailConfirmation(any(User.class));
		
		// Act
		userService.createUser(firstName, lastName, email, password, repeatPassword);
		
		// Assert
		verify(emailVerificationService, times(1)).scheduleEmailConfirmation(any(User.class));
	}
}
