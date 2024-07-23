package com.example.securityexample.auth.handler;

import com.example.securityexample.auth.dto.AuthenticatedUser;
import com.example.securityexample.auth.dto.LoginResponseDto;
import com.example.securityexample.global.constants.Message;
import com.example.securityexample.user.entity.User;
import com.example.securityexample.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;

@RequiredArgsConstructor
public class JsonLoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

  private static final String RESPONSE_CONTENT_TYPE = "application/json;charset=utf-8";
  private static final String SUCCESS_MESSAGE = "Login Success: ";
  private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
  private static final String CLAIM_EMAIL = "email";
  private static final String CLAIM_USER_ID = "id";
  private static final String CLAIM_BOROUGH_ID = "boroughId";
  private static final String CLAIM_BOROUGH_NAME = "boroughName";
  private static final String CLAIM_AUTHORITIES = "auth";
  private static final String BEARER = "Bearer ";

  @Value("${jwt.secretKey}")
  private String secretKey;
  @Value("${jwt.access.expiration}")
  private Long accessTokenExpiration;
  @Value("${jwt.access.header}")
  private String accessHeader;
  private final UserRepository userRepository;

  @Override
  public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
      Authentication authentication) throws IOException {
    response.setStatus(HttpServletResponse.SC_OK);
    response.setContentType(RESPONSE_CONTENT_TYPE);
    AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
    String email = authenticatedUser.getUsername();
    String authorities = authenticatedUser.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    User user = userRepository.findByEmail(email).orElseThrow(
        () -> new BadCredentialsException(Message.NOT_MATCH_LOGIN_DTO)
    );
    String accessToken = Jwts.builder()
        .subject(ACCESS_TOKEN_SUBJECT)
        .claim(CLAIM_USER_ID, user.getId())
        .claim(CLAIM_EMAIL, user.getEmail())
        .claim(CLAIM_BOROUGH_ID, user.getBorough().getId())
        .claim(CLAIM_AUTHORITIES, authorities)
        .expiration(new Date(new Date().getTime() + accessTokenExpiration))
        .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
        .compact();

    new ObjectMapper().writeValue(response.getWriter(),
        LoginResponseDto.builder()
            .accessToken(accessToken)
            .message(SUCCESS_MESSAGE + authentication.getName())
            .build()
    );
  }
}
