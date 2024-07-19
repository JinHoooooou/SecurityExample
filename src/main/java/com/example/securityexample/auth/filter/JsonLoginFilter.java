package com.example.securityexample.auth.filter;

import com.example.securityexample.auth.dto.LoginRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

public class JsonLoginFilter extends AbstractAuthenticationProcessingFilter {

  private static final String LOGIN_REQUEST_URL = "/api/v1/login";
  private static final String LOGIN_REQUEST_HTTP_METHOD = "POST";
  private static final String LOGIN_REQUEST_CONTENT_TYPE = "application/json";
  private static final AntPathRequestMatcher DEFAULT_LOGIN_PATH_REQUEST_MATCHER =
      new AntPathRequestMatcher(LOGIN_REQUEST_URL, LOGIN_REQUEST_HTTP_METHOD);

  private final Validator validator;

  public JsonLoginFilter(Validator validator) {
    super(DEFAULT_LOGIN_PATH_REQUEST_MATCHER);
    this.validator = validator;
  }

  @Override
  public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
      throws AuthenticationException, IOException {
    if (!isApplicationJson(request.getContentType())) {
      throw new AuthenticationServiceException("Not Supported Content-Type: " + request.getContentType());
    }

    LoginRequestDto loginRequestDto = parseDto(request);
    return getAuthentication(loginRequestDto);
  }

  private LoginRequestDto parseDto(HttpServletRequest request) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    LoginRequestDto loginRequestDto = objectMapper.readValue(request.getInputStream(), LoginRequestDto.class);
    Set<ConstraintViolation<LoginRequestDto>> violations = validator.validate(loginRequestDto);
    if (!violations.isEmpty()) {
      Map<String, String> errorMap = violations
          .stream()
          .collect(Collectors.toMap(k -> k.getPropertyPath().toString(), ConstraintViolation::getMessage));
      throw new AuthenticationServiceException(objectMapper.writeValueAsString(errorMap));
    }
    return loginRequestDto;
  }

  private Authentication getAuthentication(LoginRequestDto loginRequestDto) {
    UsernamePasswordAuthenticationToken authentication =
        UsernamePasswordAuthenticationToken.unauthenticated(loginRequestDto.getEmail(), loginRequestDto.getPassword());
    return this.getAuthenticationManager().authenticate(authentication);
  }

  private boolean isApplicationJson(String contentType) {
    return contentType != null && contentType.equals(LOGIN_REQUEST_CONTENT_TYPE);
  }
}
