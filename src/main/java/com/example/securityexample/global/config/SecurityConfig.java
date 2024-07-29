package com.example.securityexample.global.config;

import com.example.securityexample.auth.filter.JsonLoginFilter;
import com.example.securityexample.auth.filter.JwtAuthorizationFilter;
import com.example.securityexample.auth.handler.JsonLoginFailureHandler;
import com.example.securityexample.auth.handler.JsonLoginSuccessHandler;
import com.example.securityexample.auth.service.AuthService;
import com.example.securityexample.auth.service.JwtService;
import com.example.securityexample.global.handler.CustomAccessDeniedHandler;
import com.example.securityexample.global.handler.CustomAuthenticationEntryPoint;
import jakarta.validation.Validator;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

  private final Validator validator;
  private final AuthService authService;
  private final JwtService jwtService;
  private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
  private final CustomAccessDeniedHandler customAccessDeniedHandler;

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
    httpSecurity
        .formLogin(AbstractHttpConfigurer::disable)
        .httpBasic(AbstractHttpConfigurer::disable)
        .csrf(AbstractHttpConfigurer::disable)
        .headers(frame -> frame.frameOptions(FrameOptionsConfig::sameOrigin));

    httpSecurity.authorizeHttpRequests(requests -> requests
        .requestMatchers(HttpMethod.GET, "/h2-console/**", "/", "/api/v1/tests/books/list").permitAll()
        .requestMatchers(HttpMethod.POST, "/api/v1/signup").anonymous()
        .requestMatchers(HttpMethod.POST, "/api/v1/tests/books").authenticated()
        .requestMatchers(HttpMethod.GET, "/api/v1/tests/users/my-info").authenticated()
        .requestMatchers(HttpMethod.PATCH, "/api/v1/tests/books/{isbn}").hasAuthority("ADMIN")
    );

    httpSecurity.exceptionHandling(handler -> handler
        .authenticationEntryPoint(customAuthenticationEntryPoint)
        .accessDeniedHandler(customAccessDeniedHandler));

    httpSecurity.addFilterAt(jsonLoginFilter(), UsernamePasswordAuthenticationFilter.class);
    httpSecurity.addFilterBefore(jwtAuthorizationFilter(), JsonLoginFilter.class);

    return httpSecurity.build();
  }

  @Bean
  public PasswordEncoder bCryptPasswordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationManager() {
    DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
    authenticationProvider.setUserDetailsService(authService);
    authenticationProvider.setPasswordEncoder(bCryptPasswordEncoder());

    return new ProviderManager(authenticationProvider);
  }

  @Bean
  public JsonLoginSuccessHandler jsonLoginSuccessHandler() {
    return new JsonLoginSuccessHandler(jwtService);
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

  @Bean
  public JwtAuthorizationFilter jwtAuthorizationFilter() {
    return new JwtAuthorizationFilter(jwtService);
  }
}
