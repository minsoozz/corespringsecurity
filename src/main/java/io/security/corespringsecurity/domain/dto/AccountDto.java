package io.security.corespringsecurity.domain.dto;

import lombok.Data;

@Data
public class AccountDto {

  private Long Id;
  private String username;
  private String password;
  private String email;
  private Integer age;
  private String role;
}
