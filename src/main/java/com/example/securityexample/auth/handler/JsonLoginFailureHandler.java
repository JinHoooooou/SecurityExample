package com.example.securityexample.auth.handler;

import com.example.securityexample.auth.dto.LoginResponseDto;
import com.example.securityexample.global.constants.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

public class JsonLoginFailureHandler extends SimpleUrlAuthenticationFailureHandler {

  private static final String RESPONSE_CONTENT_TYPE = "application/json;charset=utf-8";

  @Override
  public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException exception) throws IOException {
    response.setContentType(RESPONSE_CONTENT_TYPE);
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

    new ObjectMapper().writeValue(response.getWriter(),
        LoginResponseDto.builder().message(Message.NOT_MATCH_LOGIN_DTO).build());
  }
}
