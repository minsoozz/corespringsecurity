package io.security.corespringsecurity.security.common;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

public class AjaxAccessDeniedHandler implements AccessDeniedHandler {

  /**
   * 인증은 받았지만 권한이 없는 경우 호출
   * @param request
   * @param response
   * @param e
   * @throws IOException
   * @throws ServletException
   */
  @Override
  public void handle(HttpServletRequest request, HttpServletResponse response,
      AccessDeniedException e) throws IOException, ServletException {
    response.sendError(HttpServletResponse.SC_FORBIDDEN, " Access is denied");
  }
}
