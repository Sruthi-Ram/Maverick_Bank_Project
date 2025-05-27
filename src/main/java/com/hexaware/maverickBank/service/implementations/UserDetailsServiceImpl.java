package com.hexaware.maverickBank.service.implementations;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hexaware.maverickBank.entity.User;
import com.hexaware.maverickBank.repository.IUserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("loadUserByUsername called for username: {}", username);
        User user = userRepository.findByUsernameOrEmail(username);

        if (user == null) {
            log.warn("User not found with username/email: {}", username);
            throw new UsernameNotFoundException("User not found with username: " + username);
        }

        log.info("User found: Username={}, Email={}, Password={}, Role={}",
                user.getUsername(), user.getEmail(), user.getPassword(), user.getRole());

        List<GrantedAuthority> authorities = new ArrayList<>();
        if (user.getRole() != null) {
            log.info("Adding authority: ROLE_{}", user.getRole().getName().toUpperCase());
            authorities.add(new SimpleGrantedAuthority("ROLE_" + user.getRole().getName().toUpperCase()));
        } else {
            log.warn("User role is null for user: {}", username);
        }

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }
}