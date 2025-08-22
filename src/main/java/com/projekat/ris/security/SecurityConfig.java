package com.projekat.ris.security;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity // @PreAuthorize na metodama servisa i kontrolera
public class SecurityConfig {
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .formLogin(Customizer.withDefaults())
                .logout(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                                "/swagger-resources/**", "/webjars/**", "/css/**", "/js/**", "/images/**", "/ws/**"
                        ).permitAll()

                        .requestMatchers("/", "/products/**").permitAll()
                        .requestMatchers("/users/register").permitAll()

                        .requestMatchers("/carts/**", "/orders/**").hasRole("USER")
                        .requestMatchers( "/chat/**").hasAnyRole("USER", "ADMIN")
                        .requestMatchers("/users/**", "/wishlists/**").authenticated()

                         .requestMatchers(HttpMethod.POST,   "/products/**").hasRole("ADMIN")
                         .requestMatchers(HttpMethod.PUT,    "/products/**").hasRole("ADMIN")
                         .requestMatchers(HttpMethod.DELETE, "/products/**").hasRole("ADMIN")

                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration cfg) throws Exception {
        return cfg.getAuthenticationManager();
    }
}
