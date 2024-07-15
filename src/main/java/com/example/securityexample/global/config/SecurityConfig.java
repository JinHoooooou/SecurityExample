package com.example.securityexample.global.config;

import com.example.securityexample.global.filter.JsonLoginFilter;
import com.example.securityexample.global.handler.JsonLoginFailureHandler;
import com.example.securityexample.global.handler.JsonLoginSuccessHandler;
import jakarta.validation.Validator;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer.FrameOptionsConfig;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final Validator validator;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .headers(frame -> frame.frameOptions(FrameOptionsConfig::sameOrigin));

    httpSecurity.authorizeHttpRequests(requests -> requests
        .requestMatchers(HttpMethod.POST, "/api/v1/login", "/api/v1/signup").permitAll()
        .requestMatchers("/h2-console/**").permitAll()
        .requestMatchers("/").permitAll()
    );

    httpSecurity.addFilterBefore(jsonLoginFilter(), UsernamePasswordAuthenticationFilter.class);

    return httpSecurity.build();
  }

  @Bean
  public PasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    User tempUser = new User(
        "jinho4744@naver.com",
        new BCryptPasswordEncoder().encode("12345678"),
        Collections.singleton(new SimpleGrantedAuthority("read"))
    );
    authenticationProvider.setUserDetailsService(new InMemoryUserDetailsManager(tempUser));
    authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

    return new ProviderManager(authenticationProvider);
  }

  @Bean
  public JsonLoginSuccessHandler jsonLoginSuccessHandler() {
    return new JsonLoginSuccessHandler();
  }

  @Bean
  public JsonLoginFailureHandler jsonLoginFailureHandler() {
    return new JsonLoginFailureHandler();
  }

  @Bean
  public AbstractAuthenticationProcessingFilter jsonLoginFilter() {
    AbstractAuthenticationProcessingFilter loginFilter = new JsonLoginFilter(validator);
    loginFilter.setAuthenticationManager(authenticationManager());
    loginFilter.setAuthenticationSuccessHandler(jsonLoginSuccessHandler());
    loginFilter.setAuthenticationFailureHandler(jsonLoginFailureHandler());
    return loginFilter;
  }
}
