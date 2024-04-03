package com.example.demorestapi.configs;

import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.InMemoryTokenStore;
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


    //web filter 설정
    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return web -> web.ignoring()
                .requestMatchers("/docs/index.html")
                //정적 리소스
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }

    //SecurityFilterChain 설정. 각 http 요청에 대한 설정임.
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // web Filter에서 걸러주기.
//        http.authorizeHttpRequests(authorize -> authorize
//                .requestMatchers("/docs/index.html").anonymous()
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).anonymous()
//                .anyRequest().authenticated())
//        ;

        http.authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(HttpMethod.GET,"/api/**").anonymous()
                        .anyRequest().authenticated()
                )
                .formLogin(Customizer.withDefaults());

        return http.build();
    }


}
