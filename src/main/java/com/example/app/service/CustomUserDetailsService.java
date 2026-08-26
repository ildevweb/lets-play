package com.example.app.service;

import org.springframework.stereotype.Service;
import com.example.app.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.app.model.User;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        User user = userRepository.findByName(username);

        return org.springframework.security.core.userdetails.User
                .withUsername(user.getId())
                .password(user.getPassword())
                .authorities("ROLE_" + user.getRole())
                .build();
    }
}