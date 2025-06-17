package com.hexaware.maverickBank.service.implementations;

import java.util.Collections;
import java.util.List;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.hexaware.maverickBank.entity.Role;
import com.hexaware.maverickBank.entity.User;
import com.hexaware.maverickBank.repository.IUserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IUserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userIdStr) throws UsernameNotFoundException {
        try {
            Long userId = Long.parseLong(userIdStr);
            User user = userRepository.findByUserId(userId);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with ID: " + userId);
            }
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    mapRoleToAuthorities(user.getRole())
            );
        } catch (NumberFormatException e) {
            
            User user = userRepository.findByUsernameOrEmail(userIdStr);
            if (user == null) {
                throw new UsernameNotFoundException("User not found with username or email: " + userIdStr);
            }
            return new org.springframework.security.core.userdetails.User(
                    user.getUsername(),
                    user.getPassword(),
                    mapRoleToAuthorities(user.getRole())
            );
        }
    }
    private List<SimpleGrantedAuthority> mapRoleToAuthorities(Role role) {
        if (role == null || role.getName() == null) {
           
            return Collections.emptyList();
        }
        return Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + role.getName()));
    }
}
