package com.example.testing.bootstrap;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.example.testing.entity.Role;
import com.example.testing.entity.User;
import com.example.testing.repo.UserRepo;

@Component
public class DataInitializer implements CommandLineRunner {

	@Autowired
	private UserRepo userRepo;
	
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	public DataInitializer() {
	}

	@Override
	public void run(String... args) throws Exception {
		this.initCustomUserDetailsService();
	}
	
	private void initCustomUserDetailsService() {
		String password = passwordEncoder.encode("12345678");
		User dev = new User("dev", password);
		dev.setUserId(UUID.randomUUID().toString());
		dev.setFirstName("dev");
		dev.setLastName("test");
		dev.setEmail("dev@company.com");
		dev.addRole(new Role("ROLE_USER"));
		this.userRepo.save(dev);

		User admin = new User("admin", password);
		admin.setUserId(UUID.randomUUID().toString());
		admin.setFirstName("admin");
		admin.setLastName("test");
		admin.setEmail("admin@company.com");
		admin.addRole(new Role("ROLE_ADMIN"));
		admin.addRole(new Role("ROLE_OPERATION"));
		this.userRepo.save(admin);
	}
	
}
