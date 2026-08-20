package com.taskmanagement.aitaskmanagement.security;


import com.taskmanagement.aitaskmanagement.entity.Permissions;
import com.taskmanagement.aitaskmanagement.entity.Role;
import com.taskmanagement.aitaskmanagement.entity.User;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.Nullable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

    private final User user;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {

        List<GrantedAuthority> authorities = new ArrayList<>();

        Role role = user.getRole();

        authorities.add( new SimpleGrantedAuthority("ROLE_"+ role.name()));

        for (Permissions permissions : role.getPermissions()){
            authorities.add(new SimpleGrantedAuthority(permissions.name()));
        }


        return authorities;
    }

    @Override
    public  String getPassword() {
        return user.getPassword();
    }

    @Override
    public String getUsername() {

        return user.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId(){

        return user.getId();
    }
    public User getUser(){

        return user;
    }

    public Role getRole(){

        return user.getRole();
    }
}
