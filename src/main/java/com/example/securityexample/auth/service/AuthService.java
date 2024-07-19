package com.example.securityexample.auth.service;

import com.example.securityexample.global.constants.Message;
import com.example.securityexample.auth.dto.AuthenticatedUser;
import com.example.securityexample.user.entity.User;
import com.example.securityexample.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByEmail(username).orElseThrow(
        () -> new BadCredentialsException(Message.NOT_MATCH_LOGIN_DTO)
    );
    return new AuthenticatedUser(user);
  }
}
