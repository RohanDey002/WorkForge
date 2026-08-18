package com.taskmanagement.aitaskmanagement.security;

import com.taskmanagement.aitaskmanagement.entity.User;
import com.taskmanagement.aitaskmanagement.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        User user = userRepository
                .findByEmail(email)
                .orElseThrow(()->
                        new RuntimeException(
                                "User not find with email"+email
                        ));
        return new CustomUserDetails(user);
    }
}
