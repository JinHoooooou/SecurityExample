package com.example.securityexample.auth.service;

import com.example.securityexample.auth.dto.AuthenticatedUser;
import com.example.securityexample.global.constants.Message;
import com.example.securityexample.user.entity.User;
import com.example.securityexample.user.repository.UserRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtService {

  private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
  private static final String CLAIM_EMAIL = "email";
  private static final String CLAIM_USER_ID = "id";
  private static final String CLAIM_BOROUGH_ID = "boroughId";
  private static final String CLAIM_AUTHORITIES = "auth";

  @Value("${jwt.secretKey}")
  private String secretKey;
  @Value("${jwt.access.expiration}")
  private Long accessTokenExpiration;

  private final UserRepository userRepository;

  public String createAccessToken(String email, String authorities) {
    User user = userRepository.findByEmail(email).orElseThrow(
        () -> new BadCredentialsException(Message.NOT_MATCH_LOGIN_DTO)
    );

    return Jwts.builder()
        .subject(ACCESS_TOKEN_SUBJECT)
        .claim(CLAIM_USER_ID, user.getId())
        .claim(CLAIM_EMAIL, user.getEmail())
        .claim(CLAIM_BOROUGH_ID, user.getBorough().getId())
        .claim(CLAIM_AUTHORITIES, authorities)
        .expiration(new Date(new Date().getTime() + accessTokenExpiration))
        .signWith(Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey)))
        .compact();
  }

  public String createAccessToken(Authentication authentication) {
    AuthenticatedUser authenticatedUser = (AuthenticatedUser) authentication.getPrincipal();
    String email = authenticatedUser.getUsername();
    String authorities = authenticatedUser.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.joining(","));

    return this.createAccessToken(email, authorities);
  }
}
