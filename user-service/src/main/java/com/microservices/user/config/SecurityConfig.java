package com.microservices.user.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

//@Configuration
//public class SecurityConfig {
//
//    @Bean
//    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http
//            .csrf(csrf -> csrf.disable())
//            .authorizeHttpRequests(auth -> auth
//                .requestMatchers("/api/users/me").authenticated()
//                .requestMatchers(HttpMethod.POST, "/api/users/**").authenticated()
//                .requestMatchers(HttpMethod.PUT, "/api/users/**").authenticated()
//                .requestMatchers(HttpMethod.DELETE, "/api/users/**").authenticated()
//                .requestMatchers(HttpMethod.GET, "/api/users/{id:\\d+}").permitAll()
//                .requestMatchers(HttpMethod.GET, "/api/users").permitAll()
//                .anyRequest().authenticated()
//            )
//            .oauth2ResourceServer(oauth2 -> oauth2.jwt());
//
//        return http.build();
//    }
//}


@Configuration
public class SecurityConfig {

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
            .csrf(csrf -> csrf.disable())
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/actuator/**").permitAll()
                .anyRequest().authenticated()
            )
      
            .oauth2ResourceServer(oauth2 -> oauth2.jwt())
            .build();
    }
	
//	@Bean
//	SecurityFilterChain security(HttpSecurity http) throws Exception {
//	    http
//	        .csrf(csrf -> csrf.disable())
//	        .authorizeHttpRequests(auth -> auth
//	            .requestMatchers("/actuator/**").permitAll()
//	            .anyRequest().authenticated()
//	        )
//	        .oauth2ResourceServer(oauth2 -> oauth2.jwt());
//
//	    return http.build();
//	}
}