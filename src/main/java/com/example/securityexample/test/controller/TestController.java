package com.example.securityexample.test.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tests")
public class TestController {

  // 인증한 사용자만 사용 가능한 API
  @PostMapping("/books")
  public String createBook(@RequestBody String bookInfo) {
    return "createBook";
  }

  @GetMapping("/users/my-info")
  public String getMyInfo() {
    return "getMyInfo";
  }

  // 관리자만 사용 가능한 API
  @PatchMapping("/books/{isbn}")
  public String updateBookStatus(@PathVariable(name = "isbn") Long isbn, @RequestBody String status) {
    return "updateBookStatus";
  }

  // 인증 여부에 관계 없이 사용 가능한 API
  @GetMapping("/books/list")
  public String getBooks(@RequestParam(name = "filter") String filter, @RequestParam(name = "keyword") String keyword) {
    return "searchBooks";
  }

  // 로그인은 필터로 구현, 회원가입은 user 패키지 내에 구현
}
