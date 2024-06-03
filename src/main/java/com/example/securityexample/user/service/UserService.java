package com.example.securityexample.user.service;

import com.example.securityexample.borough.entity.Borough;
import com.example.securityexample.borough.repository.BoroughRepository;
import com.example.securityexample.global.constants.Message;
import com.example.securityexample.global.exception.DuplicateResourceException;
import com.example.securityexample.global.exception.InvalidFieldException;
import com.example.securityexample.user.dto.SignUpDto;
import com.example.securityexample.user.entity.User;
import com.example.securityexample.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final BoroughRepository boroughRepository;
  private final PasswordEncoder passwordEncoder;

  public void createNewUser(SignUpDto signUpDto) {
    if (alreadyExistEmail(signUpDto.getEmail())) {
      throw new DuplicateResourceException(Message.DUPLICATE_EMAIL);
    }
    if (alreadyExistNickname(signUpDto.getNickname())) {
      throw new DuplicateResourceException(Message.DUPLICATE_NICKNAME);
    }

    User user = signUpDto.toEntity(passwordEncoder);
    Borough borough = this.boroughRepository
        .findByName(user.extractBoroughName())
        .orElseThrow(() -> new InvalidFieldException("address", Message.INVALID_ADDRESS));
    user.setBorough(borough);
    this.userRepository.save(user);
  }

  private boolean alreadyExistNickname(String nickname) {
    return userRepository.findByNickname(nickname).isPresent();
  }

  private boolean alreadyExistEmail(String email) {
    return userRepository.findByEmail(email).isPresent();
  }
}
