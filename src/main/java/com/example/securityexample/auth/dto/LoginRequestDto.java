package com.example.securityexample.auth.dto;

import com.example.securityexample.global.constants.Message;
import com.example.securityexample.global.constants.Regexp;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequestDto {

  @NotNull(message = Message.INVALID_EMAIL)
  @Pattern(regexp = Regexp.EMAIL, message = Message.INVALID_EMAIL)
  private String email;

  @NotNull(message = Message.NOT_MATCH_LOGIN_DTO)
  private String password;
}
