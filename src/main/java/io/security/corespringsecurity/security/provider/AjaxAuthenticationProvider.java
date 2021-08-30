package io.security.corespringsecurity.security.provider;

import io.security.corespringsecurity.security.common.FormWebAuthenticationDetails;
import io.security.corespringsecurity.security.service.AccountContext;
import io.security.corespringsecurity.security.token.AjaxAuthenticationToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 1. AjaxAuthenticationProvider 인증처리를 하는 클래스
 * 2. AuthenticationManager 로부터 AjaxAuthenticationProvider 가 실제 인증처리를 할 수 있도록 위임을 받는다
 * 3. 그 AuthenticationManager 로 부터 인증 객체를 전달 받는다
 * 4. Authentication 객체에는 User 정보가 담겨있다
 * 5. Authentication 사용해서 userDetailService 로 부터 인증 객체를 하나 얻는다
 * 6. AccountContext Type 으로 반환받는다
 * 7. 비밀번호 일치 여부를 확인
 * 8. 만약 부가 정보가 있다면 일치 여부를 확인
 * 9. 검증이 완료되면 실제로 인증 객체를 생성하고 그 인증 객체 안에 account 객체와 authorities (권한정보) 를 담아서 AuthenticationManager 에게 반환한다
 */
public class AjaxAuthenticationProvider implements AuthenticationProvider {

  @Autowired
  private UserDetailsService userDetailsService;

  @Autowired
  private PasswordEncoder passwordEncoder;

  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {

    String loginId = authentication.getName();
    String password = (String) authentication.getCredentials();

    AccountContext accountContext = (AccountContext) userDetailsService
        .loadUserByUsername(loginId);

    if (!passwordEncoder.matches(password, accountContext.getAccount().getPassword())) {
      throw new BadCredentialsException("BadCredentialsException");
    }

    return new AjaxAuthenticationToken(accountContext.getAccount(), null,
        accountContext.getAuthorities());
  }

  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(AjaxAuthenticationToken.class);
  }
}
