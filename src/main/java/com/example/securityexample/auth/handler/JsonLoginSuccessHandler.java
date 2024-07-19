package com.example.securityexample.auth.handler;

import com.example.securityexample.auth.dto.LoginResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

public class JsonLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private static final String RESPONSE_CONTENT_TYPE = "application/json;charset=utf-8";
  private static final String SUCCESS_MESSAGE = "Login Success: ";

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(RESPONSE_CONTENT_TYPE);

    HttpSession session = request.getSession();
    session.setAttribute("authenticatedUser", authentication.getPrincipal());

    new ObjectMapper().writeValue(response.getWriter(),
        LoginResponseDto.builder().message(SUCCESS_MESSAGE + authentication.getName()).build());
  }
}
