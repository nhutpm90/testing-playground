package com.example.testing.model;

public class User {

	private String id;
	private String firstName;
	private String lastName;
	private String email;

	public User(String id, String firstName, String lastName, String email) {
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
	}

	public String getFirstName() {
		return firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public String getEmail() {
		return email;
	}

	public String getId() {
		return id;
	}
}
