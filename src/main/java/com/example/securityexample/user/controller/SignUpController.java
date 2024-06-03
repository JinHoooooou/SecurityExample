package com.example.securityexample.user.controller;

import com.example.securityexample.global.constants.Message;
import com.example.securityexample.user.dto.SignUpDto;
import com.example.securityexample.user.service.UserService;
import jakarta.validation.Valid;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/signup")
public class SignUpController {

  private final UserService userService;

  @PostMapping(value = "", produces = "application/json;charset=UTF-8")
  public ResponseEntity<Map<String, String>> signUp(@RequestBody @Valid SignUpDto signUpDto) {
    userService.createNewUser(signUpDto);

    return ResponseEntity
        .status(HttpStatus.CREATED)
        .body(Map.of("message", Message.SIGNUP_SUCCESS));
  }
}
