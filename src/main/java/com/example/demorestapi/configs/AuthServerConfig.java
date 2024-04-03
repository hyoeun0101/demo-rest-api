package com.example.demorestapi.configs;

import com.example.demorestapi.accounts.AccountService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.TokenStore;

/**
 * OAuth2 인증서버 등록
 */
@Configuration
@EnableAuthorizationServer
@Slf4j
public class AuthServerConfig extends AuthorizationServerConfigurerAdapter {
    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TokenStore tokenStore;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountService accountService;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        //client secret 확인할 때 passwordEncoder 사용
        log.debug("passwordEncoder:::"+passwordEncoder);
        security.passwordEncoder(passwordEncoder);
    }

    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        //인메모리로 등록. DB에서 관리하는게 이상적!
        clients.inMemory()
                .withClient("myApp")//client id
                .secret(this.passwordEncoder.encode("pass"))
                .authorizedGrantTypes("password","refresh_token")
                .scopes("read", "write")
                .accessTokenValiditySeconds(10 * 60) //10m
                .refreshTokenValiditySeconds(60 * 60); //1h
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        //AuthenticationManger, UserDetails, TokenStore 설정
        log.debug("authenticationManager:::"+authenticationManager);
        log.debug("tokenStore:::"+tokenStore);
        log.debug("accountService:::"+accountService);

        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore)
                .userDetailsService(accountService);

    }
}
