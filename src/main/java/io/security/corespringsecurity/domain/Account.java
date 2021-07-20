package io.security.corespringsecurity.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import lombok.Data;

@Entity
@Data
public class Account {

  @Id
  @GeneratedValue
  private Long Id;
  private String username;
  private String password;
  private String email;
  private Integer age;
  private String role;
}
