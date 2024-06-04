package com.example.securityexample.user.controller;

import com.example.securityexample.global.constants.Message;
import com.example.securityexample.user.dto.LoginDto;
import com.example.securityexample.user.entity.User;
import com.example.securityexample.user.service.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/login")
public class LoginController {

  private final UserService userService;

  @PostMapping("")
  public ResponseEntity<String> login(@RequestBody @Valid LoginDto loginDto, HttpSession session) {
    User loggedInUser = userService.login(loginDto);
    session.setAttribute("user", loggedInUser);

    return ResponseEntity.ok(Message.LOGIN_SUCCESS);
  }

}
