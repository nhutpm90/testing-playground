package com.example.testing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.testing.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {
	
	User findByEmail(String email);
	
}
