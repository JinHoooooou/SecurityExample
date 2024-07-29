package com.example.securityexample.global.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private static final String SIGNUP_URLS = "/api/v1/signup";

  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {
    response.setContentType("application/json;charset=UTF-8");
    response.setStatus(HttpServletResponse.SC_FORBIDDEN);

    String requestUri = request.getRequestURI();
    String message = accessDeniedException.getLocalizedMessage();

    if(requestUri.startsWith(SIGNUP_URLS)) {
      message = "Header에 Authorization이 포함되어 회원가입 API 요청이 불가능합니다.";
    }

    new ObjectMapper().writeValue(response.getWriter(), message);
  }
}
