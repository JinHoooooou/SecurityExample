package com.example.securityexample.auth.handler;

import com.example.securityexample.auth.dto.LoginResponseDto;
import com.example.securityexample.auth.service.JwtService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@RequiredArgsConstructor
public class JsonLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private static final String RESPONSE_CONTENT_TYPE = "application/json;charset=utf-8";
  private static final String SUCCESS_MESSAGE = "Login Success: ";
  private final JwtService jwtService;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(RESPONSE_CONTENT_TYPE);

    String accessToken = jwtService.createAccessToken(authentication);

    new ObjectMapper().writeValue(response.getWriter(),
        LoginResponseDto.builder()
            .accessToken(accessToken)
            .message(SUCCESS_MESSAGE + authentication.getName())
            .build()
    );
  }
}
