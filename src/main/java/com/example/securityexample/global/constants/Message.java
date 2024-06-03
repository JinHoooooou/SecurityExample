package com.example.securityexample.global.constants;

public interface Message {

  String SIGNUP_SUCCESS = """
      가입이 완료되었습니다.
      환영합니다.
      로그인 후 이용해 주세요 😊
      """;
  String INVALID_EMAIL = "유효하지 않은 이메일 형식입니다.";
  String INVALID_PASSWORD = "영문, 숫자, 특수기호를 포함하여 8자 이상 20자 이하로 입력해주세요.";
  String INVALID_PASSWORD_CONFIRM = "비밀번호와 비밀번호 확인이 일치하지 않습니다.";
  String INVALID_NICKNAME = "영문, 유효한 한글, 숫자를 이용하여 3자 이상 20자 이하로 입력해주세요.";
  String INVALID_ADDRESS = "유효하지 않은 주소 형식입니다. 현재는 서울시만 등록 가능합니다.";
  String INVALID_PHONE = "유효하지 않은 휴대폰 번호 형식입니다.";
  String DUPLICATE_EMAIL = "이미 가입된 이메일입니다.";
  String DUPLICATE_NICKNAME = "이미 가입된 닉네임입니다.";
}
