package com.example.securityexample.global.handler;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    response.getWriter().write(SUCCESS_MESSAGE + authentication.getName());
  }
}
