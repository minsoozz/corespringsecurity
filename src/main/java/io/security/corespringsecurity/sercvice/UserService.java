package io.security.corespringsecurity.sercvice;

import io.security.corespringsecurity.domain.entity.Account;

public interface UserService {

  void createUser(Account account);

}
