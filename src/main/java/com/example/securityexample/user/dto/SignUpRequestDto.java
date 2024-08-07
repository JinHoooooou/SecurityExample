package com.example.securityexample.user.dto;

import com.example.securityexample.global.constants.Message;
import com.example.securityexample.global.constants.Regexp;
import com.example.securityexample.user.entity.User;
import jakarta.validation.constraints.AssertTrue;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class SignUpRequestDto {

  @Pattern(regexp = Regexp.EMAIL, message = Message.INVALID_EMAIL)
  private String email;
  @Pattern(regexp = Regexp.PASSWORD, message = Message.INVALID_PASSWORD)
  private String password;
  private String passwordConfirm;
  @Pattern(regexp = Regexp.NICKNAME, message = Message.INVALID_NICKNAME)
  private String nickname;
  @Pattern(regexp = Regexp.ADDRESS, message = Message.INVALID_ADDRESS)
  private String address;
  @Pattern(regexp = Regexp.PHONE, message = Message.INVALID_PHONE)
  private String phone;

  @AssertTrue(message = Message.INVALID_PASSWORD_CONFIRM)
  public boolean isPasswordConfirm() {
    return this.password.equals(this.passwordConfirm);
  }

  public User toEntity(PasswordEncoder passwordEncoder) {
    return User.builder()
        .email(this.email)
        .nickname(this.nickname)
        .password(passwordEncoder.encode(this.password))
        .address(this.address)
        .phone(this.phone)
        .build();
  }
}
