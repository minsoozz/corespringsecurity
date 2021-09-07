package io.security.corespringsecurity.security.filter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import org.springframework.security.access.intercept.InterceptorStatusToken;
import org.springframework.security.web.FilterInvocation;
import org.springframework.security.web.access.intercept.FilterSecurityInterceptor;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

public class PermitAllFilter extends FilterSecurityInterceptor {

  private static final String FILTER_APPLIED = "__spring_security_filterSecurityInterceptor_filterApplied";
  private boolean observeOncePerRequest = true;

  private List<RequestMatcher> permitAllRequestMatchers = new ArrayList<>();

  public PermitAllFilter(String... permitAllResources) {
    for (String resource : permitAllResources) {
      permitAllRequestMatchers.add(new AntPathRequestMatcher(resource));
    }
  }

  @Override
  protected InterceptorStatusToken beforeInvocation(Object object) {

    boolean permitAll = false;
    HttpServletRequest request = ((FilterInvocation) object).getRequest();
    for (RequestMatcher requestMatcher : permitAllRequestMatchers) {
      if (requestMatcher.matches(request)) {
        permitAll = true;
        break;
      }
    }

    if (permitAll) {
      return null;  // return 이 null 이라면 권한 심사를 하지 않음
    }

    return super.beforeInvocation(object);
  }

  public void invoke(FilterInvocation fi) throws IOException, ServletException {
    if (fi.getRequest() != null
        && fi.getRequest().getAttribute("__spring_security_filterSecurityInterceptor_filterApplied")
        != null && this.observeOncePerRequest) {
      fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
    } else {
      if (fi.getRequest() != null && this.observeOncePerRequest) {
        fi.getRequest().setAttribute("__spring_security_filterSecurityInterceptor_filterApplied",
            Boolean.TRUE);
      }

      InterceptorStatusToken token = beforeInvocation(fi);

      try {
        fi.getChain().doFilter(fi.getRequest(), fi.getResponse());
      } finally {
        super.finallyInvocation(token);
      }
      super.afterInvocation(token, (Object) null);
    }
  }
}