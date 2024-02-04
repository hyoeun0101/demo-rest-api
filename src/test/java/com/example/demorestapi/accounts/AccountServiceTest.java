package com.example.demorestapi.accounts;

import org.hamcrest.Matchers;
import org.junit.Rule;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.rules.ExpectedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@ActiveProfiles("test")
class AccountServiceTest {
    @Autowired
    AccountService accountService;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    PasswordEncoder passwordEncoder;
    @Test
    public void findByUsername() {
        //given
        String username = "eunoo@gmail.com";
        String password = "1234";
        Account account = Account.builder()
                .email(username)
                .password(password)
                .roles(Set.of(AccountRole.ADMIN, AccountRole.USER))
                .build();
        this.accountService.saveAccount(account);

        //when
        UserDetailsService userDetailsService = accountService;
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        //then
        assertThat(userDetails.getUsername()).isEqualTo(username);
        assertThat(this.passwordEncoder.matches(password, userDetails.getPassword())).isTrue();
    }

    @Test
    @DisplayName("방법1. Assertions.assertThrows 사용")
    public void findByUsernameFail() {
        //when & then
        //방법 1. Assertions.assertThrows 사용.
        assertThrows(UsernameNotFoundException.class,() -> {
            accountService.loadUserByUsername("test@gmail.com");
        });
    }

    @Test
    @DisplayName("방법2. try-catch 사용")
    public void findByUsernameFail2() {
        try {
            accountService.loadUserByUsername("test");
            fail("supposed to be failed");
        } catch (UsernameNotFoundException e) {
            assertThat(e.getMessage()).containsSequence("test");
        }
    }

}