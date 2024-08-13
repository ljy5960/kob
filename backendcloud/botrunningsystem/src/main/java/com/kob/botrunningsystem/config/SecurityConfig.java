package com.kob.botrunningsystem.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.IpAddressMatcher;

;


@Configuration
@EnableWebSecurity
public class SecurityConfig {

//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
//                        .requestMatchers("/player/add/").permitAll()
//                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
//                        .anyRequest().authenticated());
//
//
//        return http.build();
//   }
//    IpAddressMatcher hasIpAddress = new IpAddressMatcher("127.0.0.1");
//    @Bean
//    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
//        http.csrf(AbstractHttpConfigurer::disable)
//                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .authorizeHttpRequests(authorizeHttpRequests -> authorizeHttpRequests
//                        .requestMatchers("/player/add/").access((authentication, context) ->
//                                new AuthorizationDecision(hasIpAddress.matches(context.getRequest())))
//                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
//                        .anyRequest().authenticated());
//
//        return http.build();
//    }
//    }
IpAddressMatcher hasIpAddress = new IpAddressMatcher("127.0.0.1");

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(httpSecuritySessionManagementConfigurer -> httpSecuritySessionManagementConfigurer
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/bot/add/").access((authentication, context) ->
                                new AuthorizationDecision(hasIpAddress.matches(context.getRequest())))
                        .requestMatchers(HttpMethod.OPTIONS).permitAll()
                        .anyRequest().authenticated());


        return http.build();
    }
    }
