package io.security.corespringsecurity.security.configs;

import io.security.corespringsecurity.security.common.FormAuthenticationDetailsSource;
import io.security.corespringsecurity.security.filter.AjaxLoginProcessingFilter;
import io.security.corespringsecurity.security.handler.CustomAccessDeniedHandler;
import io.security.corespringsecurity.security.provider.FormAuthenticationProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Slf4j
@Configuration
@EnableWebSecurity
@Order(1)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

  @Autowired
  private FormAuthenticationDetailsSource authenticationDetailsSource;

  @Autowired
  private AuthenticationSuccessHandler customAuthenticationSuccessHandler;

  @Autowired
  private AuthenticationFailureHandler customAuthenticationFailureHandler;

  /**
   * 스프링 시큐리티가 사용자가 만든 UserDetailsService 구현체를 사용해서 인증처리를 하게 된다
   *
   * @param auth
   * @throws Exception
   */
  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(authenticationProvider());
  }

  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  public AuthenticationProvider authenticationProvider() {
    return new FormAuthenticationProvider(passwordEncoder());
  }

  /**
   * WebIgnore 설정
   *
   * @param web
   * @throws Exception
   */
  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring().requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    /*web.ignoring().requestMatchers().antMatchers("/favicon.ico","/resources/**","/error");*/
  }

  @Override
  protected void configure(final HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/", "/users", "user/login/**", "/login*").permitAll()
        .antMatchers("/mypage").hasRole("USER")
        .antMatchers("/messages").hasRole("MANAGER")
        .antMatchers("/config").hasRole("ADMIN")
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .loginProcessingUrl("/login_proc")
        .defaultSuccessUrl("/")
        .authenticationDetailsSource(authenticationDetailsSource)
        .successHandler(customAuthenticationSuccessHandler)
        .failureHandler(customAuthenticationFailureHandler)
        .permitAll()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
        .accessDeniedPage("/denied")
        .accessDeniedHandler(accessDeniedHandler());
  }


  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }

  @Bean
  public AccessDeniedHandler accessDeniedHandler() {
    CustomAccessDeniedHandler accessDeniedHandler = new CustomAccessDeniedHandler();
    accessDeniedHandler.setErrorPage("/denied");
    return accessDeniedHandler;
  }
}
