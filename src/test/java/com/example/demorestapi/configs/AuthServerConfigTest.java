package com.example.demorestapi.configs;

import com.example.demorestapi.accounts.Account;
import com.example.demorestapi.accounts.AccountRole;
import com.example.demorestapi.accounts.AccountService;
import com.example.demorestapi.common.BaseControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Set;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AuthServerConfigTest extends BaseControllerTest {
    @Autowired
    AccountService accountService;

    @Test
    @DisplayName("인증 토큰을 발급 받는 테스트")
    public void getAuthToken() throws Exception {
        // given
        String username = "eunoo@gmail.com";
        String password = "1234";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountService.saveAccount(account);

        String clientId = "myApp";
        String clientSecret = "pass";

        //when & then
        this.mockMvc.perform(post("/oauth/token")
                        .with(httpBasic(clientId,clientSecret))
                        .param("username",username)
                        .param("password",password)
                        .param("grant_type","password")
                )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("access_token").exists());

    }

}