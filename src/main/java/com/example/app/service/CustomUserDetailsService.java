package com.example.app.service;

import org.springframework.stereotype.Service;
import com.example.app.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import com.example.app.model.User;
import java.util.Optional;


@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username)
            throws UsernameNotFoundException {

        Optional<User> user = userRepository.findByName(username);

        if (user.isEmpty()) {
            throw new UsernameNotFoundException(
                "User not found with username: " + username
            );
        }

        return org.springframework.security.core.userdetails.User
                .withUsername(user.get().getId())
                .password(user.get().getPassword())
                .authorities("ROLE_" + user.get().getRole())
                .build();
    }
}