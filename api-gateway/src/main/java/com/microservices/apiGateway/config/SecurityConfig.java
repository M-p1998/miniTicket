package com.microservices.apiGateway.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;

import org.springframework.http.HttpMethod;

@Configuration
public class SecurityConfig {
	@Bean
	  SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
	    return http
	      .csrf(ServerHttpSecurity.CsrfSpec::disable)
	      .authorizeExchange(ex -> ex
	        // allow health checks
	        .pathMatchers("/actuator/**").permitAll()

	        // allow GET (optional) - if you want browsing tickets without login, keep these
	        // .pathMatchers(HttpMethod.GET, "/api/tickets/**", "/api/comments/**").permitAll()

	        // REQUIRE LOGIN for write operations
	        .pathMatchers(HttpMethod.POST, "/api/tickets/**", "/api/comments/**").authenticated()
	        .pathMatchers(HttpMethod.PUT, "/api/tickets/**", "/api/comments/**").authenticated()
	        .pathMatchers(HttpMethod.PATCH, "/api/tickets/**", "/api/comments/**").authenticated()
	        .pathMatchers(HttpMethod.DELETE, "/api/tickets/**", "/api/comments/**").authenticated()

	        // everything else requires login (recommended)
	        .anyExchange().authenticated()
	      )
	      .oauth2ResourceServer(oauth2 -> oauth2.jwt(jwt -> {}))
	      .build();
	  }

}
