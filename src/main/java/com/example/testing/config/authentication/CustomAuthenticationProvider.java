package com.example.testing.config.authentication;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.testing.entity.CustomUserDetails;
import com.example.testing.entity.User;
import com.example.testing.repo.UserRepo;

@Service
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserRepo userRepo;

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) {
		String username = authentication.getName();
		String pwd = authentication.getCredentials().toString();
		User user = userRepo.findUserWithRoles(username);
		if (user != null) {
			String password = user.getPassword();
			if (passwordEncoder.matches(pwd, password)) {
				CustomUserDetails principal = new CustomUserDetails(user);
				Collection<? extends GrantedAuthority> authorities = principal.getAuthorities();
				
				UsernamePasswordAuthenticationToken authenticationToReturn = 
						new UsernamePasswordAuthenticationToken(principal, null, authorities);
				return authenticationToReturn;
			} else {
				throw new BadCredentialsException("Invalid password!");
			}
		} else {
			throw new BadCredentialsException("No user registered with this details!");
		}
	}

	@Override
	public boolean supports(Class<?> authenticationType) {
		return authenticationType.equals(UsernamePasswordAuthenticationToken.class);
	}
}