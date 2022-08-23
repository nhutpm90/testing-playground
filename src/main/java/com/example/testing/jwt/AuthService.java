package com.example.testing.jwt;

import java.time.Instant;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import com.example.testing.entity.CustomUserDetails;
import com.example.testing.entity.User;
import com.example.testing.repo.UserRepo;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public class AuthService {

    private final Logger logger = LoggerFactory.getLogger(AuthService.class);
    
    @Autowired
    private AuthenticationManager authenticationManager;
    
    @Autowired
    private UserRepo userRepo;
    
    @Autowired
    private RefreshTokenRepo refreshTokenRepo;
    
    private String issuer = "testing-playground-app";
    private String subject = "JWT Token Test";
    
    public String JWT_KEY = SecurityConstants.JWT_KEY;
    public Integer JWT_EXPIRATION_IN_MS = SecurityConstants.JWT_EXPIRATION_IN_MS;
    public Integer REFRESH_TOKEN_EXPIRATION_IN_MS = SecurityConstants.REFRESH_TOKEN_EXPIRATION_IN_MS;
    
    public JwtWrapper login(String username, String password) throws JsonProcessingException {
        Authentication authentication = this.authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));
        
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        User user = userDetails.getUser();
        
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Set<String> roles = authorities.stream()
        		.map(authority -> authority.getAuthority())
        		.collect(Collectors.toSet());
        
        String strAuthorities = String.join(",", roles);
        String jwtToken = JwtUtils.generateTokenFromUsername(issuer, subject, 
        		username, strAuthorities, JWT_KEY, JWT_EXPIRATION_IN_MS);
        
        RefreshToken rt = this.createRefreshToken(user.getId());
        String refreshToken = rt.getToken();
        JwtWrapper response = new JwtWrapper(username, jwtToken, refreshToken, roles);
        
        logger.info(String.format("AuthService:: login:: username:: %s - password:: %s - token:: %s",  
        		username, password, jwtToken));
        return response;
    }
    
	public RefreshToken createRefreshToken(Long userId) {
		User user = this.userRepo.findById(userId).get();
		RefreshToken refreshToken = new RefreshToken();
		refreshToken.setUser(user);
		refreshToken.setExpiryDate(Instant.now().plusMillis(REFRESH_TOKEN_EXPIRATION_IN_MS));
		refreshToken.setToken(UUID.randomUUID().toString());
		refreshToken = refreshTokenRepo.save(refreshToken);
		return refreshToken;
	}
	
    public JwtWrapper refreshToken(String token) throws TokenException {
    	JwtWrapper ret = null;
		RefreshToken refreshToken = this.refreshTokenRepo.findByToken(token);
		if (refreshToken.getExpiryDate().compareTo(Instant.now()) < 0) {
			refreshTokenRepo.delete(refreshToken);
			throw new TokenException("invalid token");
		}
		User user = refreshToken.getUser();
		Set<String> roles = user.getRoles().stream()
        		.map(role -> role.getName())
        		.collect(Collectors.toSet());
		
		String strAuthorities = String.join(",", roles);
        String jwtToken = JwtUtils.generateTokenFromUsername(issuer, subject, 
        		user.getUsername(), strAuthorities, JWT_KEY, JWT_EXPIRATION_IN_MS);
		
        ret = new JwtWrapper(token, jwtToken);
		return ret;
    }
}