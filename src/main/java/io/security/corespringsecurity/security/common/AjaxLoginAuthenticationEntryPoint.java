package io.security.corespringsecurity.security.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

public class AjaxLoginAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private ObjectMapper objectMapper = new ObjectMapper();

  /**
   * 클라이언트로부터 인증을 받지 않은 상태로 자원에 접근을 했기 때문에 401 에러코드를 전달한다
   *
   * @param request
   * @param response
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public void commence(HttpServletRequest request, HttpServletResponse response,
      AuthenticationException authException) throws IOException, ServletException {

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setStatus(HttpStatus.UNAUTHORIZED.value());
    response.getWriter()
        .write(objectMapper.writeValueAsString(HttpServletResponse.SC_UNAUTHORIZED));
  }
}
