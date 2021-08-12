package io.security.corespringsecurity.security.common;

import javax.servlet.http.HttpServletRequest;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

public class FormWebAuthenticationDetails extends WebAuthenticationDetails {

  private String secretKey;

  /**
   * 사용자가 전달하는 추가적인 파라미터를 저장하는 클래스
   *
   * @param request
   */
  public FormWebAuthenticationDetails(HttpServletRequest request) {
    super(request);
    secretKey = request.getParameter("secret_key");
  }

  public String getSecretKey() {
    return secretKey;
  }
}
