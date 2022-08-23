package com.example.testing.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.Size;

public class UserRequestModel {

	@Size(min = 2, message = "User name must not be less than 2 characters")
	private String username;

	@Size(min = 8, max = 16, message = "Password must be equal to or greater than 8 characters and less than 16 characters")
	private String password;
	
	@Size(min = 2, message = "First name must not be less than 2 characters")
	private String firstName;

	@Size(min = 2, message = "Last name must not be less than 2 characters")
	private String lastName;

	@Email
	private String email;

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

}
