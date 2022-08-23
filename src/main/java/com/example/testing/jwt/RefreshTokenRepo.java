package com.example.testing.jwt;

import org.springframework.data.jpa.repository.JpaRepository;

public interface RefreshTokenRepo extends JpaRepository<RefreshToken, Long> {
	
	RefreshToken findByToken(String token);
}
