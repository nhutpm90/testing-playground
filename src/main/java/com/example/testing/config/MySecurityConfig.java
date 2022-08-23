package com.example.testing.config;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.example.testing.config.filter.AuthoritiesLoggingAfterFilter;
import com.example.testing.config.filter.JWTTokenValidatorFilter;


@Configuration
public class MySecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
			.and().cors().configurationSource(new CorsConfigurationSource() {
				@Override
				public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
					CorsConfiguration config = new CorsConfiguration();
	//				config.setAllowedOrigins(Collections.singletonList("http://172.16.2.29:3000"));
					config.addAllowedOriginPattern(CorsConfiguration.ALL);
					config.setAllowedMethods(Collections.singletonList("*"));
					config.setAllowCredentials(true);
					config.setAllowedHeaders(Collections.singletonList("*"));
					config.setMaxAge(3600L);
					return config;
				}
			})
			.and().csrf().disable()
			.addFilterBefore(new JWTTokenValidatorFilter(), BasicAuthenticationFilter.class)
			.addFilterAfter(new AuthoritiesLoggingAfterFilter(), BasicAuthenticationFilter.class)
			.authorizeRequests()
			.antMatchers("/test-db").permitAll()
			.antMatchers("/users").permitAll()
			.antMatchers("/public").permitAll()
			.antMatchers("/home").authenticated()
			.antMatchers("/operation").hasRole("OPERATION")
			.antMatchers("/manager").hasRole("MANAGER")
			.antMatchers("/admin").hasRole("ADMIN")
			.mvcMatchers("/about").permitAll()
			.and().formLogin()
			.and().httpBasic() // allow for authentication using http header
			;
	}
	
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
	@Bean
	PasswordEncoder passwordEncoder() {
//		return NoOpPasswordEncoder.getInstance();
		return new BCryptPasswordEncoder(10);
	}
	
	@Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/h2-console/**");
    }
}
