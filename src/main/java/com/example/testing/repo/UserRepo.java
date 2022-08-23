package com.example.testing.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.testing.entity.User;

@Repository
public interface UserRepo extends JpaRepository<User, Long> {

	User findByEmail(String email);
	
	@Query("SELECT u FROM User u JOIN FETCH u.roles WHERE u.username=:username")
	User findUserWithRoles(String username);
}
