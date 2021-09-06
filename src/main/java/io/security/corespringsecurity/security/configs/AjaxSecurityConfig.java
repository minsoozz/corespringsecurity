package io.security.corespringsecurity.security.configs;

import io.security.corespringsecurity.security.common.AjaxLoginAuthenticationEntryPoint;
import io.security.corespringsecurity.security.handler.AjaxAccessDeniedHandler;
import io.security.corespringsecurity.security.handler.AjaxAuthenticationFailureHandler;
import io.security.corespringsecurity.security.handler.AjaxAuthenticationSuccessHandler;
import io.security.corespringsecurity.security.provider.AjaxAuthenticationProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.access.AccessDeniedHandler;

@Configuration
@Order(0)
public class AjaxSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    auth.authenticationProvider(ajaxAuthenticationProvider());
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {

    http
        .antMatcher("/api/**")
        .authorizeRequests()
        .antMatchers("/api/messages").hasRole("MANAGER")
        .antMatchers("/api/login").permitAll()
        .anyRequest().authenticated()
        .and()
        .exceptionHandling()
        .authenticationEntryPoint(new AjaxLoginAuthenticationEntryPoint())
        .accessDeniedHandler(ajaxAccessDeniedHandler())
    ;
//        http.csrf().disable();

    ajaxConfigurer(http);
  }

  private void ajaxConfigurer(HttpSecurity http) throws Exception {
    http
        .apply(new AjaxLoginConfigurer<>())
        .successHandlerAjax(ajaxAuthenticationSuccessHandler())
        .failureHandlerAjax(ajaxAuthenticationFailureHandler())
        .loginPage("/api/login")
        .loginProcessingUrl("/api/login")
        .setAuthenticationManager(authenticationManagerBean());
  }

  @Bean
  public AccessDeniedHandler ajaxAccessDeniedHandler() {
    return new AjaxAccessDeniedHandler();
  }

  @Bean
  public AuthenticationProvider ajaxAuthenticationProvider() {
    return new AjaxAuthenticationProvider(passwordEncoder());
  }

  @Bean
  public AjaxAuthenticationSuccessHandler ajaxAuthenticationSuccessHandler() {
    return new AjaxAuthenticationSuccessHandler();
  }

  @Bean
  public AjaxAuthenticationFailureHandler ajaxAuthenticationFailureHandler() {
    return new AjaxAuthenticationFailureHandler();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return PasswordEncoderFactories.createDelegatingPasswordEncoder();
  }
}