package com.example.demorestapi.accounts;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService implements UserDetailsService {
    @Autowired
    private final AccountRepository accountRepository;

    @Autowired
    private final PasswordEncoder passwordEncoder;

    public Account saveAccount(Account account) {
        account.setPassword((this.passwordEncoder.encode(account.getPassword())));
        return this.accountRepository.save(account);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //1. DB에서 해당 유저정보를 검색한다.
        Account account = accountRepository.findByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException(username));

        //UserDetails의 구현체 User를 사용하여 Account를 UserDetails로 변환한다.
        return new User(account.getEmail(), account.getPassword(), authorities(account.getRoles()));
    }

    // Account의 role을 GrantedAuthority로 변환하는 메서드
    private Collection<? extends GrantedAuthority> authorities(Set<AccountRole> roles) {
        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }
}
