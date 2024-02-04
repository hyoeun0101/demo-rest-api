package com.example.demorestapi.configs;

import com.example.demorestapi.accounts.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.WebSecurityConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

/**
 * AuthorizationServer와 ResourceServer의 공통 설정
 */
@Configuration
//@EnableWebSecurity
public class SecurityConfig {
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
        // AuthenticationManager 빈 생성 시 스프링의 내부 동작으로 인해 UserDetailsService, PasswordEncoder가 자동으로 설정됨.
    }

    @Bean
    public TokenStore tokenStore() {
        return new InMemoryTokenStore();
    }


    //http 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http
              .formLogin(formLogin -> formLogin
                      .loginPage("/login"))
              .authorizeHttpRequests((auth) -> auth
                      .requestMatchers(HttpMethod.GET, "/api/**").anonymous()
                      .anyRequest().authenticated()
              )
              ;
      return http.build();
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/docs/index.html")
                //정적 리소스
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }


}
