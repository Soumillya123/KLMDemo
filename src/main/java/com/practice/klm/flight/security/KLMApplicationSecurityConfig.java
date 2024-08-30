package com.practice.klm.flight.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
@EnableWebSecurity
@Configuration
public class KLMApplicationSecurityConfig {

	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http)throws Exception{
		/*
		 * http.csrf().disable() .authorizeHttpRequests()
		 * .requestMatchers(HttpMethod.GET, "/flights/**").hasAnyRole("USER","ADMIN")
		 * .requestMatchers(HttpMethod.POST, "/flights/**").hasRole("ADMIN")
		 * .requestMatchers(HttpMethod.PUT, "/flights/**").hasRole("ADMIN")
		 * .requestMatchers(HttpMethod.DELETE, "/flights/**").hasRole("ADMIN")
		 * anyRequest().authenticated() .and() .httpBasic(Customizer.withDefaults()) ;
		 */
		http.authorizeHttpRequests(auth -> auth
	            .requestMatchers(HttpMethod.GET, "/flights/**").hasAnyRole("USER", "ADMIN")
	            .requestMatchers(HttpMethod.POST, "/flights/**").hasRole("ADMIN")
	            .requestMatchers(HttpMethod.PUT, "/flights/**").hasRole("ADMIN")
	            .requestMatchers(HttpMethod.DELETE, "/flights/**").hasRole("ADMIN")
	            .anyRequest().authenticated()
	        )
		.httpBasic(Customizer.withDefaults())
		.csrf().disable();
		return http.build();
	}
	
	@Bean
    public UserDetailsService userDetailsService() {
		InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();
        manager.createUser(User.withDefaultPasswordEncoder()
            .username("user")
            .password("password")
            .roles("USER")
            .build());
        manager.createUser(User.withDefaultPasswordEncoder()
            .username("admin")
            .password("password")
            .roles("ADMIN")
            .build());
        
        return manager;
		
    }
	
}
