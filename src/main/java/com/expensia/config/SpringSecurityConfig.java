package com.expensia.config;

import com.expensia.security.JwtAuthenticationEntryPoint;
import com.expensia.security.JwtAuthenticationFilter;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@AllArgsConstructor
@EnableMethodSecurity
public class SpringSecurityConfig {
    @Autowired
    private UserDetailsService userDetailsService;

    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        httpSecurity.csrf((csrf) -> csrf.disable())
                .authorizeHttpRequests((authrize) -> {
                    authrize.requestMatchers("/expensia/auth/**", "/expensia/password-reset/**").permitAll();
                    authrize.requestMatchers(HttpMethod.OPTIONS, "/**").permitAll();
                    authrize.anyRequest().authenticated();
                })
                .httpBasic(Customizer.withDefaults())
                .exceptionHandling(exception ->
                        exception.authenticationEntryPoint(jwtAuthenticationEntryPoint))
                .addFilterBefore(
                        jwtAuthenticationFilter,
                        UsernamePasswordAuthenticationFilter.class);
        return httpSecurity.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }
}

//This class, SpringSecurityConfig, is a configuration class for Spring Security in a Spring Boot application.
// It defines how requests to your application should be secured and authenticated.

//SecurityFilterChain Bean:
//
//Configures the security settings for the application using SecurityFilterChain.
//Disables CSRF protection.
//Configures authorization rules using authorizeRequests(). For example:
//Requests to "/expensia/auth/**" are permitted without authentication.
//OPTIONS requests are permitted for any URL.
//All other requests require authentication.
//Configures HTTP Basic authentication and provides a custom entry point for unauthorized access.
//Adds a custom JWT authentication filter before the UsernamePasswordAuthenticationFilter.

//This configuration is suitable for stateless JWT-based authentication.
//It assumes that the JwtAuthenticationFilter is correctly implemented to handle JWT processing

